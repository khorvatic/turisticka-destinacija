package hr.algebra.turistika.controller;

import hr.algebra.turistika.model.*;
import hr.algebra.turistika.repository.DestinacijaRepository;
import hr.algebra.turistika.repository.DestinacijaRepositoryImpl;
import hr.algebra.turistika.repository.ZemljaRepository;
import hr.algebra.turistika.repository.ZemljaRepositoryImpl;
import hr.algebra.turistika.service.DestinacijaService;
import hr.algebra.turistika.util.SessionManager;
import hr.algebra.turistika.util.XmlExporter;
import hr.algebra.turistika.util.XmlLogger;
import hr.algebra.turistika.xml.DestinacijaXml;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ArrayList;
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
    @FXML private MenuItem adminPanel;

    @FXML private TextField searchField;
    @FXML private ComboBox<Zemlja> zemljaFilter;
    @FXML private ComboBox<VrstaPutovanja> vrstaPutovanjaFilter;

    @FXML private Label statusLabel;

    @FXML private DestinacijaFormaController destinacijaFormaController;
    @FXML private DestinacijaDetaljiController destinacijaDetaljiController;

    private final DestinacijaRepository destinacijaRepository = new DestinacijaRepositoryImpl();
    private final ZemljaRepository zemljaRepository = new ZemljaRepositoryImpl();
    private final DestinacijaService destinacijaService = new DestinacijaService(destinacijaRepository);

    private ObservableList<Destinacija> destinacije
            = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        postaviKolone();
        popuniFiltere();
        postaviFilterListenere();
        ucitajDestinacije();
        statusLabel.setText("Destiancija uspjesno spremljena!");

        destinacijaFormaController.postaviOnSpremi(() -> {
            ucitajDestinacije();
            statusLabel.setText("Destinacija uspjesno spremljena!");
            XmlLogger.log(
                    SessionManager.getInstance().getKorisnickoIme(),
                    "SPREMI",
                    "Spremljena destinacija"
            );
        });

        destinacijaDetaljiController.sakrijPanel();
        destinacijaDetaljiController.postaviOnSlikaUploaded(() -> ucitajDestinacije());

        boolean isAdmin = SessionManager.getInstance().getTrenutniKorisnik() instanceof Admin;
        adminPanel.setVisible(isAdmin);
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
        List<Zemlja> zemlje = new ArrayList<>();
        zemlje.add(null);
        zemlje.addAll(zemljaRepository.findAll());
        zemljaFilter.setItems(FXCollections.observableArrayList(zemlje));
        zemljaFilter.setConverter(new StringConverter<Zemlja>() {
            @Override
            public String toString(Zemlja z) {
                return z != null ? z.toString() : "Sve zemlje";
            }
            @Override
            public Zemlja fromString(String s) { return null; }
        });

        List<VrstaPutovanja> vrste = new ArrayList<>();
        vrste.add(null);
        vrste.addAll(List.of(VrstaPutovanja.values()));
        vrstaPutovanjaFilter.setItems(FXCollections.observableArrayList(vrste));
        vrstaPutovanjaFilter.setConverter(new StringConverter<>() {
            @Override
            public String toString(VrstaPutovanja v) {
                return v != null ? v.toString() : "Sve vrste putovanja";
            }
            @Override
            public VrstaPutovanja fromString(String s) { return null; }
        });
    }

    private void postaviFilterListenere() {
        searchField.textProperty().addListener(
                (observable, oldValue, newValue) -> applyFilters());
        zemljaFilter.valueProperty().addListener(
                (observable, oldValue, newValue) -> applyFilters());
        vrstaPutovanjaFilter.valueProperty().addListener(
                (observable, oldValue, newValue) -> applyFilters());
    }

    private void applyFilters() {
        Task<List<Destinacija>> task = new Task<>() {
            @Override
            protected List<Destinacija> call() {
                return destinacijaService.filter(
                        searchField.getText(),
                        zemljaFilter.getValue(),
                        vrstaPutovanjaFilter.getValue()
                );
            }
        };

        task.setOnSucceeded(event -> {
            destinacije.setAll(task.getValue());
            destinacijeTable.setItems(destinacije);
            statusLabel.setText("Pronadeno " + destinacije.size() + " destinacija");
            }
        );

        new Thread(task).start();
    }

    @FXML
    private void ucitajDestinacije() {
        statusLabel.setText("Ucitavanje destinacija...");

        Task<List<Destinacija>> task = new Task<>() {
            @Override
            protected List<Destinacija> call() {
                return destinacijaService.findAllSorted();
            }
        };

        task.setOnSucceeded(event -> {
            destinacije.setAll(task.getValue());
            destinacijeTable.setItems(destinacije);
            statusLabel.setText("Ucitano " + destinacije.size() + " destinacija");
        });

        task.setOnFailed(event -> {
            statusLabel.setText("Greska pri ucitavanju destinacija");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
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
        destinacijaDetaljiController.sakrijPanel();
        destinacijaFormaController.otvoriZaDodavanje();
        statusLabel.setText("Dodavanje nove destinacije...");
    }

    @FXML
    private void urediDestinaciju() {
        Destinacija odabranaDestinacija = destinacijeTable.getSelectionModel().getSelectedItem();
        if (odabranaDestinacija == null) {
            statusLabel.setText("Odaberite destinaciju za uređivanje!");
            return;
        }
        destinacijaDetaljiController.sakrijPanel();
        destinacijaFormaController.otvoriZaUredivanje(odabranaDestinacija);
        statusLabel.setText("Uredivanje: " + odabranaDestinacija.getNaziv());
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
                XmlLogger.log(
                        SessionManager.getInstance().getKorisnickoIme(),
                        "BRISANJE",
                        "Obrisana destinacija: " + odabrana.getNaziv()
                );
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

        destinacijaFormaController.sakrijFormu();
        destinacijaDetaljiController.prikaziDestinaciju(odabrana);
        statusLabel.setText("Prikaz detalja za: " + odabrana.getNaziv());
    }

    @FXML
    private void exportXml() {
        statusLabel.setText("Exportiranje u ML...");

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                List<DestinacijaXml> xmlDestinacije = destinacijaRepository.findAll()
                        .stream()
                        .map(DestinacijaXml::new)
                        .toList();

                String path = "exports/destinacije.xml";
                XmlExporter.export(xmlDestinacije, path);

                XmlLogger.log(
                        SessionManager.getInstance().getKorisnickoIme(),
                        "EXPORT",
                        "Exportirano " + xmlDestinacije.size() + " destinacija u XML");
                return null;
            }
        };

        task.setOnSucceeded(event -> {
            statusLabel.setText("Export zavrsen! (exports/destinacije.xml)");
        });

        task.setOnFailed(event -> {
            statusLabel.setText("Greska pri exportu u XML");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    @FXML
    private void odjava() {
        SessionManager.getInstance().postavi(null);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader()
                    .getResource("view/login.fxml"));
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Turisticka destinacija");
        } catch (Exception e) {
            throw new RuntimeException("Greska pri odjavi", e);
        }
    }

    @FXML
    private void natragNaAdmin() {
        System.out.println("1 - pocetak");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader()
                    .getResource("view/admin.fxml"));

            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Admin panel");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Greska pri povratku na admin panel", e);
        }
    }
}
