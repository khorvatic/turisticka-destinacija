package hr.algebra.turistika.controller;

import hr.algebra.turistika.service.BackupService;
import hr.algebra.turistika.util.DatabaseInitializer;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

public class AdminPodaciController {
    @FXML private Label statusLabel;

    private final BackupService backupService = new BackupService();

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
    private void backup(){
        backupService.backup();
        statusLabel.setText("Backup kreiran: backup/backup.xml");
    }

    @FXML
    private void restoreBackup() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Ovo ce zamijeniti sve trenutne podatke s backupom. Nastaviti?",
                ButtonType.YES, ButtonType.NO);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                backupService.restoreBackup();
                statusLabel.setText("Restore zavrsen!");
            }
        });
    }
}
