package hr.algebra.turistika.repository;

import hr.algebra.turistika.model.TuristickiVodic;
import hr.algebra.turistika.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TuristickiVodicRepositoryImpl implements TuristickiVodicRepository {
    private final Connection connection;

    public TuristickiVodicRepositoryImpl() {
        this.connection = DatabaseUtil.getInstance().getConnection();
    }

    @Override
    public List<TuristickiVodic> findAll() {
        List<TuristickiVodic> turistickiVodici = new ArrayList<>();
        String sql = "SELECT * FROM TuristickiVodic";

        try (var stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()){
                turistickiVodici.add(mapResultSet(rs));
            }
        } catch (SQLException e){
            throw new RuntimeException("Greska pri dohvacanju turistickih vodica", e);
        }
        return turistickiVodici;
    }

    @Override
    public Optional<TuristickiVodic> findById(Long id) {
        String sql = "SELECT * FROM TuristickiVodic WHERE id = ?";

        try (var stmt = connection.prepareStatement(sql)){
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                return Optional.of(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Greska pri dohvacanju turistickog vodica", e);
        }
        return Optional.empty();
    }

    @Override
    public void save(TuristickiVodic turistickiVodic) {
        String sql = "INSERT INTO TuristickiVodic(ime, prezime, kontakt) VALUES (?, ?, ?)";

        try (var stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1, turistickiVodic.getIme());
            stmt.setString(2, turistickiVodic.getPrezime());
            stmt.setString(3, turistickiVodic.getKontakt());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()){
                turistickiVodic.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Greska pri spremanju turistickog vodica", e);
        }
    }

    @Override
    public void update(TuristickiVodic turistickiVodic) {
        String sql = "UPDATE TuristickiVodic SET ime = ?, prezime = ?, kontakt = ? WHERE id = ?";

        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, turistickiVodic.getIme());
            stmt.setString(2, turistickiVodic.getPrezime());
            stmt.setString(3, turistickiVodic.getKontakt());
            stmt.setLong(4, turistickiVodic.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Greska pri azuriranju turistickog vodica", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM TuristickiVodic WHERE id = ?";

        try (var stmt = connection.prepareStatement(sql)){
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Greska pri brisanju turistickog vodica", e);
        }
    }

    private TuristickiVodic mapResultSet(ResultSet rs) throws SQLException {
        return new TuristickiVodic(
                rs.getLong("id"),
                rs.getString("ime"),
                rs.getString("prezime"),
                rs.getString("kontakt")
        );
    }
}
