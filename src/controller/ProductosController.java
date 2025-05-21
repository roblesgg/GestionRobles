package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import database.ConexionBD;
import database.ProductoDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Producto;
import util.Alertas;

public class ProductosController {

	@FXML
	private TableColumn<Producto, String> tcNombre;

	@FXML
	private TableColumn<Producto, Double> tcPrecio;

	@FXML
	private TableColumn<Producto, Integer> tcStock;

	@FXML
	private TableColumn<Producto, String> tcDescripcion;

	@FXML
	private Button btnAgregar;

	@FXML
	private Button btnBorrar;

	@FXML
	private Button btnEditar;

	@FXML
	private Button btnBorrarTodo;

	@FXML
	private ImageView imgFlecha;

	@FXML
	private ImageView imgFondo;

	@FXML
	private Label lblProductos;

	@FXML
	private TableView<Producto> tvProductos;

	@FXML
	private TextField txtFieldNombre;

	@FXML
	private TextField txtFieldPrecio;

	@FXML
	private TextField txtFieldStock;

	@FXML
	private TextField txtFieldDescripcion;
//----------------------------------------------------------------------

	// Inicializate
	@FXML
	public void initialize() {
		cargarProductos();
		// Esto hace que se completen los field con el producto que se selecccione
		tvProductos.setOnMouseClicked(event -> {
			Producto seleccionado = tvProductos.getSelectionModel().getSelectedItem();
			if (seleccionado != null) {
				txtFieldNombre.setText(seleccionado.getNombre());
				txtFieldPrecio.setText(String.valueOf(seleccionado.getPrecio()));
				txtFieldStock.setText(String.valueOf(seleccionado.getStock()));
				txtFieldDescripcion.setText(seleccionado.getDescripcion());
			}
		});
	}

	// cargar productos
	public void cargarProductos() {
		// Configuramos las columnas para que muestren las propiedades correctas de
		// Producto
		tcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		tcPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
		tcStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
		tcDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

		// Obtenemos la lista de productos de la base de datos
		List<Producto> productos = ProductoDatabase.obtenerProductos();

		// Convertimos la lista a ObservableList para que la tabla pueda trabajar con
		// ella
		ObservableList<Producto> listaProductos = FXCollections.observableArrayList(productos);

		// Cargamos los productos en el TableView
		tvProductos.setItems(listaProductos);
	}

	// Agregar producto
	@FXML
	void agregarProducto(ActionEvent event) {
		try {
			String nombre = txtFieldNombre.getText();
			double precio = Double.parseDouble(txtFieldPrecio.getText());
			int stock = Integer.parseInt(txtFieldStock.getText());
			String descripcion = txtFieldDescripcion.getText();

			Producto nuevoProducto = new Producto(nombre, precio, stock, descripcion);

			boolean exito = ProductoDatabase.insertarProducto(nuevoProducto);

			if (exito) {
				// Opcional: limpiar campos
				txtFieldNombre.clear();
				txtFieldPrecio.clear();
				txtFieldStock.clear();
				txtFieldDescripcion.clear();

				// Recargar productos en la tabla para mostrar el nuevo
				cargarProductos();

				System.out.println("Producto añadido correctamente.");
			} else {
				System.out.println("Error al añadir el producto.");
			}
		} catch (NumberFormatException e) {
			System.out.println("Por favor, ingresa valores numéricos válidos para precio y stock.");
		}
	}

