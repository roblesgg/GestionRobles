package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Encargo {
    private String nombre;
    private String apellido;
    private String hora;
    private String descripcion;
    private ObservableList<ProductoEncargado> productos = FXCollections.observableArrayList();

    public Encargo(String nombre, String apellido, String hora, String descripcion) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.hora = hora;
        this.descripcion = descripcion;
    }

    // Getters actuales
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getHora() { return hora; }
    public String getDescripcion() { return descripcion; }

    // Getter y setter para productos
    public ObservableList<ProductoEncargado> getProductos() {
        return productos;
    }

    public void addProducto(ProductoEncargado p) {
        productos.add(p);
    }

    // Método para calcular el total real sumando productos
    public double getPrecioTotal() {
        double total = 0;
        for (ProductoEncargado p : productos) {
            total += p.getCantidad() * p.getPrecio();
        }
        return total;
    }
}
