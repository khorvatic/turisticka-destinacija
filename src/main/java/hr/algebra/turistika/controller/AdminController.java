package hr.algebra.turistika.controller;

import hr.algebra.turistika.util.DatabaseInitializer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AdminController {
    @FXML
    private Label statusLabel;

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

    @FXML
    private void otvoriAplikaciju() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getClassLoader().getResource("view/main.fxml"));
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Turisticka destinacija");
        } catch (Exception e) {
            throw new RuntimeException("Greska pri otvaranju aplikacije", e);
        }
    }
}