	@FXML
	void borrarProducto(ActionEvent event) {
		// Obtener el producto seleccionado en la tabla
		Producto productoSeleccionado = tvProductos.getSelectionModel().getSelectedItem();

		// Comprobar si hay un producto seleccionado
		if (productoSeleccionado != null) {
			// Crear un cuadro de diálogo para confirmar la eliminación
			Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
			confirmacion.setTitle("Confirmar eliminación"); // Título del diálogo
			confirmacion.setHeaderText("¿Estás seguro de que deseas borrar este producto?"); // Pregunta al usuario
			confirmacion.setContentText("Producto: " + productoSeleccionado.getNombre()); // Mostrar nombre del producto

			// Mostrar la alerta y esperar la respuesta del usuario
			Optional<ButtonType> resultado = confirmacion.showAndWait();

			// Si el usuario presiona OK para confirmar
			if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
				// Intentar borrar el producto usando el método de ProductoDatabase
				boolean exito = ProductoDatabase.borrarProducto(productoSeleccionado.getIdProducto());

				if (exito) {
					// Si se borró correctamente, eliminar el producto del TableView
					tvProductos.getItems().remove(productoSeleccionado);

					// Mostrar mensaje de éxito
					Alertas.mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Producto eliminado correctamente.");
				} else {
					// Si no se pudo borrar (por ejemplo, porque tiene ventas), mostrar advertencia
					Alertas.mostrarAlerta(Alert.AlertType.WARNING, "No se pudo borrar",
							"El producto tiene ventas asociadas y no puede ser eliminado.");
				}
			}

		} else {
			// Si no se ha seleccionado ningún producto, mostrar advertencia
			Alertas.mostrarAlerta(Alert.AlertType.WARNING, "Sin selección", "Selecciona un producto para borrar.");
		}
	}

	// Borrar todos los productos
	@FXML
	void borrarTodosProductos(ActionEvent event) {
	    // Crear un cuadro de diálogo para confirmar la eliminación
	    Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
	    confirmacion.setTitle("Confirmar eliminación masiva");
	    confirmacion.setHeaderText("¿Estás seguro de que deseas borrar TODOS los productos?");

	    Optional<ButtonType> resultado = confirmacion.showAndWait();

	    // Si el usuario confirma
	    if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
	        try {
	            // Llamar al método que borra todos los productos respetando restricciones
	            int cantidadBorrados = ProductoDatabase.borrarTodosProductos();

	            if (cantidadBorrados > 0) {
	                // Recargar productos en la tabla para mostrar los cambios
	                cargarProductos();

	                Alertas.mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito",
	                        "Se eliminaron " + cantidadBorrados + " productos correctamente.");
	            } else {
	                Alertas.mostrarAlerta(Alert.AlertType.WARNING, "Sin cambios",
	                        "No se pudo eliminar ningún producto (quizás todos tienen ventas asociadas).");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            Alertas.mostrarAlerta(Alert.AlertType.ERROR, "Error en la base de datos", e.getMessage());
	        }
	    }
	}


	// Editar producto
	@FXML
	void editarProducto(ActionEvent event) {
		// Obtener el producto seleccionado en la tabla
		Producto productoSeleccionado = tvProductos.getSelectionModel().getSelectedItem();

		// Verificar que hay producto seleccionado
		if (productoSeleccionado != null) {
			// Mostrar alerta de confirmación
			Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
			confirmacion.setTitle("Confirmar edición");
			confirmacion.setHeaderText("¿Estás seguro de que deseas editar este producto?");
			confirmacion.setContentText("Producto: " + productoSeleccionado.getNombre());

			Optional<ButtonType> resultado = confirmacion.showAndWait();

			// Si el usuario confirma
			if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
				try {
					// Leer los nuevos datos de los campos de texto
					String nuevoNombre = txtFieldNombre.getText();
					double nuevoPrecio = Double.parseDouble(txtFieldPrecio.getText());
					int nuevoStock = Integer.parseInt(txtFieldStock.getText());
					String nuevaDescripcion = txtFieldDescripcion.getText();

					// Actualizar el objeto Producto con los nuevos valores
					productoSeleccionado.setNombre(nuevoNombre);
					productoSeleccionado.setPrecio(nuevoPrecio);
					productoSeleccionado.setStock(nuevoStock);
					productoSeleccionado.setDescripcion(nuevaDescripcion);

					// Actualizar la base de datos
					try (Connection conn = ConexionBD.getConnection()) {
						String sql = "UPDATE producto SET nombre = ?, precio = ?, stock = ?, descripcion = ? WHERE id_producto = ?";
						PreparedStatement stmt = conn.prepareStatement(sql);
						stmt.setString(1, nuevoNombre);
						stmt.setDouble(2, nuevoPrecio);
						stmt.setInt(3, nuevoStock);
						stmt.setString(4, nuevaDescripcion);
						stmt.setInt(5, productoSeleccionado.getIdProducto());

						int filasAfectadas = stmt.executeUpdate();

						if (filasAfectadas > 0) {
							// Refrescar tabla para mostrar cambios
							cargarProductos();

							// Mostrar mensaje de éxito
							Alertas.mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito",
									"Producto actualizado correctamente.");
						} else {
							Alertas.mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo actualizar el producto.");
						}
					}

				} catch (NumberFormatException e) {
					Alertas.mostrarAlerta(Alert.AlertType.WARNING, "Entrada inválida",
							"Precio y stock deben ser números válidos.");
				} catch (SQLException e) {
					e.printStackTrace();
					Alertas.mostrarAlerta(Alert.AlertType.ERROR, "Error en la base de datos", e.getMessage());
				}
			}

		} else {
			// Si no hay producto seleccionado, avisar al usuario
			Alertas.mostrarAlerta(Alert.AlertType.WARNING, "Sin selección", "Selecciona un producto para editar.");
		}
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
