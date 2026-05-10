package hr.algebra.turistika.repository;

import hr.algebra.turistika.model.Admin;
import hr.algebra.turistika.model.Korisnik;
import hr.algebra.turistika.model.User;
import hr.algebra.turistika.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository{
    private final Connection connection;
    public UserRepositoryImpl() {
        this.connection = DatabaseUtil.getInstance().getConnection();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM korisnik WHERE username = ?";

        try (var stmt = connection.prepareStatement(sql)){
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()){
                Long id = rs.getLong("id");
                String user = rs.getString("username");
                String password = rs.getString("password");
                String uloga = rs.getString("uloga");

                if (uloga.equals("ADMINISTRATOR")){
                    return Optional.of(new Admin(id, user, password));
                } else {
                    return Optional.of(new Korisnik(id, user, password));
                }
            }
        } catch (SQLException e){
            throw new RuntimeException("Greska pri dohvacanju korisnika", e);
        }
        return Optional.empty();
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO Korisnik(username, password, uloga) VALUES (?, ?, ?)";

        try (var stmt = connection.prepareStatement(sql)){
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            stmt.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException("Greska pri spremanju korisnika", e);
        }
    }
}
