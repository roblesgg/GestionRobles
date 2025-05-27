package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Producto;
import model.ProductoEncargado;
import database.ProductoDatabase;

import java.util.List;

public class EncargarController {

    private ObservableList<ProductoEncargado> listaEncargo = FXCollections.observableArrayList();
    private ObservableList<Producto> listaProductos = FXCollections.observableArrayList();

    @FXML
    private TableView<ProductoEncargado> tvCarrito;

    @FXML
    private TableColumn<ProductoEncargado, String> tcProductoCarrito;

    @FXML
    private TableColumn<ProductoEncargado, Integer> tcCantidadCarrito;

    @FXML
    private TableColumn<ProductoEncargado, Double> tcPrecioCarrito;

    @FXML
    private TableView<Producto> tvProductos;

    @FXML
    private TableColumn<Producto, String> tcProducto;

    @FXML
    private TableColumn<Producto, Double> tcPrecio;

    @FXML
    private TableColumn<Producto, Integer> tcStock;

    @FXML
    private TableColumn<Producto, String> tcDescripcion;

    @FXML
    private Label lblTotal;

    @FXML
    private TextField txtFieldNombre, txtFieldApellido, txtFieldHora, txtFieldDescripcion;

    @FXML
    private ImageView imgFlecha;

    // Inicialización del controlador
    @FXML
    public void initialize() {
        // Configurar columnas de la tabla de carrito (encargo)
        tcProductoCarrito.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcCantidadCarrito.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        tcPrecioCarrito.setCellValueFactory(new PropertyValueFactory<>("precio"));
        tvCarrito.setItems(listaEncargo);

        // Configurar columnas de la tabla de productos
        tcProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        tcStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        tcDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        // Cargar productos desde la base de datos
        cargarProductos();
    }

    // Cargar productos desde la base de datos
    public void cargarProductos() {
        List<Producto> productos = ProductoDatabase.obtenerProductos();
        listaProductos.setAll(productos);
        tvProductos.setItems(listaProductos);
    }

    // Añadir producto al carrito cuando se haga clic en un producto en tvProductos
    @FXML
    void agregarAcarrito(MouseEvent event) {
        Producto seleccionado = tvProductos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            // Buscar si ya está en el carrito
            ProductoEncargado encontrado = null;
            for (ProductoEncargado p : listaEncargo) {
                if (p.getNombre().equals(seleccionado.getNombre())) {
                    encontrado = p;
                    break;
                }
            }
            if (encontrado != null) {
                // Incrementar cantidad si no supera el stock
                if (encontrado.getCantidad() < seleccionado.getStock()) {
                    encontrado.setCantidad(encontrado.getCantidad() + 1);
                } else {
                    mostrarAlerta("Stock insuficiente", "No hay más stock disponible para este producto.");
                }
            } else {
                // Añadir nuevo producto con cantidad 1
                listaEncargo.add(new ProductoEncargado(seleccionado.getNombre(), 1, seleccionado.getPrecio()));
            }
            tvCarrito.refresh();
            actualizarTotal();
        }
    }

    // Reducir cantidad o eliminar producto del carrito con clic en tvCarrito
    @FXML
    void restarDelCarrito(MouseEvent event) {
        ProductoEncargado seleccionado = tvCarrito.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            if (seleccionado.getCantidad() > 1) {
                seleccionado.setCantidad(seleccionado.getCantidad() - 1);
            } else {
                listaEncargo.remove(seleccionado);
            }
            tvCarrito.refresh();
            actualizarTotal();
        }
    }

    // Registrar el encargo con los datos del usuario y la lista de productos del carrito
    @FXML
    void encargarCarrito(ActionEvent event) {
        if (listaEncargo.isEmpty()) {
            mostrarAlerta("Encargo vacío", "Agrega al menos un producto antes de encargar.");
            return;
        }

        String nombre = txtFieldNombre.getText();
        String apellido = txtFieldApellido.getText();
        String hora = txtFieldHora.getText();
        String descripcion = txtFieldDescripcion.getText();

        if (nombre.isEmpty() || apellido.isEmpty() || hora.isEmpty()) {
            mostrarAlerta("Datos incompletos", "Nombre, apellido y hora son obligatorios.");
            return;
        }

        // Guardar encargo y productos en la base de datos (a implementar)

        System.out.println("Encargo de: " + nombre + " " + apellido + " a las " + hora);
        for (ProductoEncargado p : listaEncargo) {
            System.out.println("- " + p.getNombre() + " x" + p.getCantidad() + " (" + p.getPrecio() + "€)");
        }

        // Limpiar campos y lista
        listaEncargo.clear();
        txtFieldNombre.clear();
        txtFieldApellido.clear();
        txtFieldHora.clear();
        txtFieldDescripcion.clear();
        actualizarTotal();

        mostrarAlerta("Encargo realizado", "El encargo ha sido registrado correctamente.");
    }

    // Actualizar el total del encargo
    private void actualizarTotal() {
        double total = 0;
        for (ProductoEncargado p : listaEncargo) {
            total += p.getCantidad() * p.getPrecio();
        }
        lblTotal.setText(String.format("%.2f €", total));
    }

    // Mostrar una alerta
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Volver a la vista de encargos
    @FXML
    void volverEncargos(MouseEvent event) {
        try {
            Parent menuRoot = FXMLLoader.load(getClass().getResource("/view/Encargos.fxml"));
            Stage stage = (Stage) imgFlecha.getScene().getWindow();
            stage.setScene(new Scene(menuRoot));
            stage.setTitle("Gestión Robles");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
