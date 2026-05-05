package hr.algebra.turistika.repository;

import hr.algebra.turistika.model.Aktivnost;
import hr.algebra.turistika.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AktivnostRepositoryImpl implements AktivnostRepository{

    private final Connection connection;

    public AktivnostRepositoryImpl() {
        this.connection = DatabaseUtil.getInstance().getConnection();
    }

    @Override
    public List<Aktivnost> findAll() {
        List<Aktivnost> aktivnosti = new ArrayList<>();
        String sql = "SELECT * FROM Aktivnost";

        try(var stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()){
                aktivnosti.add(mapResultSet(rs));
            }
        } catch (SQLException e){
            throw new RuntimeException("Greska pri dohvacanju aktivnosti", e);
        }
        return aktivnosti;
    }

    @Override
    public Optional<Aktivnost> findById(Long id) {
        String sql = "SELECT * FROM Aktivnost WHERE id = ?";

        try(var stmt = connection.prepareStatement(sql)){
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return Optional.of(mapResultSet(rs));
            }

        } catch (SQLException e){
            throw new RuntimeException("Greska pri dohvacanju aktivnosti", e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Aktivnost aktivnost) {
        String sql = "INSERT INTO Aktivnost(naziv, opis) VALUES (?, ?)";

        try(var stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1, aktivnost.getNaziv());
            stmt.setString(2, aktivnost.getOpis());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if(keys.next()){
                aktivnost.setId(keys.getLong(1));
            }
        } catch (SQLException e){
            throw new RuntimeException("Greska pri spremanju aktivnosti", e);
        }
    }

    @Override
    public void update(Aktivnost aktivnost) {
        String sql = "UPDATE Aktivnost SET naziv = ?, opis = ? WHERE id = ?";

        try(var stmt = connection.prepareStatement(sql)){
            stmt.setString(1, aktivnost.getNaziv());
            stmt.setString(2, aktivnost.getOpis());
            stmt.setLong(3, aktivnost.getId());
            stmt.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException("Greska pri azuriranju aktivnosti", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM Aktivnost WHERE id = ?";

        try(var stmt = connection.prepareStatement(sql)){
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException("Greska pri brisanju aktivnosti", e);
        }
    }

    private Aktivnost mapResultSet(ResultSet rs) throws SQLException {
        return new Aktivnost(
                rs.getLong("id"),
                rs.getString("naziv"),
                rs.getString("opis")
        );
    }
}
