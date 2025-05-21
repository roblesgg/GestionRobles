package controller;

import java.util.List;

import database.ProductoDatabase;
import database.VentaDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Carrito;
import model.Producto;
import model.Venta;

public class VentaDirectaController {

    @FXML
    private Button btnVender;

    @FXML
    private ImageView cambioAdevolver;

    @FXML
    private ImageView imgFlecha;

    @FXML
    private Label lblCambio;

    @FXML
    private Label lblCambioText;

    @FXML
    private Label lblCarrito;

    @FXML
    private Label lblEfectivo;

    @FXML
    private Label lblProductos;

    @FXML
    private Label lblTotal;

    @FXML
    private Label lblTotalText;

    @FXML
    private TableView<Producto> tvProductos;
    
    @FXML
    private TableColumn<Producto, String> tcDescripcion;

    @FXML
    private TableColumn<Producto, Double> tcPrecio;

    @FXML
    private TableColumn<Producto, String> tcProducto;

    @FXML
    private TableColumn<Producto, Integer> tcStock;

    @FXML
    private TableView<Carrito> tvCarrito;

    @FXML
    private TableColumn<Carrito, Double> tcPrecioCarrito;
    
    @FXML
    private TableColumn<Carrito, String> tcProductoCarrito;

    @FXML
    private TableColumn<Carrito, Integer> tcCantidadCarrito;
    
    @FXML
    private TextField txtFieldEfectivo;
//---------------------------------------------------------
    
    //Variables
    private ObservableList<Carrito> carrito = FXCollections.observableArrayList();
    
    
    @FXML
    void initialize() {
        cargarProductos();
        cargarCarrito();
        //listener para actualizar entiempo real
        txtFieldEfectivo.textProperty().addListener((observable, oldValue, newValue) -> {
            cargarCambio();
        });
    }

        
//carga el cambio
    @FXML
    public void cargarCambio() {
        try {
            // Obtener efectivo introducido (convertir a double)
            double efectivo = Double.parseDouble(txtFieldEfectivo.getText().trim());

            // Obtener total (el texto sin el símbolo € y espacios)
            String totalStr = lblTotal.getText().replace("€", "").trim();
            double total = Double.parseDouble(totalStr);

            // Calcular cambio
            double cambio = efectivo - total;

            if (cambio < 0) {
                lblCambio.setText("Efectivo insuficiente");
                lblCambio.setStyle("-fx-text-fill: red;");
            } else {
                lblCambio.setText(String.format("%.2f €", cambio));
                lblCambio.setStyle("-fx-text-fill: green;");
            }
        } catch (NumberFormatException e) {
            // Cuando el texto no es un número válido o está vacío
            lblCambio.setText("Introduce un número válido");
            lblCambio.setStyle("-fx-text-fill: orange;");
        }
    }


    
 // Configura las columnas del carrito y carga la tabla
    @FXML
    private void cargarCarrito() {
        tcProductoCarrito.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcPrecioCarrito.setCellValueFactory(new PropertyValueFactory<>("precio"));
        tcCantidadCarrito.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        tvCarrito.setItems(carrito);
    }

    //carga la tabla productos
    @FXML
    public void cargarProductos() {
        // Configuramos las columnas para que muestren las propiedades correctas de Producto
        tcProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        tcStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        tcDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        
        // Obtenemos la lista de productos de la base de datos
        List<Producto> productos = ProductoDatabase.obtenerProductos();

        // Convertimos la lista a ObservableList para que la tabla pueda trabajar con ella
        ObservableList<Producto> listaProductos = FXCollections.observableArrayList(productos);

        // Cargamos los productos en el TableView
        tvProductos.setItems(listaProductos);
    }
    
    @FXML
    private void agregarAcarrito(MouseEvent event) {
        Producto producto = tvProductos.getSelectionModel().getSelectedItem();
        // Buscar si el producto ya está en el carrito
        for (Carrito item : carrito) {
            if (item.getNombre().equals(producto.getNombre())) {
                // Aumentar cantidad si hay stock suficiente
                if (item.getCantidad() < producto.getStock()) {
                    item.setCantidad(item.getCantidad() + 1);
                    tvCarrito.refresh();
                    actualizarTotal();
                }
                return;
            }
        }
        // Si no estaba, agregar nuevo con cantidad 1
        if (producto.getStock() > 0) {
            carrito.add(new Carrito(producto, 1));
            actualizarTotal();
        }
    }
    
    //Actualiza el total delcarrito
    private void actualizarTotal() {
        double total = 0;
        for (Carrito item : carrito) {
            total += item.getSubtotal();
        }
        lblTotal.setText(String.format("%.2f €", total));
    }
    
    //quitar del carrito productos
    @FXML
    private void restarDelCarrito(MouseEvent event) {
        Carrito seleccionado = tvCarrito.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            int cantidadActual = seleccionado.getCantidad();
            if (cantidadActual > 1) {
                seleccionado.setCantidad(cantidadActual - 1);
            } else {
                // Si la cantidad es 1, al restar queda 0, elimina el producto del carrito
                carrito.remove(seleccionado);
            }
            tvCarrito.refresh();
            actualizarTotal();
        }
    }

    //vender
    @FXML
    void venderCarrito(ActionEvent event) {
        // Verifica si hay productos en el carrito
        if (carrito.isEmpty()) {
            lblCambio.setText("Carrito vacío");
            lblCambio.setStyle("-fx-text-fill: red;");
            return;
        }

        // Verifica si el efectivo es suficiente
        double efectivo;
        try {
            efectivo = Double.parseDouble(txtFieldEfectivo.getText().trim());
        } catch (NumberFormatException e) {
            lblCambio.setText("Introduce un número válido");
            lblCambio.setStyle("-fx-text-fill: orange;");
            return;
        }

        double total = 0;
        for (Carrito item : carrito) {
            total += item.getSubtotal();
        }

        if (efectivo < total) {
            lblCambio.setText("Efectivo insuficiente");
            lblCambio.setStyle("-fx-text-fill: red;");
            return;
        }

        // Registrar venta
        Venta venta = new Venta(false, null); // venta directa
        int idVenta = VentaDatabase.insertarVenta(venta);

        if (idVenta > 0) {
            VentaDatabase.insertarDetalleVenta(idVenta, carrito);
            VentaDatabase.actualizarStock(carrito);

            lblCambio.setText(String.format("Cambio: %.2f €", efectivo - total));
            lblCambio.setStyle("-fx-text-fill: green;");

            carrito.clear();
            tvCarrito.refresh();
            actualizarTotal();
            cargarProductos(); // para refrescar stock en la tabla de productos

        } else {
            lblCambio.setText("Error al guardar venta");
            lblCambio.setStyle("-fx-text-fill: red;");
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
