package hr.algebra.turistika.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class CrudController<T> implements Initializable {
    @FXML protected TableView<T> table;
    @FXML protected VBox formaPanel;
    @FXML protected Label formaNaslov;
    @FXML protected Label errorLabel;
    @FXML protected Label statusLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        postaviKolone();
        sakrijFormu();
        ucitajPodatke();
    }

    @FXML
    protected void otvoriDodavanje() {
        postaviZaDodavanje();
        prikaziFormu();
    }

    @FXML
    protected void otvoriUredivanje() {
        T odabran = table.getSelectionModel().getSelectedItem();
        if(odabran == null){
            statusLabel.setText("Odaberite  za urediavnje!");
            return;
        }
        postaviZaUredivanje(odabran);
        prikaziFormu();
    }

    @FXML
    protected void obrisi(){
        T odabran = table.getSelectionModel().getSelectedItem();
        if (odabran == null) {
            statusLabel.setText("Odaberite stavku za brisanje!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Jeste li sigurni da zelite obrisati?",
                ButtonType.YES, ButtonType.NO);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                izbrisi(odabran);
                ucitajPodatke();
                statusLabel.setText("Stavka obrisana!");
                sakrijFormu();
            }
        });
    }

    @FXML
    protected void spremi() {
        errorLabel.setText("");
        if(!validiraj()) return;
        spremiPodatke();
        ucitajPodatke();
        sakrijFormu();
        statusLabel.setText("Uspjesno spremljeno!");
    }

    @FXML
    protected void odustani() {
        sakrijFormu();
    }

    protected void prikaziFormu() {
        formaPanel.setVisible(true);
        formaPanel.setManaged(true);
    }

    protected void sakrijFormu() {
        formaPanel.setVisible(false);
        formaPanel.setManaged(false);
    }

    protected abstract void postaviKolone();
    protected abstract void ucitajPodatke();
    protected abstract void postaviZaDodavanje();
    protected abstract void postaviZaUredivanje(T stavka);
    protected abstract boolean validiraj();
    protected abstract void spremiPodatke();
    protected abstract void izbrisi(T stavka);
}
