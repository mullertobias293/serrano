/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoserrano;

/**
 *
 * @author Administrator
 */
import java.sql.*;

public class BaseDeDatos {

    private static final String URL = "jdbc:sqlite:manaos.db";

    public static void main(String[] args) {
        crearTablas();
        insertarProducto(1, "Manaos Cola", 50.0, 100);
        insertarProducto(2, "Manaos Naranja", 55.0, 50);
        mostrarProductos();
    }

    public static void crearTablas() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            // Crear tabla de productos
            String productos = "CREATE TABLE IF NOT EXISTS productos (" +
                               "id INTEGER PRIMARY KEY," +
                               "nombre TEXT NOT NULL," +
                               "precio REAL NOT NULL," +
                               "stock INTEGER NOT NULL)";
            stmt.execute(productos);

            // Crear tabla de ventas
            String ventas = "CREATE TABLE IF NOT EXISTS ventas (" +
                            "id INTEGER PRIMARY KEY," +
                            "fecha TEXT NOT NULL," +
                            "total REAL NOT NULL)";
            stmt.execute(ventas);

            // Crear tabla de detalles de venta
            String detallesVenta = "CREATE TABLE IF NOT EXISTS detalles_venta (" +
                                   "id_venta INTEGER NOT NULL," +
                                   "id_producto INTEGER NOT NULL," +
                                   "cantidad INTEGER NOT NULL," +
                                   "FOREIGN KEY (id_venta) REFERENCES ventas (id)," +
                                   "FOREIGN KEY (id_producto) REFERENCES productos (id))";
            stmt.execute(detallesVenta);

            System.out.println("Tablas creadas con éxito.");
        } catch (SQLException e) {
            System.out.println("Error al crear tablas: " + e.getMessage());
        }
    }

    public static void insertarProducto(int id, String nombre, double precio, int stock) {
        String sql = "INSERT INTO productos (id, nombre, precio, stock) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.setString(2, nombre);
            pstmt.setDouble(3, precio);
            pstmt.setInt(4, stock);
            pstmt.executeUpdate();

            System.out.println("Producto insertado: " + nombre);
        } catch (SQLException e) {
            System.out.println("Error al insertar producto: " + e.getMessage());
        }
    }

    public static void mostrarProductos() {
        String sql = "SELECT * FROM productos";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Productos en inventario:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                                   ", Nombre: " + rs.getString("nombre") +
                                   ", Precio: " + rs.getDouble("precio") +
                                   ", Stock: " + rs.getInt("stock"));
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar productos: " + e.getMessage());
        }
    }
}