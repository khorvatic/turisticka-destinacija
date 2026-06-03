package hr.algebra.turistika.controller;

import hr.algebra.turistika.model.Aktivnost;
import hr.algebra.turistika.model.Destinacija;
import hr.algebra.turistika.model.VrstaPutovanja;
import hr.algebra.turistika.model.Zemlja;
import hr.algebra.turistika.repository.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
    @FXML private VBox aktivnostiContainer;
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
        aktivnostiContainer.getChildren().forEach(node -> {
            if (node instanceof CheckBox cb) cb.setSelected(false);
        });
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
        aktivnostiContainer.getChildren().forEach(node -> {
            if (node instanceof CheckBox cb) {
                Aktivnost a = (Aktivnost) cb.getUserData();
                cb.setSelected(d.getAktivnosti().contains(a));
            }
        });
    }

    private void popuniAktivnosti(){
        aktivnostiContainer.getChildren().clear();
        for (Aktivnost a : aktivnostRepository.findAll()){
            CheckBox cb = new CheckBox(a.getNaziv());
            cb.setUserData(a);
            aktivnostiContainer.getChildren().add(cb);
        }
    }

    private List<Aktivnost> dohvatiOdabraneAktivnosti(){
        List<Aktivnost> odabrane = new ArrayList<>();
        aktivnostiContainer.getChildren().forEach(node -> {
            if (node instanceof CheckBox cb && cb.isSelected()) {
                odabrane.add((Aktivnost) cb.getUserData());
            }
        });
        return odabrane;
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
