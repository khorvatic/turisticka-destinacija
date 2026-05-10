package hr.algebra.turistika.controller;

import hr.algebra.turistika.model.Admin;
import hr.algebra.turistika.model.Korisnik;
import hr.algebra.turistika.model.User;
import hr.algebra.turistika.repository.UserRepository;
import hr.algebra.turistika.repository.UserRepositoryImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Optional;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    private final UserRepository userRepository = new UserRepositoryImpl();

    @FXML
    private void handleLogin() {
        String username = this.usernameField.getText().trim();
        String password = this.passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Unesite korisničko ime i lozinku!");
            return;
        }

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty() || !user.get().getPassword().equals(password)) {
            errorLabel.setText("Pogrešno korisničko ime ili lozinka!");
            return;
        }

        if (user.get() instanceof Admin){
            openWindow("view/admin.fxml", "Admin panel");
        } else {
            openWindow("view/main.fxml", "Turistička destinacija");
        }
    }

    @FXML
    private void handleRegistration() {
        String username = this.usernameField.getText().trim();
        String password = this.passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Unesite korisničko ime i lozinku!");
            return;
        }

        if (userRepository.findByUsername(username).isPresent()) {
            errorLabel.setText("Korisničko ime već postoji!");
            return;
        }

        userRepository.save(new Korisnik(username, password));
        errorLabel.setStyle("-fx-text-fill: green;");
        errorLabel.setText("Registracija uspješna!");
    }

    private void openWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlPath));
            Stage stage = (Stage) this.usernameField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(title);
        } catch (IOException e) {
            throw new RuntimeException("Greška pri otvaranju prozora", e);
        }
    }
}
