package hr.algebra.turistika.controller;

import hr.algebra.turistika.model.Aktivnost;
import hr.algebra.turistika.model.Destinacija;
import hr.algebra.turistika.model.VrstaPutovanja;
import hr.algebra.turistika.model.Zemlja;
import hr.algebra.turistika.repository.DestinacijaRepository;
import hr.algebra.turistika.repository.DestinacijaRepositoryImpl;
import hr.algebra.turistika.repository.ZemljaRepository;
import hr.algebra.turistika.repository.ZemljaRepositoryImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainController implements Initializable {
    @FXML private TableView<Destinacija> destinacijeTable;
    @FXML private TableColumn<Destinacija, String> nazivColumn;
    @FXML private TableColumn<Destinacija, String> zemljaColumn;
    @FXML private TableColumn<Destinacija, String> vrstaPutovanjaColumn;
    @FXML private TableColumn<Destinacija, String> godisnjeDobaColumn;
    @FXML private TableColumn<Destinacija, String> aktivnostiColumn;

    @FXML private TextField searchField;
    @FXML private ComboBox<Zemlja> zemljaFilter;
    @FXML private ComboBox<VrstaPutovanja> vrstaPutovanjaFilter;

    @FXML private Label statusLabel;

    private final DestinacijaRepository destinacijaRepository = new DestinacijaRepositoryImpl();
    private final ZemljaRepository zemljaRepository = new ZemljaRepositoryImpl();

    private ObservableList<Destinacija> destinacije
            = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        postaviKolone();
        popuniFiltere();
        ucitajDestinacije();
    }

    private void postaviKolone(){
        nazivColumn.setCellValueFactory(
                new PropertyValueFactory<>("naziv"));
        zemljaColumn.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getZemlja() != null
                        ? data.getValue().getZemlja().getNaziv()
                        : ""));
        vrstaPutovanjaColumn.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getVrstaPutovanja().toString()));
        godisnjeDobaColumn.setCellValueFactory(
                new PropertyValueFactory<>("preporucenoGodisnjeDoba"));
        aktivnostiColumn.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getAktivnosti().stream()
                                .map(Aktivnost::getNaziv)
                                .collect(Collectors.joining(", "))));
    }

    private void popuniFiltere() {
        List<Zemlja> zemlje = zemljaRepository.findAll();
        zemljaFilter.setItems(FXCollections.observableArrayList(zemlje));

        vrstaPutovanjaFilter.setItems(FXCollections.observableArrayList(VrstaPutovanja.values()));
    }

    @FXML
    private void ucitajDestinacije() {
        destinacije.setAll(destinacijaRepository.findAll());
        destinacijeTable.setItems(destinacije);
        statusLabel.setText("Učitano " + destinacije.size() + " destinacija");
    }

    @FXML
    private void pretraziDestinacije() {
        String pojam = searchField.getText().trim().toLowerCase();
        List<Destinacija> filtrirane = destinacijaRepository.findAll().stream()
                .filter(d -> d.getNaziv().toLowerCase().contains(pojam))
                .collect(Collectors.toList());

        destinacijeTable.setItems(FXCollections.observableArrayList(filtrirane));
        statusLabel.setText("Pronađeno " + filtrirane.size() + " destinacija");
    }

    @FXML
    private void filtrirajPoZemlji() {
        Zemlja odabranaZemlja = zemljaFilter.getValue();
        if (odabranaZemlja == null) return;

        List<Destinacija> filtrirane = destinacijaRepository.findByZemlja(odabranaZemlja.getId());
        destinacijeTable.setItems(FXCollections.observableArrayList(filtrirane));
        statusLabel.setText("Filtrirano po zemlji: " + odabranaZemlja.getNaziv());
    }

    @FXML
    private void filtrirajPoVrsti() {
        VrstaPutovanja odabranaVrsta = vrstaPutovanjaFilter.getValue();
        if (odabranaVrsta == null) return;

        List<Destinacija> filtrirane = destinacijaRepository.findByVrstaPutovanja(odabranaVrsta);
        destinacijeTable.setItems(FXCollections.observableArrayList(filtrirane));
        statusLabel.setText("Filtrirano po vrsti putovanja: " + odabranaVrsta);
    }

    @FXML
    private void resetirajFiltere() {
        searchField.clear();
        zemljaFilter.setValue(null);
        vrstaPutovanjaFilter.setValue(null);
        ucitajDestinacije();
    }

    @FXML
    private void otvoriDodajDestinaciju() {
        // TODO: Implementiraj otvori dodaj destinaciju
        statusLabel.setText("Dodavanje destinacije - u izradi");
    }

    @FXML
    private void urediDestinaciju() {
        Destinacija odabranaDestinacija = destinacijeTable.getSelectionModel().getSelectedItem();
        if (odabranaDestinacija == null) {
            statusLabel.setText("Odaberite destinaciju za uređivanje!");
            return;
        }
        // TODO - Implementiraj uredivanje destinacije
        statusLabel.setText("Uređivanje destinacije - u izradi");
    }

    @FXML
    private void obrisiDestinaciju() {
        Destinacija odabrana = destinacijeTable.getSelectionModel().getSelectedItem();
        if (odabrana == null) {
            statusLabel.setText("Odaberite destinaciju za brisanje!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Obrisati destinaciju: " + odabrana.getNaziv() + "?",
                ButtonType.YES, ButtonType.NO);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                destinacijaRepository.delete(odabrana.getId());
                ucitajDestinacije();
                statusLabel.setText("Destinacija obrisana!");
            }
        });
    }

    @FXML
    private void prikaziDetalje() {
        Destinacija odabrana = destinacijeTable.getSelectionModel().getSelectedItem();
        if (odabrana == null){
            statusLabel.setText("Odaberite destinaciju!");
            return;
        }
        // TODO - Implementiraj prikaz detalja destinacije
        statusLabel.setText("Detalji destinacije - u izradi");
    }

    @FXML
    private void exportXml() {
        // TODO - Implementiraj export u XML
        statusLabel.setText("Export u XML - u izradi");
    }
}
