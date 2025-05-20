package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carga el archivo FXML (ajusta el nombre al que vayas a usar)
        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setTitle("Gestión Robles");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
