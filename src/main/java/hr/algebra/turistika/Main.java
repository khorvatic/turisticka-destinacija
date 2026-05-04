package hr.algebra.turistika;

import hr.algebra.turistika.util.DatabaseInitializer;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        DatabaseInitializer.initialize();

        stage.setTitle("Turistička agencija");
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
