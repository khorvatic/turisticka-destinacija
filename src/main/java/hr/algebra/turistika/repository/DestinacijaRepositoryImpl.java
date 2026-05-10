package hr.algebra.turistika.repository;

import hr.algebra.turistika.model.*;
import hr.algebra.turistika.util.DatabaseUtil;
import org.h2.command.Prepared;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DestinacijaRepositoryImpl implements DestinacijaRepository{
    private final Connection connection;
    private final ZemljaRepository zemljaRepository;
    private final AktivnostRepository aktivnostRepository;

    public DestinacijaRepositoryImpl() {
        this.connection = DatabaseUtil.getInstance().getConnection();
        this.zemljaRepository = new ZemljaRepositoryImpl();
        this.aktivnostRepository = new AktivnostRepositoryImpl();
    }

    @Override
    public List<Destinacija> findAll() {
        List<Destinacija> destinacije = new ArrayList<>();
        String sql = "SELECT * FROM Destinacija";

        try(Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

            while(rs.next()){
                Destinacija d = mapResultSet(rs);
                ucitajAktivnosti(d);
                ucitajVodice(d);
                destinacije.add(d);
            }
        } catch (SQLException e){
            throw new RuntimeException("Greska pri dohvacanju destinacija", e);
        }
        return destinacije;
    }

    @Override
    public Optional<Destinacija> findById(Long id) {
        String sql = "SELECT * FROM Destinacija WHERE id = ?";

        try(var stmt = connection.prepareStatement(sql)){
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                Destinacija d = mapResultSet(rs);
                ucitajVodice(d);
                ucitajAktivnosti(d);
                return Optional.of(d);
            }
        } catch (SQLException e){
            throw new RuntimeException("Greska pri dohvacanju destinacije", e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Destinacija destinacija) {
        String sql = """
                INSERT INTO Destinacija (naziv, opis, latitude, longitude, 
                preporucenoGodisnjeDoba, putanjaFotografija, zemljaId, vrstaPutovanjaId)
                VALUES(?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try(var stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            setDestinacijaParams(stmt, destinacija);
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if(keys.next()){
                destinacija.setId(keys.getLong(1));
            }

            spremiAktivnosti(destinacija);
            spremiVodice(destinacija);
        } catch (SQLException e){
            throw new RuntimeException("Greska pri spremanju destinacije", e);
        }
    }

    @Override
    public void update(Destinacija destinacija) {
        String sql = """
                UPDATE Destinacija SET
                naziv = ?, opis = ?, latitude = ?, logitude = ?, preporucenoGodisnjeDoba = ?, putanjaFotografija = ?,
                zemljaId = ?, vrstaPutovanjaId = ?
                WHERE id = ? 
                """;

        try(var stmt = connection.prepareStatement(sql)){
            setDestinacijaParams(stmt, destinacija);
            stmt.setLong(9, destinacija.getId());
            stmt.executeUpdate();

            obrisiAktivnosti(destinacija.getId());
            obrisiVodice(destinacija.getId());
            spremiAktivnosti(destinacija);
            spremiVodice(destinacija);
        } catch(SQLException e){
            throw new RuntimeException("Greska pri azuriranju destinacije", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM Destinacije WHERE id = ?";
        obrisiAktivnosti(id);
        obrisiVodice(id);

        try(var stmt = connection.prepareStatement(sql)){
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Greska pri brisanju destinacije", e);
        }
    }

    @Override
    public List<Destinacija> findByZemlja(Long zemljaId) {
        List<Destinacija> destinacije = new ArrayList<>();
        String sql = "SELECT * FROM Destinacija WHERE zemljaId = ?";

        try(var stmt = connection.prepareStatement(sql)){
            stmt.setLong(1, zemljaId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                Destinacija d = mapResultSet(rs);
                ucitajAktivnosti(d);
                destinacije.add(d);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Greska pri dohvacanju destinacija u zemlji", e);
        }
        return destinacije;
    }

    @Override
    public List<Destinacija> findByVrstaPutovanja(VrstaPutovanja vrsta) {
        List<Destinacija> destinacije = new ArrayList<>();
        String sql = "SELECT * FROM Destinacija WHERE vrstaPutovanjaId = ?";

        try(var stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, vrsta.ordinal() + 1);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                destinacije.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Greska pri dohvacanju destinacija po vrsti putovanja", e);
        }
        return destinacije;
    }

    // ==== Helper metode ====
    private Destinacija mapResultSet(ResultSet rs) throws SQLException{
        Long zemljaId = rs.getLong("zemljaId");
        Zemlja zemlja = zemljaRepository.findById(zemljaId).orElse(null);

        int vrstaPutovanjaId = rs.getInt("vrstaPutovanjaId");
        VrstaPutovanja vrsta = VrstaPutovanja.values()[vrstaPutovanjaId - 1];

        return new Destinacija(
                rs.getLong("id"),
                rs.getString("naziv"),
                rs.getString("opis"),
                rs.getDouble("latitude"),
                rs.getDouble("longitude"),
                rs.getString("preporucenoGodisnjeDoba"),
                rs.getString("putanjaFotografije"),
                zemlja,
                vrsta
        );
    }

    private void setDestinacijaParams(PreparedStatement stmt, Destinacija d) throws SQLException{
        stmt.setString(1, d.getNaziv());
        stmt.setString(2, d.getOpis());
        stmt.setDouble(3, d.getLatitude());
        stmt.setDouble(4, d.getLongitude());
        stmt.setString(5, d.getPreporucenoGodisnjeDoba());
        stmt.setString(6, d.getPutanjaFotografije());
        stmt.setLong(7, d.getZemlja().getId());
        stmt.setInt(8, d.getVrstaPutovanja().ordinal() + 1);
    }

    private void ucitajAktivnosti(Destinacija destinacija) throws SQLException{
        String sql = """
                SELECT a.* FROM Aktivnost a
                JOIN DestinacijaAktivnost da ON a.id = da.aktivnostId
                WHERE da.destinacijaId = ?
                """;

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setLong(1, destinacija.getId());
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                destinacija.addAktivnost(new Aktivnost(
                        rs.getLong("id"),
                        rs.getString("naziv"),
                        rs.getString("opis")
                ));
            }
        }
    }

    private void ucitajVodice(Destinacija destinacija) throws SQLException{
        String sql = """
                SELECT tv.* FROM TuristickiVodic tv
                JOIN DestinacijaVodic dv ON tv.id = dv.vodicId
                WHERE dv.destinacijaId = ?
                """;
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setLong(1, destinacija.getId());
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                destinacija.addTuristickiVodic(new TuristickiVodic(
                        rs.getLong("id"),
                        rs.getString("ime"),
                        rs.getString("prezime"),
                        rs.getString("kontakt")
                ));
            }
        }
    }

    private void spremiAktivnosti(Destinacija destinacija) throws SQLException{
        String sql = "INSERT INTO DestinacijaAktivnost VALUES(?, ?)";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            for(Aktivnost a : destinacija.getAktivnosti()){
                stmt.setLong(1, destinacija.getId());
                stmt.setLong(2, a.getId());
                stmt.executeUpdate();
            }
        }
    }

    private void spremiVodice(Destinacija destinacija) throws SQLException{
        String sql = "INSERT INTO DestinacijaVodic VALUES(?, ?)";

        try (var stmt = connection.prepareStatement(sql)){
            for (TuristickiVodic v : destinacija.getTuristickiVodici()){
                stmt.setLong(1, destinacija.getId());
                stmt.setLong(1, v.getId());
                stmt.executeUpdate();
            }
        }
    }

    private void obrisiAktivnosti(Long destinacijaId){
        String sql = "DELETE FROM DestinacijaAktivnost WHERE destinacijaId = ?";

        try (var stmt = connection.prepareStatement(sql)){
            stmt.setLong(1, destinacijaId);
            stmt.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException("Greska pri brisanju aktivnosti destinacije", e);
        }
    }

    private void obrisiVodice(Long destinacijaId){
        String sql = "DELETE FROM DestinacijaVodic WHERE destinacijaId = ?";

        try (var stmt = connection.prepareStatement(sql)){
            stmt.setLong(1, destinacijaId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Greska pri brisanju vodica destinacije", e);
        }
    }
}
