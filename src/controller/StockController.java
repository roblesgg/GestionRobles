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
        // Aquí implementarás la lógica para añadir cantidad al stock del producto seleccionado
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
