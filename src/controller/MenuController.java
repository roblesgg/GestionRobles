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

public class MenuController {

    @FXML
    private Button btnEncargos;

    @FXML
    private Button btnHistorial;

    @FXML
    private Button btnProductos;

    @FXML
    private Button btnReiniciarTodo;

    @FXML
    private Button btnStock;

    @FXML
    private Button btnVentaDirecta;

    @FXML
    private ImageView imgFlecha;

    @FXML
    private ImageView imgFondo;
//----------------------------------------------------
    
    @FXML
    void abrirEncargos(ActionEvent event) {

    }

    @FXML
    void abrirHistorial(ActionEvent event) {

    }

    @FXML
    void abrirProductos(ActionEvent event) {
        try {
            // Carga la vista Menu.fxml
            Parent menuRoot = FXMLLoader.load(getClass().getResource("/view/Productos.fxml"));

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

    //Abrir stock
    @FXML
    void abrirStock(ActionEvent event) {
        try {
            // Carga la vista Menu.fxml
            Parent menuRoot = FXMLLoader.load(getClass().getResource("/view/Stock.fxml"));

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

    //Abrir venta directa
    @FXML
    void abrirVentaDirecta(ActionEvent event) {
        try {
            // Carga la vista Menu.fxml
            Parent menuRoot = FXMLLoader.load(getClass().getResource("/view/VentaDirecta.fxml"));

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

    @FXML
    void reiniciarTodo(ActionEvent event) {

    }
    
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
