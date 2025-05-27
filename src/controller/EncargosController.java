package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.event.ActionEvent;
import model.Encargo;
import model.ProductoEncargado;

public class EncargosController {

    @FXML
    private Button btnBorrarTodo;

    @FXML
    private Button btnNuevo;

    @FXML
    private Button btnVender;

    @FXML
    private ImageView imgFlecha;

    @FXML
    private Label lblCambio;

    @FXML
    private Label lblTotal;

    @FXML
    private TableView<Encargo> tvEncargos;

    @FXML
    private TableColumn<Encargo, String> tcNombreEncargos;

    @FXML
    private TableColumn<Encargo, String> tcApellidoEncargos;

    @FXML
    private TableColumn<Encargo, String> tcHoraEncargos;

    @FXML
    private TableColumn<Encargo, String> tcDescripcionEncargos;

    @FXML
    private TableView<ProductoEncargado> tvEncargo;

    @FXML
    private TableColumn<ProductoEncargado, String> tcProductoEncargo;

    @FXML
    private TableColumn<ProductoEncargado, Integer> tcCantidadEncargo;

    @FXML
    private TableColumn<ProductoEncargado, Double> tcPrecioEncargo;

    @FXML
    private TextField txtFieldEfectivo;

    // Lista observable global para gestionar los encargos
    private ObservableList<Encargo> listaEncargos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configura columnas encargos
        tcNombreEncargos.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcApellidoEncargos.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        tcHoraEncargos.setCellValueFactory(new PropertyValueFactory<>("hora"));
        tcDescripcionEncargos.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        // Configura columnas productos del encargo
        tcProductoEncargo.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcCantidadEncargo.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        tcPrecioEncargo.setCellValueFactory(new PropertyValueFactory<>("precio"));

        // Asignar lista observable vacía al TableView encargos
        tvEncargos.setItems(listaEncargos);

        // Listener para mostrar productos del encargo seleccionado
        tvEncargos.getSelectionModel().selectedItemProperty().addListener((obs, oldEncargo, newEncargo) -> {
            if (newEncargo != null) {
                tvEncargo.setItems(newEncargo.getProductos());
            } else {
                tvEncargo.setItems(FXCollections.observableArrayList());
            }
        });

        // Cargar encargos de ejemplo
        cargarEncargosEjemplo();

        recalcularTotal();
    }

    // Método para cargar encargos de ejemplo (puedes cambiar por BD)
    private void cargarEncargosEjemplo() {
        Encargo e1 = new Encargo("Pedro", "Almira", "12:00", "Encargo de productos");
        e1.addProducto(new ProductoEncargado("Leche", 2, 1.2));
        e1.addProducto(new ProductoEncargado("Pan", 1, 0.9));

        Encargo e2 = new Encargo("Ana", "García", "15:30", "Encargo urgente");
        e2.addProducto(new ProductoEncargado("Café", 3, 2.5));
        e2.addProducto(new ProductoEncargado("Azúcar", 1, 0.5));

        listaEncargos.addAll(e1, e2);
    }

    @FXML
    void abrirEncargar(ActionEvent event) {
        try {
            Parent menuRoot = FXMLLoader.load(getClass().getResource("/view/Encargar.fxml"));
            Stage stage = (Stage) imgFlecha.getScene().getWindow();
            stage.setScene(new Scene(menuRoot));
            stage.setTitle("Gestión Robles");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void borrarTodosEncargos(ActionEvent event) {
        listaEncargos.clear();
        tvEncargo.getItems().clear();
        lblTotal.setText("0.0 €");
        lblCambio.setText("0.0 €");
    }

    @FXML
    void restarDelEncargo(MouseEvent event) {
        Encargo seleccionado = tvEncargos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            listaEncargos.remove(seleccionado);
            tvEncargo.getItems().clear();
            recalcularTotal();
        }
    }

    @FXML
    void venderEncargo(ActionEvent event) {
        try {
            double total = calcularTotal();
            double efectivo = Double.parseDouble(txtFieldEfectivo.getText());

            if (efectivo < total) {
                lblCambio.setText("Efectivo insuficiente");
                return;
            }

            double cambio = efectivo - total;
            lblCambio.setText(String.format("Cambio: %.2f €", cambio));

            // Aquí puedes guardar la venta en la base de datos

            // Limpiar listas y UI
            listaEncargos.clear();
            tvEncargo.getItems().clear();
            lblTotal.setText("0.0 €");
            txtFieldEfectivo.clear();
        } catch (NumberFormatException e) {
            lblCambio.setText("Introduce un número válido");
        }
    }

    private void recalcularTotal() {
        double total = calcularTotal();
        lblTotal.setText(String.format("%.2f €", total));
    }

    private double calcularTotal() {
        double total = 0;
        for (Encargo encargo : listaEncargos) {
            total += encargo.getPrecioTotal();
        }
        return total;
    }

    @FXML
    void volverMenu(MouseEvent event) {
        try {
            Parent menuRoot = FXMLLoader.load(getClass().getResource("/view/Menu.fxml"));
            Stage stage = (Stage) imgFlecha.getScene().getWindow();
            stage.setScene(new Scene(menuRoot));
            stage.setTitle("Gestión Robles");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
