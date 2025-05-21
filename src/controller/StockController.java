package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class StockController {

	@FXML
    private TableColumn<?, ?> TcDescripcion;

    @FXML
    private ImageView imgFlecha;

    @FXML
    private ImageView imgFondo;

    @FXML
    private ImageView imgReiniciar;

    @FXML
    private ImageView imgSuma;

    @FXML
    private Label lblProductos;

    @FXML
    private TableColumn<?, ?> tcProducto;

    @FXML
    private TableColumn<?, ?> tcStock;

    @FXML
    private TableColumn<?, ?> tcVendido;

    @FXML
    private TableView<?> tvProductos;

    @FXML
    private TextField txtFieldPrecio;
//-------------------------------------------------------
    
    @FXML
    void initialize() {
    	cargarStock();
    }
    
    public void cargarStock() {
    	
    }
    
    @FXML
    void añadirCantidad(MouseEvent event) {

    }
    
    @FXML
    void reiniciarVendido(MouseEvent event) {

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
