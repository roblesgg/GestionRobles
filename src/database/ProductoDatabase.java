package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Producto;

public class ProductoDatabase {

    // Este método devuelve una lista de productos obtenida desde la base de datos
	public static List<Producto> obtenerProductos() {
	    List<Producto> productos = new ArrayList<>();

	    try (
	        Connection conn = ConexionBD.getConnection();
	        Statement stmt = conn.createStatement();
	        ResultSet rs = stmt.executeQuery("SELECT * FROM producto")
	    ) {
	        while (rs.next()) {
	            // Creamos el producto con los datos de la fila
	            Producto p = new Producto(
	                rs.getString("NOMBRE"),    // Nombre del producto
	                rs.getDouble("PRECIO"),    // Precio del producto
	                rs.getInt("STOCK"),        // Stock disponible
	                rs.getString("DESCRIPCION")// Descripción del producto
	            );
	            
	            // IMPORTANTE: asignamos el ID del producto para poder luego referenciarlo (por ejemplo, para borrarlo)
	            p.setIdProducto(rs.getInt("ID_PRODUCTO"));

	            // Añadimos el producto a la lista
	            productos.add(p);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return productos;
	}

    
    
    //Insertar Producto
    public static boolean insertarProducto(Producto producto) {
        // 1. Definimos la consulta SQL con parámetros (uso de ? para evitar inyecciones SQL)
        String sql = "INSERT INTO producto (nombre, precio, stock, descripcion) VALUES (?, ?, ?, ?)";

        try (
            // 2. Obtenemos la conexión a la base de datos desde la clase ConexionBD
            Connection conn = ConexionBD.getConnection();

            // 3. Preparamos la sentencia SQL con los parámetros
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            // 4. Asignamos los valores del objeto producto a cada parámetro de la consulta
            stmt.setString(1, producto.getNombre());       // Primer ?
            stmt.setDouble(2, producto.getPrecio());       // Segundo ?
            stmt.setInt(3, producto.getStock());           // Tercer ?
            stmt.setString(4, producto.getDescripcion());  // Cuarto ?

            // 5. Ejecutamos la sentencia y comprobamos si afectó a alguna fila
            int filasInsertadas = stmt.executeUpdate();

            // 6. Si se insertó al menos una fila, devolvemos true
            return filasInsertadas > 0;

        } catch (SQLException e) {
            // 7. Si ocurre un error con la base de datos, mostramos el error por consola
            System.out.println("Error al insertar producto en la base de datos:");
            e.printStackTrace();
            return false;
        }
    }


}
