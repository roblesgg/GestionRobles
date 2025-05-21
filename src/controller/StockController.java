package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import database.ConexionBD;         // Clase para conectar con la base de datos
import database.ProductoDatabase; // Clase que contiene métodos para acceder a productos en BD
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Producto;

public class StockController {

    @FXML
    private ImageView imgFlecha;

    @FXML
    private ImageView imgFondo;

    @FXML
    private ImageView imgReiniciar;

    @FXML
    private ImageView imgSuma;
    
    @FXML
    private ImageView imgResta;

    @FXML
    private Label lblProductos;

    @FXML
    private TextField txtFieldPrecio;
    
    @FXML
    private TableView<Producto> tvProductos; 
    
    @FXML
    private TableColumn<Producto, String> tcProducto;   

    @FXML
    private TableColumn<?, ?> tcEncargado;

    @FXML
    private TableColumn<Producto, Integer> tcStock; 

    @FXML
    private TableColumn<Producto, Integer> tcVendido;     

//-------------------------------------------------------

    @FXML
    void initialize() {
        cargarStock();
    }
    
    // Método para cargar los productos desde la base de datos y mostrar en la tabla
    public void cargarStock() {
        // Enlazamos cada columna con la propiedad del objeto Producto correspondiente
        tcProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcVendido.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        tcStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        tcVendido.setCellValueFactory(new PropertyValueFactory<>("vendido"));

        // Obtenemos la lista de productos desde la base de datos (incluye campo vendido)
        List<Producto> productos = ProductoDatabase.obtenerProductos();

        // Limpiamos y cargamos todos los productos en el TableView para mostrar
        tvProductos.getItems().setAll(productos);
    }

    // Método para reiniciar el contador 'vendido' a cero para todos los productos
    @FXML
    void reiniciarVendido(MouseEvent event) {
        // Ejecutamos la actualización en la base de datos
        try (Connection conn = ConexionBD.getConnection()) {
            // SQL para poner a 0 la columna VENDIDO de todos los productos
            String sql = "UPDATE producto SET vendido = 0";
            
            // Preparamos la sentencia SQL y la ejecutamos
            PreparedStatement stmt = conn.prepareStatement(sql);
            int filasActualizadas = stmt.executeUpdate();
            
            // Podemos mostrar en consola cuántos productos se actualizaron (opcional)
            System.out.println("Vendidos reiniciados para " + filasActualizadas + " productos.");

            // Refrescamos la tabla para mostrar los cambios
            cargarStock();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Métodos pendientes (de momento vacíos)
    @FXML
    void añadirCantidad(MouseEvent event) {
        try {
            // Obtener el producto seleccionado de la tabla
            Producto productoSeleccionado = tvProductos.getSelectionModel().getSelectedItem();

            // Verificar que haya un producto seleccionado
            if (productoSeleccionado == null) {
                System.out.println("No se ha seleccionado ningún producto.");
                return;
            }

            // Obtener la cantidad introducida en el campo de texto
            String textoCantidad = txtFieldPrecio.getText().trim();

            // Verificar que el campo no esté vacío y que sea un número entero válido
            if (textoCantidad.isEmpty()) {
                System.out.println("Por favor, introduce una cantidad.");
                return;
            }

            int cantidadASumar = Integer.parseInt(textoCantidad);

            // Sumar la cantidad al stock actual
            int nuevoStock = productoSeleccionado.getStock() + cantidadASumar;

            // Actualizar el stock en la base de datos
            try (Connection conn = ConexionBD.getConnection()) {
                String sql = "UPDATE producto SET stock = ? WHERE id_producto = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, nuevoStock);
                stmt.setInt(2, productoSeleccionado.getIdProducto()); // Asegúrate de tener este getter
                stmt.executeUpdate();
            }

            // Recargar la tabla con los nuevos datos
            cargarStock();

            // Vaciar el campo de texto
            txtFieldPrecio.clear();

            // Mensaje opcional
            System.out.println("Stock actualizado correctamente.");
            
        } catch (NumberFormatException e) {
            System.out.println("Cantidad no válida. Debe ser un número entero.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al actualizar el stock.");
        }
    }
    
    @FXML
    void restarCantidad(MouseEvent event) {
        try {
            // Obtener el producto seleccionado
            Producto productoSeleccionado = tvProductos.getSelectionModel().getSelectedItem();

            if (productoSeleccionado == null) {
                System.out.println("No se ha seleccionado ningún producto.");
                return;
            }

            String textoCantidad = txtFieldPrecio.getText().trim();

            if (textoCantidad.isEmpty()) {
                System.out.println("Introduce una cantidad a restar.");
                return;
            }

            int cantidadARestar = Integer.parseInt(textoCantidad);

            // Verificar que no se reste más de lo que hay en stock
            int stockActual = productoSeleccionado.getStock();

            if (cantidadARestar > stockActual) {
                System.out.println("No puedes restar más de lo que hay en stock.");
                return;
            }

            int nuevoStock = stockActual - cantidadARestar;

            // Actualizar el stock en la base de datos
            try (Connection conn = ConexionBD.getConnection()) {
                String sql = "UPDATE producto SET stock = ? WHERE id_producto = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, nuevoStock);
                stmt.setInt(2, productoSeleccionado.getIdProducto());
                stmt.executeUpdate();
            }

            // Recargar la tabla y limpiar el campo
            cargarStock();
            txtFieldPrecio.clear();

            System.out.println("Stock reducido correctamente.");
            
        } catch (NumberFormatException e) {
            System.out.println("Cantidad no válida. Debe ser un número entero.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al restar stock.");
        }
    }


    // Método para volver a la ventana del menú principal
    @FXML
    void volverMenu(MouseEvent event) {
        try {
            // Carga la vista Menu.fxml
            Parent menuRoot = FXMLLoader.load(getClass().getResource("/view/Menu.fxml"));

            // Obtén la ventana actual a partir de la imagen imgFlecha
            Stage stage = (Stage) imgFlecha.getScene().getWindow();

            // Crea una nueva escena con la vista Menu
            Scene scene = new Scene(menuRoot);

            // Cambia la escena actual por la nueva escena del menú
            stage.setScene(scene);
            stage.setTitle("Gestión Robles");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
