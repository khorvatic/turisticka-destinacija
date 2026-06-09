package hr.algebra.turistika.controller;

import hr.algebra.turistika.model.Zemlja;
import hr.algebra.turistika.repository.ZemljaRepository;
import hr.algebra.turistika.repository.ZemljaRepositoryImpl;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ZemljaController extends CrudController<Zemlja> {
    @FXML private TableColumn<Zemlja, String> nazivColumn;
    @FXML private TableColumn<Zemlja, String> kodColumn;
    @FXML private TextField nazivField;
    @FXML private TextField kodField;

    private final ZemljaRepository zemljaRepository = new ZemljaRepositoryImpl();
    private Zemlja zemljaZaUredivanje;

    @Override
    protected void postaviKolone() {
        nazivColumn.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        kodColumn.setCellValueFactory(new PropertyValueFactory<>("kodDrzave"));
    }

    @Override
    protected void ucitajPodatke() {
        table.setItems(FXCollections.observableArrayList(zemljaRepository.findAll()));
    }

    @Override
    protected void postaviZaDodavanje() {
        zemljaZaUredivanje = null;
        formaNaslov.setText("Nova zemlja");
        nazivField.clear();
        kodField.clear();
        errorLabel.setText("");
    }

    @Override
    protected void postaviZaUredivanje(Zemlja zemlja) {
        zemljaZaUredivanje = zemlja;
        formaNaslov.setText("Uredi zemlju");
        nazivField.setText(zemlja.getNaziv());
        kodField.setText(zemlja.getKodDrzave());
        errorLabel.setText("");
    }

    @Override
    protected boolean validiraj() {
        if (nazivField.getText().isBlank()) {
            errorLabel.setText("Naziv je obavezan!");
            return false;
        }
        if (kodField.getText().isBlank()) {
            errorLabel.setText("Kod drzave je obavezan!");
            return false;
        }
        return true;
    }

    @Override
    protected void spremiPodatke() {
        if (zemljaZaUredivanje == null) {
            zemljaRepository.save(new Zemlja(null, nazivField.getText().trim(),
                    kodField.getText().trim().toUpperCase()));
        } else {
            zemljaZaUredivanje.setNaziv(nazivField.getText().trim());
            zemljaZaUredivanje.setKodDrzave(kodField.getText().trim().toUpperCase());
            zemljaRepository.update(zemljaZaUredivanje);
        }
    }

    @Override
    protected void izbrisi(Zemlja zemlja) {
        zemljaRepository.delete(zemlja.getId());
    }
}
