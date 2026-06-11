package hr.algebra.turistika.controller;

import hr.algebra.turistika.model.Admin;
import hr.algebra.turistika.model.Destinacija;
import hr.algebra.turistika.repository.DestinacijaRepository;
import hr.algebra.turistika.repository.DestinacijaRepositoryImpl;
import hr.algebra.turistika.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DestinacijaDetaljiController {
    @FXML private VBox detaljiPanel;
    @FXML private Label nazivLabel;
    @FXML private Label drzavaLabel;
    @FXML private ImageView slikaView;
    @FXML private Label opisLabel;
    @FXML private Label vrstaPutovanjaLabel;
    @FXML private Label godisnjeDobaLabel;
    @FXML private Label koordinateLabel;
    @FXML private Label aktivnostiLabel;
    @FXML private Label vodiciLabel;
    @FXML private Button uploadSlikuButton;

    private Destinacija trenutnaDestinacija;
    private Runnable onSlikaUploaded;

    private final DestinacijaRepository destinacijaRepository = new DestinacijaRepositoryImpl();

    public void postaviOnSlikaUploaded(Runnable callback){
        this.onSlikaUploaded = callback;
    }

    public void prikaziDestinaciju(Destinacija destinacija) {
        trenutnaDestinacija = destinacija;

        nazivLabel.setText(destinacija.getNaziv());
        drzavaLabel.setText(destinacija.getZemlja() != null
                ? destinacija.getZemlja().getNaziv() : "");
        opisLabel.setText(destinacija.getOpis());
        vrstaPutovanjaLabel.setText(destinacija.getVrstaPutovanja().toString());
        godisnjeDobaLabel.setText(destinacija.getPreporucenoGodisnjeDoba() != null
                ? destinacija.getPreporucenoGodisnjeDoba() : "");
        koordinateLabel.setText(destinacija.getLatitude() + ", " + destinacija.getLongitude());
        aktivnostiLabel.setText(destinacija.getAktivnosti().isEmpty()
            ? "Nema aktivnosti" : destinacija.getAktivnosti().stream()
                .map(a -> "• " + a.getNaziv())
                .reduce("", (a, b) -> a + "\n" + b).strip());
        vodiciLabel.setText(destinacija.getTuristickiVodici().isEmpty()
            ? "Nema vodica" : destinacija.getTuristickiVodici().stream()
                .map(v -> "• " + v.getFullName())
                .reduce("", (a, b) -> a + "\n" + b).strip());

        ucitajSliku(destinacija.getPutanjaFotografija());

        boolean isAdmin = SessionManager.getInstance().getTrenutniKorisnik() instanceof Admin;
        uploadSlikuButton.setVisible(isAdmin);
        uploadSlikuButton.setManaged(isAdmin);

        prikaziPanel();
    }

    private void ucitajSliku(String putanja){
        if (putanja != null && !putanja.isBlank()){
            File file = new File(putanja);
            if (file.exists()){
                slikaView.setImage(new Image(file.toURI().toString()));
                return;
            }
        }
        slikaView.setImage(null);
    }

    @FXML
    private void uploadSliku() {
        FileChooser fIleChooser = new FileChooser();
        fIleChooser.setTitle("Odaberi sliku");
        fIleChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Slike", "*.jpg", "*.png", "*.jpeg"));

        Stage stage = (Stage) detaljiPanel.getScene().getWindow();
        File odabranaSlika = fIleChooser.showOpenDialog(stage);

        if (odabranaSlika != null){
            try {
                Path destFolder = Paths.get("assets/images");
                Files.createDirectories(destFolder);

                String fileName = trenutnaDestinacija.getId() + "_" + odabranaSlika.getName();
                Path destination = destFolder.resolve(fileName);

                Files.copy(odabranaSlika.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

                String relativePath = destination.toString();
                trenutnaDestinacija.setPutanjaFotografija(relativePath);
                destinacijaRepository.update(trenutnaDestinacija);

                ucitajSliku(relativePath);
                if (onSlikaUploaded != null) onSlikaUploaded.run();
            } catch (IOException e) {
                throw new RuntimeException("Greska pri uploadu slike", e);
            }
        }
    }

    public void prikaziPanel(){
        detaljiPanel.setVisible(true);
        detaljiPanel.setManaged(true);
    }

    public void sakrijPanel(){
        detaljiPanel.setVisible(false);
        detaljiPanel.setManaged(false);
    }
}
