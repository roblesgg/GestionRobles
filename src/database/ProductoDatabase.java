package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Producto;

public class ProductoDatabase {

	// Obtener todos los productos
	public static List<Producto> obtenerProductos() {
		List<Producto> productos = new ArrayList<>();

		String sql = "SELECT * FROM producto";

		try (
			Connection conn = ConexionBD.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql)
		) {
			while (rs.next()) {
				Producto p = new Producto(
					rs.getString("NOMBRE"),
					rs.getDouble("PRECIO"),
					rs.getInt("STOCK"),
					rs.getString("DESCRIPCION")
				);
				p.setIdProducto(rs.getInt("ID_PRODUCTO"));
				p.setVendido(rs.getInt("VENDIDO"));
				productos.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return productos;
	}

	// Insertar un producto
	public static boolean insertarProducto(Producto producto) {
		String sql = "INSERT INTO producto (nombre, precio, stock, descripcion) VALUES (?, ?, ?, ?)";

		try (
			Connection conn = ConexionBD.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql)
		) {
			stmt.setString(1, producto.getNombre());
			stmt.setDouble(2, producto.getPrecio());
			stmt.setInt(3, producto.getStock());
			stmt.setString(4, producto.getDescripcion());

			int filasInsertadas = stmt.executeUpdate();
			return filasInsertadas > 0;

		} catch (SQLException e) {
			System.out.println("Error al insertar producto en la base de datos:");
			e.printStackTrace();
			return false;
		}
	}

	// Comprobar si un producto tiene ventas asociadas en detalle_venta
	public static boolean tieneVentas(int idProducto) {
		String sql = "SELECT COUNT(*) FROM detalle_venta WHERE ID_PRODUCTO = ?";
		try (
			Connection conn = ConexionBD.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql)
		) {
			stmt.setInt(1, idProducto);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Borrar un producto si no tiene ventas asociadas
	public static boolean borrarProducto(int idProducto) {
		if (tieneVentas(idProducto)) {
			return false;
		}

		String sql = "DELETE FROM producto WHERE ID_PRODUCTO = ?";
		try (
			Connection conn = ConexionBD.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql)
		) {
			stmt.setInt(1, idProducto);
			int filasBorradas = stmt.executeUpdate();
			return filasBorradas > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// Borrar todos los productos que NO tienen ventas asociadas (usando detalle_venta)
	public static int borrarTodosProductos() throws SQLException {
		String sqlSeleccion = "SELECT id_producto FROM producto";
		String sqlBorrado = "DELETE FROM producto WHERE id_producto = ?";

		int borrados = 0;

		try (
			Connection conn = ConexionBD.getConnection();
			PreparedStatement psSelect = conn.prepareStatement(sqlSeleccion);
			PreparedStatement psDelete = conn.prepareStatement(sqlBorrado)
		) {
			ResultSet rs = psSelect.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id_producto");
				if (!tieneVentas(id)) {
					psDelete.setInt(1, id);
					borrados += psDelete.executeUpdate();
				}
			}
		}
		return borrados;
	}
}
