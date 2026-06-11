package hr.algebra.turistika.service;

import hr.algebra.turistika.model.*;
import hr.algebra.turistika.repository.*;
import hr.algebra.turistika.util.DatabaseInitializer;
import hr.algebra.turistika.util.DatabaseUtil;
import hr.algebra.turistika.xml.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import javax.xml.crypto.Data;
import java.io.File;
import java.util.List;

public class BackupService {
    private static final String BACKUP_PATH = "backup/backup.xml";

    private final ZemljaRepository zemljaRepository;
    private final AktivnostRepository aktivnostRepository;
    private final TuristickiVodicRepository turistickiVodicRepository;
    private final DestinacijaRepository destinacijaRepository;

    public BackupService() {
        this.zemljaRepository = new ZemljaRepositoryImpl();
        this.aktivnostRepository = new AktivnostRepositoryImpl();
        this.turistickiVodicRepository = new TuristickiVodicRepositoryImpl();
        this.destinacijaRepository = new DestinacijaRepositoryImpl();
    }

    public void backup() {
        List<ZemljaBackup> zemlje = zemljaRepository.findAll().stream()
                .map(ZemljaBackup::new).toList();
        List<AktivnostBackup> aktivnosti = aktivnostRepository.findAll().stream()
                .map(AktivnostBackup::new).toList();
        List<VodicBackup> vodici = turistickiVodicRepository.findAll().stream()
                .map(VodicBackup::new).toList();
        List<DestinacijaBackup> destinacije = destinacijaRepository.findAll().stream()
                .map(DestinacijaBackup::new).toList();

        System.out.println("Backup destinacija: " + destinacije.size());

        DatabaseBackup backup = new DatabaseBackup(zemlje, aktivnosti, vodici, destinacije);

        try {
            File file = new File(BACKUP_PATH);
            file.getParentFile().mkdirs();

            JAXBContext context = JAXBContext.newInstance(
                    DatabaseBackup.class, ZemljaBackup.class,
                    AktivnostBackup.class, VodicBackup.class,
                    DestinacijaBackup.class);

            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(backup, file);
        } catch (Exception e) {
            throw new RuntimeException("Greska pri backupu baze podataka", e);
        }
    }

    public void restoreBackup() {
        try {
            File file = new File(BACKUP_PATH);
            if (!file.exists()) throw new RuntimeException("Backup ne postoji!");

            JAXBContext context = JAXBContext.newInstance(
                    DatabaseBackup.class, ZemljaBackup.class,
                    AktivnostBackup.class, VodicBackup.class,
                    DestinacijaBackup.class);

            Unmarshaller unmarshaller = context.createUnmarshaller();
            DatabaseBackup backup = (DatabaseBackup) unmarshaller.unmarshal(file);

            System.out.println("Zemlje: " + (backup.getZemlje() != null ? backup.getZemlje().size() : "NULL"));
            System.out.println("Aktivnosti: " + (backup.getAktivnosti() != null ? backup.getAktivnosti().size() : "NULL"));
            System.out.println("Vodici: " + (backup.getVodici() != null ? backup.getVodici().size() : "NULL"));
            System.out.println("Destinacije: " + (backup.getDestinacije() != null ? backup.getDestinacije().size() : "NULL"));
            // Obrise postojece podatke
            DatabaseInitializer.executeScript("sql/clear_tables.sql");
            DatabaseInitializer.createDefaultAdmin();
            restoreVrstePutovanja();

            var connection = DatabaseUtil.getInstance().getConnection();

            // Zemlja
            try (var stmt = connection.prepareStatement(
                    "INSERT INTO Zemlja(id, naziv, kod_drzave) VALUES (?, ?, ?)")) {
                for (ZemljaBackup z : backup.getZemlje()) {
                    stmt.setLong(1, z.getId());
                    stmt.setString(2, z.getNaziv());
                    stmt.setString(3, z.getKodDrzave());
                    stmt.executeUpdate();
                }
            }

            // Aktivnost
            try (var stmt = connection.prepareStatement(
                    "INSERT INTO Aktivnost(id, naziv, opis) VALUES (?, ?, ?)")) {
                for (AktivnostBackup a : backup.getAktivnosti()) {
                    stmt.setLong(1, a.getId());
                    stmt.setString(2, a.getNaziv());
                    stmt.setString(3, a.getOpis());
                    stmt.executeUpdate();
                }
            }

            // TuristickiVodic
            try (var stmt = connection.prepareStatement(
                    "INSERT INTO TuristickiVodic(id, ime, prezime, kontakt) VALUES (?, ?, ?, ?)")) {
                for (VodicBackup v : backup.getVodici()) {
                    stmt.setLong(1, v.getId());
                    stmt.setString(2, v.getIme());
                    stmt.setString(3, v.getPrezime());
                    stmt.setString(4, v.getKontakt());
                }
            }

            // Destinacija
            for (DestinacijaBackup d : backup.getDestinacije()) {
                Zemlja zemlja = zemljaRepository.findById(d.getZemljaId()).orElse(null);
                VrstaPutovanja vrsta = VrstaPutovanja.valueOf(d.getVrstaPutovanja());
                Destinacija dest = new Destinacija(d.getId(), d.getNaziv(), d.getOpis(),
                        d.getLatitude(), d.getLongitude(), d.getPreprucenoGodisnjeDoba(),
                        "", zemlja, vrsta);

                if (d.getAktivnostiIds() != null) {
                    d.getAktivnostiIds().forEach(id ->
                            aktivnostRepository.findById(id).ifPresent(dest::addAktivnost));
                }

                // Destinacija.save() samo sto moramo forsirati ID
                try (var stmt = connection.prepareStatement(
                        """
                            INSERT INTO Destinacija(id, naziv, opis, latitude, longitude, preporucenoGodisnjeDoba, putanjaFotografija, zemljaId, vrstaPutovanjaId)
                            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                            """
                )) {
                    stmt.setLong(1, d.getId());
                    stmt.setString(2, d.getNaziv());
                    stmt.setString(3, d.getOpis());
                    stmt.setDouble(4, d.getLatitude() != null ? d.getLatitude() : 0);
                    stmt.setDouble(5, d.getLongitude() != null ? d.getLongitude() : 0);
                    stmt.setString(6, d.getPreprucenoGodisnjeDoba());
                    stmt.setString(7, "");
                    stmt.setLong(8, d.getZemljaId());
                    stmt.setInt(9, vrsta.ordinal() + 1);
                    stmt.executeUpdate();
                }

                if (d.getAktivnostiIds() != null) {
                    try (var stmt = connection.prepareStatement(
                            "INSERT INTO DestinacijaAktivnost VALUES (?, ?)")) {
                        for (Long aktivnostId : d.getAktivnostiIds()) {
                            stmt.setLong(1, d.getId());
                            stmt.setLong(2, aktivnostId);
                            stmt.executeUpdate();
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Greska pri restore baze podataka", e);
        }
    }

    private void restoreVrstePutovanja() {
        DatabaseInitializer.executeScript("sql/init_vrsta_putovanja.sql");
    }
}
