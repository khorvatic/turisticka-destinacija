package hr.algebra.turistika;

import hr.algebra.turistika.util.AppConfig;
import hr.algebra.turistika.util.DatabaseInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        DatabaseInitializer.initialize();

        AppConfig config = AppConfig.getInstance();

        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("view/login.fxml"));
        Scene scene = new Scene(loader.load());

        stage.setTitle(config.getWindowTitle());
        stage.setWidth(config.getWindowWidth());
        stage.setHeight(config.getWindowHeight());
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
