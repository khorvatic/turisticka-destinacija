package hr.algebra.turistika.controller;

import hr.algebra.turistika.model.Aktivnost;
import hr.algebra.turistika.model.Destinacija;
import hr.algebra.turistika.model.VrstaPutovanja;
import hr.algebra.turistika.model.Zemlja;
import hr.algebra.turistika.repository.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DestinacijaFormaController implements Initializable {
    @FXML private VBox formaPanel;
    @FXML private Label formaNaslov;
    @FXML private TextField nazivField;
    @FXML private TextArea opisField;
    @FXML private TextField latitudeField;
    @FXML private TextField longitudeField;
    @FXML private ComboBox<String> godisnjeDobaCombo;
    @FXML private ComboBox<Zemlja> zemljaCombo;
    @FXML private ComboBox<VrstaPutovanja> vrstaPutovanjaCombo;
    @FXML private ListView<Aktivnost> dostupneAktivnosti;
    @FXML private ListView<Aktivnost> dodaneAktivnosti;
    @FXML private Label formaErrorLabel;

    private final ZemljaRepository zemljaRepository = new ZemljaRepositoryImpl();
    private final AktivnostRepository aktivnostRepository = new AktivnostRepositoryImpl();
    private final DestinacijaRepository destinacijaRepository = new DestinacijaRepositoryImpl();

    private Destinacija destinacijaZaUredivanje;
    private Runnable onSpremi;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        vrstaPutovanjaCombo.getItems().setAll(VrstaPutovanja.values());
        zemljaCombo.getItems().setAll(zemljaRepository.findAll());
        godisnjeDobaCombo.getItems().setAll(
                "Proljeće", "Ljeto", "Jesen", "Zima"
        );
        popuniAktivnosti();
        postaviDragAndDrop();
        sakrijFormu();
    }

    public void postaviOnSpremi(Runnable callback){
        this.onSpremi = callback;
    }

    public void otvoriZaDodavanje(){
        destinacijaZaUredivanje = null;
        formaNaslov.setText("Nova destinacija");
        ocistiFormu();
        prikaziFormu();
    }

    public void otvoriZaUredivanje(Destinacija destinacija){
        destinacijaZaUredivanje = destinacija;
        formaNaslov.setText("Uredi destinaciju");
        popuniFormu(destinacija);
        prikaziFormu();
    }

    @FXML
    private void spremiDestinaciju(){
        formaErrorLabel.setText("");

        if (nazivField.getText().isBlank()){
            formaErrorLabel.setText("Naziv destinacije ne smije biti prazan!");
            return;
        }
        if (opisField.getText().isBlank()){
            formaErrorLabel.setText("Opis destinacije ne smije biti prazan!");
            return;
        }
        if (zemljaCombo.getValue() == null){
            formaErrorLabel.setText("Odaberite zemlju!");
            return;
        }
        if (vrstaPutovanjaCombo.getValue() == null){
            formaErrorLabel.setText("Odaberite vrstu putovanja!");
            return;
        }

        List<Aktivnost> odabraneAktivnosti = dohvatiOdabraneAktivnosti();

        if (destinacijaZaUredivanje == null){
            Destinacija nova = new Destinacija(
                    nazivField.getText().trim(),
                    opisField.getText().trim(),
                    parseDouble(latitudeField.getText()),
                    parseDouble(longitudeField.getText()),
                    godisnjeDobaCombo.getValue(),
                    "",
                    zemljaCombo.getValue(),
                    vrstaPutovanjaCombo.getValue()
            );
            nova.setAktivnosti(odabraneAktivnosti);
            destinacijaRepository.save(nova);
        } else {
            destinacijaZaUredivanje.setNaziv(nazivField.getText().trim());
            destinacijaZaUredivanje.setOpis(opisField.getText().trim());
            destinacijaZaUredivanje.setLatitude(parseDouble(latitudeField.getText()));
            destinacijaZaUredivanje.setLongitude(parseDouble(longitudeField.getText()));
            destinacijaZaUredivanje.setPreporucenoGodisnjeDoba(godisnjeDobaCombo.getValue());
            destinacijaZaUredivanje.setZemlja(zemljaCombo.getValue());
            destinacijaZaUredivanje.setVrstaPutovanja(vrstaPutovanjaCombo.getValue());
            destinacijaZaUredivanje.setAktivnosti(odabraneAktivnosti);
            destinacijaRepository.update(destinacijaZaUredivanje);
        }

        sakrijFormu();
        if (onSpremi != null) onSpremi.run();
    }

    @FXML
    private void odustaniForma(){
        sakrijFormu();
    }

    private void prikaziFormu(){
        formaPanel.setVisible(true);
        formaPanel.setManaged(true);
    }

    private void sakrijFormu(){
        formaPanel.setVisible(false);
        formaPanel.setManaged(false);
    }

    private void ocistiFormu(){
        nazivField.clear();
        opisField.clear();
        latitudeField.clear();
        longitudeField.clear();
        godisnjeDobaCombo.setValue(null);
        zemljaCombo.setValue(null);
        vrstaPutovanjaCombo.setValue(null);
        formaErrorLabel.setText("");
        popuniAktivnosti();
    }

    private void popuniFormu(Destinacija d){
        nazivField.setText(d.getNaziv());
        opisField.setText(d.getOpis());
        latitudeField.setText(d.getLatitude() != null ? d.getLatitude().toString() : "");
        longitudeField.setText(d.getLongitude() != null ? d.getLongitude().toString() : "");
        godisnjeDobaCombo.setValue(d.getPreporucenoGodisnjeDoba());
        zemljaCombo.setValue(d.getZemlja());
        vrstaPutovanjaCombo.setValue(d.getVrstaPutovanja());
        formaErrorLabel.setText("");
        popuniAktivnosti();
        dodaneAktivnosti.getItems().setAll(d.getAktivnosti());
        dostupneAktivnosti.getItems().removeAll(dodaneAktivnosti.getItems());
    }

    private void popuniAktivnosti(){
        dostupneAktivnosti.getItems().setAll(aktivnostRepository.findAll());
        dodaneAktivnosti.getItems().clear();
    }

    private List<Aktivnost> dohvatiOdabraneAktivnosti(){
        return new ArrayList<>(dodaneAktivnosti.getItems());
    }

    private void postaviDragAndDrop() {
        postaviDragIzvor(dostupneAktivnosti, dodaneAktivnosti);
        postaviDragIzvor(dodaneAktivnosti, dostupneAktivnosti);
    }

    private void postaviDragIzvor(ListView<Aktivnost> izvor, ListView<Aktivnost> cilj) {
        izvor.setOnDragDetected(event -> {
            Aktivnost odabrana = izvor.getSelectionModel().getSelectedItem();
            if (odabrana == null) return;

            Dragboard db = izvor.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(odabrana.getNaziv());
            db.setContent(content);
            event.consume();
        });

        cilj.setOnDragOver(event -> {
            if (event.getGestureSource() != cilj && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        cilj.setOnDragDropped(event -> {
            String naziv = event.getDragboard().getString();
            Aktivnost aktivnost = izvor.getItems().stream()
                    .filter(a -> a.getNaziv().equals(naziv))
                    .findFirst()
                    .orElse(null);

            if (aktivnost != null) {
                izvor.getItems().remove(aktivnost);
                cilj.getItems().add(aktivnost);
                event.setDropCompleted(true);
            } else event.setDropCompleted(false);

            event.consume();
        });
    }

    private Double parseDouble(String text){
        if (text.isBlank()) return null;
        try {
            return Double.parseDouble(text.trim().replace(',', '.'));
        } catch (NumberFormatException e){
            return null;
        }
    }
}
