package hr.algebra.turistika.controller;

import hr.algebra.turistika.model.Aktivnost;
import hr.algebra.turistika.repository.AktivnostRepository;
import hr.algebra.turistika.repository.AktivnostRepositoryImpl;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AktivnostController extends CrudController<Aktivnost> {
    @FXML private TableColumn<Aktivnost, String> nazivColumn;
    @FXML private TableColumn<Aktivnost, String> opisColumn;
    @FXML private TextField nazivField;
    @FXML private TextArea opisField;

    private final AktivnostRepository aktivnostRepository = new AktivnostRepositoryImpl();
    private Aktivnost aktivnostZaUredivanje;

    @Override
    protected void postaviKolone() {
        nazivColumn.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        opisColumn.setCellValueFactory(new PropertyValueFactory<>("opis"));
    }

    @Override
    protected void ucitajPodatke() {
        table.setItems(FXCollections.observableArrayList(aktivnostRepository.findAll()));
    }

    @Override
    protected void postaviZaDodavanje() {
        aktivnostZaUredivanje = null;
        formaNaslov.setText("Nova aktivnost");
        nazivField.clear();
        opisField.clear();
        errorLabel.setText("");
    }

    @Override
    protected void postaviZaUredivanje(Aktivnost aktivnost) {
        aktivnostZaUredivanje = aktivnost;
        formaNaslov.setText("Uredi aktivnost");
        nazivField.setText(aktivnost.getNaziv());
        opisField.setText(aktivnost.getOpis());
        errorLabel.setText("");
    }

    @Override
    protected boolean validiraj() {
        if (nazivField.getText().isBlank()) {
            errorLabel.setText("Naziv je obavezan!");
            return false;
        }
        if (opisField.getText().isBlank()) {
            errorLabel.setText("Opis je obavezan!");
            return false;
        }
        return true;
    }

    @Override
    protected void spremiPodatke() {
        if (aktivnostZaUredivanje == null) {
            aktivnostRepository.save(new Aktivnost(nazivField.getText().trim(),
                    opisField.getText().trim()));
        } else {
            aktivnostZaUredivanje.setNaziv(nazivField.getText().trim());
            aktivnostZaUredivanje.setOpis(opisField.getText().trim());
            aktivnostRepository.update(aktivnostZaUredivanje);
        }
    }

    @Override
    protected void izbrisi(Aktivnost stavka) {
        aktivnostRepository.delete(stavka.getId());
    }
}
