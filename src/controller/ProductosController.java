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

public class ProductosController {

	@FXML
	private TableColumn<?, ?> TcNombre;

	@FXML
	private TableColumn<?, ?> TcPrecio;

	@FXML
	private TableColumn<?, ?> TcStock;

	@FXML
	private Button btnAnadir;

	@FXML
	private Button btnBorrar;

	@FXML
	private Button btnEditar;

	@FXML
	private ImageView imgFlecha;

	@FXML
	private ImageView imgFondo;

	@FXML
	private Label lblProductos;

	@FXML
	private TableView<?> tvProductos;

	@FXML
	private TextField txtFieldNombre;

	@FXML
	private TextField txtFieldPrecio;

	@FXML
	private TextField txtFieldStock;

	@FXML
	void anadirProducto(ActionEvent event) {

	}

	@FXML
	void borrarProducto(ActionEvent event) {

	}

	@FXML
	void editarProducto(ActionEvent event) {

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
