package hr.algebra.turistika.service;

import hr.algebra.turistika.model.*;
import hr.algebra.turistika.repository.*;
import hr.algebra.turistika.util.DatabaseInitializer;
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

            // Obrise postojece podatke
            DatabaseInitializer.executeScript("sql/clear_tables.sql");
            DatabaseInitializer.createDefaultAdmin();

            restoreVrstePutovanja();
            backup.getZemlje().forEach(z ->
                    zemljaRepository.save(new Zemlja(z.getId(), z.getNaziv(), z.getKodDrzave())));
            backup.getAktivnosti().forEach(a ->
                    aktivnostRepository.save(new Aktivnost(a.getId(), a.getNaziv(), a.getOpis())));
            backup.getVodici().forEach(v ->
                    turistickiVodicRepository.save(new TuristickiVodic(v.getId(), v.getIme(), v.getPrezime(), v.getKontakt())));
            backup.getDestinacije().forEach(d -> {
                Zemlja zemlja = zemljaRepository.findById(d.getZemljaId()).orElse(null);
                VrstaPutovanja vrsta = VrstaPutovanja.valueOf(d.getVrstaPutovanja());
                Destinacija dest = new Destinacija(d.getId(), d.getNaziv(), d.getOpis(),
                        d.getLatitude(), d.getLongitude(), d.getPreprucenoGodisnjeDoba(),
                        "", zemlja, vrsta);

                if (d.getAktivnostiIds() != null) {
                    d.getAktivnostiIds().forEach(id ->
                        aktivnostRepository.findById(id).ifPresent(dest::addAktivnost));
                }
                destinacijaRepository.save(dest);
            });
        } catch (Exception e) {
            throw new RuntimeException("Greska pri restore baze podataka", e);
        }
    }

    private void restoreVrstePutovanja() {
        DatabaseInitializer.executeScript("sql/init_vrsta_putovanja.sql");
    }
}
