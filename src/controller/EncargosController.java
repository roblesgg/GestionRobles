package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class EncargosController {

    @FXML
    private Button btnBorrarTodo;

    @FXML
    private Button btnVender;

    @FXML
    private ImageView imgFlecha;

    @FXML
    private ImageView imgFondo;

    @FXML
    private Label lblCambio;

    @FXML
    private Label lblCambioText;

    @FXML
    private Label lblEfectivo;

    @FXML
    private Label lblProductos;

    @FXML
    private Label lblTotal;

    @FXML
    private Label lblTotalText;

    @FXML
    private TableColumn<?, ?> tcCantidadEncargo;

    @FXML
    private TableColumn<?, ?> tcEncargos;

    @FXML
    private TableColumn<?, ?> tcPrecioEncargo;

    @FXML
    private TableColumn<?, ?> tcProductoEncargo;

    @FXML
    private TableView<?> tvEncargo;

    @FXML
    private TableView<?> tvEncargos;

    @FXML
    private TextField txtFieldEfectivo;

    @FXML
    void borrarTodosEncargos(ActionEvent event) {

    }

    @FXML
    void restarDelCarrito(MouseEvent event) {

    }

    @FXML
    void venderCarrito(ActionEvent event) {

    }

	// Volver a menu
	@FXML
	void volverMenu(MouseEvent event) {
		try {
			// Carga la vista Menu.fxml
			Parent menuRoot = FXMLLoader.load(getClass().getResource("/view/Menu.fxml"));

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
