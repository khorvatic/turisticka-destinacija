package hr.algebra.turistika.repository;

import hr.algebra.turistika.model.Zemlja;
import hr.algebra.turistika.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ZemljaRepositoryImpl implements ZemljaRepository{
    private final Connection connection;

    public ZemljaRepositoryImpl() {
        this.connection = DatabaseUtil.getInstance().getConnection();
    }

    @Override
    public List<Zemlja> findAll() {
        List<Zemlja> zemlje = new ArrayList<>();
        String sql = "SELECT * FROM Zemlja";

        try (Statement stmt = connection.createStatement();
            ResultSet result = stmt.executeQuery(sql)){

            while (result.next()){
                zemlje.add(mapResultSet(result));
            }

        } catch (SQLException e){
            throw new RuntimeException("Greska pri dohvacanju zemalja", e);
        }
        return zemlje;
    }

    @Override
    public Optional<Zemlja> findById(Long id) {
        String sql = "SELECT * FROM Zemlja WHERE id = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                return Optional.of(mapResultSet(rs));
            }
        } catch (SQLException e){
            throw new RuntimeException("Greska pri dohvacanju zemlje", e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Zemlja zemlja) {
        String sql = "INSERT INTO Zemlja(naziv, kod_drzave) VALUES (?, ?)";

        try(PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1, zemlja.getNaziv());
            stmt.setString(2, zemlja.getKodDrzave());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if(keys.next()){
                zemlja.setId(keys.getLong(1));
            }
        } catch (SQLException e){
            throw new RuntimeException("Greska pri spremanju zemlje", e);
        }
    }

    @Override
    public void update(Zemlja zemlja) {
        String sql = "UPDATE Zemlja SET naziv = ?, kod_drzave = ? WHERE id = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, zemlja.getNaziv());
            stmt.setString(2, zemlja.getKodDrzave());
            stmt.setLong(3, zemlja.getId());
            stmt.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException("Greska pri ažuriranju zemlje", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM Zemlja WHERE id = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException("Greska pri brisanju zemlje", e);
        }
    }

    private Zemlja mapResultSet(ResultSet rs) throws SQLException {
        return new Zemlja(
                rs.getLong("id"),
                rs.getString("naziv"),
                rs.getString("kod_drzave")
        );
    }
}
