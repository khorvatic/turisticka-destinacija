package hr.algebra.turistika.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseInitializer {
    public static void initialize(){
        executeScript("sql/create_tables.sql");
        createDefaultAdmin();
    }

    public static void executeScript(String path){
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        DatabaseInitializer.class
                                .getClassLoader()
                                .getResourceAsStream(path)
                ))) {
            String sql = reader.lines().collect(Collectors.joining("\n"));
            Connection con = DatabaseUtil.getInstance().getConnection();

            for (String query : sql.split(";")){
                String trimmed = query.trim();
                if (!trimmed.isEmpty()) {
                    try (Statement stmt = con.createStatement()){
                        stmt.execute(trimmed);
                    }
                }
            }

        } catch (Exception e){
            throw new RuntimeException("Greska pri izvrsavanju skripte: " + path, e);
        }
    }

    public static void createDefaultAdmin(){
        String sql = """
                INSERT INTO Korisnik (username, password, uloga)
                SELECT 'admin', 'admin123', 'ADMINISTRATOR'
                WHERE NOT EXISTS (
                    SELECT * FROM Korisnik WHERE username = 'admin'
                )
                """;
        try (Statement stmt = DatabaseUtil.getInstance()
                .getConnection()
                .createStatement()) {
            stmt.execute(sql);
        } catch (Exception e) {
            throw new RuntimeException("Greska pri kreiranju admina", e);
        }
    }
}
