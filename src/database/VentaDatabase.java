package database;

import java.sql.*;
import java.util.List;

import model.Carrito;
import model.Venta;

public class VentaDatabase {

    /**
     * Inserta una nueva venta en la tabla `venta`.
     * @param venta Objeto Venta con la información a guardar.
     * @return ID autogenerado de la venta insertada o -1 si falla.
     */
    public static int insertarVenta(Venta venta) {
        int idGenerado = -1;

        // Consulta SQL para insertar la venta
        String sql = "INSERT INTO venta (CON_ENCARGO, ID_ENCARGO) VALUES (?, ?)";

        try (Connection conn = ConexionBD.getConnection(); // Abre conexión a la BD
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Establece los parámetros en el PreparedStatement
            stmt.setBoolean(1, venta.isConEncargo());

            if (venta.getIdEncargo() != null) {
                stmt.setInt(2, venta.getIdEncargo());
            } else {
                stmt.setNull(2, Types.INTEGER); // Si no hay encargo, se pone NULL
            }

            // Ejecuta la inserción
            stmt.executeUpdate();

            // Recupera el ID autogenerado de la venta
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Imprime error si falla
        }

        return idGenerado;
    }

    /**
     * Inserta los productos de un carrito como detalle de venta.
     * @param idVenta ID de la venta a la que pertenecen los productos.
     * @param carrito Lista de productos vendidos.
     */
    public static void insertarDetalleVenta(int idVenta, List<Carrito> carrito) {
        String sql = "INSERT INTO detalle_venta (id_venta, id_producto, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Recorre cada ítem del carrito y lo añade como un lote (batch)
            for (Carrito item : carrito) {
                stmt.setInt(1, idVenta); // ID de la venta
                stmt.setInt(2, item.getProducto().getIdProducto()); // ID del producto
                stmt.setInt(3, item.getCantidad()); // Cantidad vendida
                stmt.setDouble(4, item.getPrecio()); // Precio unitario (puede incluir descuentos o precio de venta)
                stmt.addBatch(); // Añade la operación al lote
            }

            // Ejecuta todos los inserts de golpe
            stmt.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Actualiza el stock de los productos vendidos, restando la cantidad comprada.
     * @param carrito Lista de productos vendidos.
     */
    public static void actualizarStock(List<Carrito> carrito) {
        String sqlUpdateStock = "UPDATE producto SET stock = stock - ? WHERE id_producto = ?";
        String sqlUpdateVendido = "UPDATE producto SET vendido = vendido + ? WHERE id_producto = ?";

        try (Connection conn = ConexionBD.getConnection()) {
            for (Carrito item : carrito) {
                // Actualizar stock
                try (PreparedStatement psStock = conn.prepareStatement(sqlUpdateStock);
                     PreparedStatement psVendido = conn.prepareStatement(sqlUpdateVendido)) {

                    // Disminuir stock
                    psStock.setInt(1, item.getCantidad());
                    psStock.setInt(2, item.getProducto().getIdProducto());
                    psStock.executeUpdate();

                    // Incrementar vendidos
                    psVendido.setInt(1, item.getCantidad());
                    psVendido.setInt(2, item.getProducto().getIdProducto());
                    psVendido.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static int borrarTodasVentasConDetalles() throws Exception {
        String borrarDetalles = "DELETE FROM detalle_venta";
        String borrarVentas = "DELETE FROM venta";

        try (
            Connection conn = ConexionBD.getConnection();
            Statement stmt = conn.createStatement()
        ) {
            // Primero eliminamos los detalles de las ventas (por dependencia)
            stmt.executeUpdate(borrarDetalles);

            // Luego eliminamos las ventas
            int eliminadas = stmt.executeUpdate(borrarVentas);

            return eliminadas;
        }
    }

}
