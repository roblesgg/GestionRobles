package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private Button btnEntrar;

    @FXML
    private ImageView imgCruz;

    @FXML
    private ImageView imgFondo;

    @FXML
    private ImageView imgLogo;

    //Abrir menu
    @FXML
    void abrirMenu(ActionEvent event) {
        try {
            // Carga la vista Menu.fxml
            Parent menuRoot = FXMLLoader.load(getClass().getResource("/view/Menu.fxml"));

            // Obtén la ventana actual a partir del botón
            Stage stage = (Stage) btnEntrar.getScene().getWindow();

            // Crea una nueva escena con la vista Menu
            Scene scene = new Scene(menuRoot);

            // Cambia la escena actual por la nueva
            stage.setScene(scene);
            stage.setTitle("Gestión Robles");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Salir de programa
    @FXML
    void salir(MouseEvent event) {
    	Stage stage=(Stage) imgCruz.getScene().getWindow();
    	stage.close();
    }

}
