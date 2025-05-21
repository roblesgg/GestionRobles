package controller;

import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class HistorialController {

    @FXML
    private Button btnBorrarTodo;

    @FXML
    private ImageView imgFlecha;

    @FXML
    private ImageView imgFondo;

    @FXML
    private Label lblCambio;

    @FXML
    private Label lblProductos;

    @FXML
    private Label lblTotal;

    @FXML
    private TableColumn<?, ?> tcApellidoHistorial;

    @FXML
    private TableColumn<?, ?> tcCantidadVendido;

    @FXML
    private TableColumn<?, ?> tcDescripcionHistorial;

    @FXML
    private TableColumn<?, ?> tcHoraHistorial;

    @FXML
    private TableColumn<?, ?> tcNombreHistorial;

    @FXML
    private TableColumn<?, ?> tcPrecioVendido;

    @FXML
    private TableColumn<?, ?> tcProductoVendido;

    @FXML
    private TableView<?> tvHistorial;

    @FXML
    private TableView<?> tvVendido;
//-------------------------------------------------------------
    
    
    @FXML
    void borrarTodosHistorial(ActionEvent event) {
        // Crear una alerta de confirmación
        Alert alertaConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        alertaConfirmacion.setTitle("Confirmar borrado");
        alertaConfirmacion.setHeaderText("¿Estás seguro de que deseas borrar todo el historial de ventas?");
        alertaConfirmacion.setContentText("Esta acción no se puede deshacer.");

        // Mostrar la alerta y esperar la respuesta del usuario
        Optional<ButtonType> resultado = alertaConfirmacion.showAndWait();

        // Si el usuario pulsa en ACEPTAR, se procede con el borrado
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                int ventasEliminadas = database.VentaDatabase.borrarTodasVentasConDetalles();

                Alert exito = new Alert(Alert.AlertType.INFORMATION);
                exito.setTitle("Ventas eliminadas");
                exito.setHeaderText(null);
                exito.setContentText("Se eliminaron " + ventasEliminadas + " ventas del historial.");
                exito.showAndWait();

                // Si tienes métodos para actualizar las tablas, los puedes llamar aquí:
                // cargarHistorial();

            } catch (Exception e) {
                e.printStackTrace();
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText("No se pudo borrar el historial.");
                error.setContentText("Ocurrió un error al intentar borrar las ventas.");
                error.showAndWait();
            }
        } else {
            // El usuario canceló la operación
            System.out.println("El usuario canceló el borrado del historial.");
        }
    }



    @FXML
    void restarDelCarrito(MouseEvent event) {

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
