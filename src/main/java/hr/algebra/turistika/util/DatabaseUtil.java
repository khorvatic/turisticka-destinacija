package hr.algebra.turistika.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static DatabaseUtil instance;
    private Connection connection;
    private final String url;
    private final String username;
    private final String password;

    private DatabaseUtil() {
        url = "jdbc:h2:file:./data/turistika;AUTO_SERVER=TRUE";
        username = "sa";
        password = "";
        try {
            if (connection == null || connection.isClosed())
                connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Greska pri spajanju na bazu", e);
        }

    }

    public static synchronized DatabaseUtil getInstance() {
        if (instance == null) instance = new DatabaseUtil();
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
