package hr.algebra.turistika.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {
    @FXML private Label statusLabel;
    @FXML private BorderPane adminRoot;

    @Override
    public void initialize(URL ul, ResourceBundle resourceBundle) {
        System.out.println("initialize - adminRoot: " + adminRoot);
        prikaziPodatke();
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

    @FXML
    private void prikaziPodatke() {
        ucitajPanel("view/admin-podaci.fxml");
    }

    @FXML
    private void prikaziZemlje() {
        ucitajPanel("view/zemlja.fxml");
    }

    @FXML
    private void prikaziAktivnosti() {
        ucitajPanel("view/aktivnost.fxml");
    }

    private void ucitajPanel(String fxmlPath) {
        System.out.println("ucitajPanel - adminRoot: " + adminRoot);
        System.out.println("ucitajPanel - top: " + adminRoot.getTop());
        System.out.println("ucitajPanel - children before: " + adminRoot.getCenter());
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getClassLoader().getResource(fxmlPath));
            adminRoot.setCenter(loader.load());
            System.out.println("ucitajPanel - top after: " + adminRoot.getTop());
        } catch (Exception e) {
            throw new RuntimeException("Greska pri otvaranju panela", e);
        }
    }
}
