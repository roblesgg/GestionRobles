package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MenuController {

    @FXML
    private ImageView imgFlecha;

    @FXML
    private ImageView imgFondo;

    //Volver al login
    @FXML
    void volverLogin(MouseEvent event) {
        try {
            // Carga la vista Menu.fxml
            Parent menuRoot = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));

            // Obtén la ventana actual a partir del botón
            Stage stage = (Stage) imgFlecha.getScene().getWindow();

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

}
