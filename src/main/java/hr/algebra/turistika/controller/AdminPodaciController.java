package hr.algebra.turistika.controller;

import hr.algebra.turistika.util.DatabaseInitializer;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

public class AdminPodaciController {
    @FXML private Label statusLabel;

    @FXML
    private void obrisiSvePodatke() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Ovo ce obrisati sve podatke iz baze. Jeste li sigurni?",
                ButtonType.YES, ButtonType.NO);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                DatabaseInitializer.executeScript("sql/clear_tables.sql");
                DatabaseInitializer.createDefaultAdmin();
                statusLabel.setText("Svi podaci su obrisani!");
            }
        });
    }

    @FXML
    private void ucitajTestPodatke(){
        DatabaseInitializer.insertTestData();
        statusLabel.setText("Test podaci ucitani");
    }
}
