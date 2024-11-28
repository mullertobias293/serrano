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
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentanaPrincipal {

    private static final String URL = "jdbc:mysql://b7bqlyp9wflopvdwklxn-mysql.services.clever-cloud.com:3306/b7bqlyp9wflopvdwklxn";
private static final String USER = "u3kvhd5surotasij";
private static final String PASSWORD = "98XeROAFkzzXYvQKhEor";


    public static void main(String[] args) {
        
        // Crear la ventana principal
        JFrame frame = new JFrame("Sistema de Ventas Manaos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLayout(null);

        // Botón para mostrar inventario
        JButton btnInventario = new JButton("Ver Inventario");
        btnInventario.setBounds(50, 50, 150, 30);
        frame.add(btnInventario);

        // Botón para registrar venta
        JButton btnVenta = new JButton("Registrar Venta");
        btnVenta.setBounds(50, 100, 150, 30);
        frame.add(btnVenta);
        
        JButton btnAgregarBebida = new JButton("Agregar Bebida");
        btnAgregarBebida.setBounds(220, 50, 150, 30); // Posición ajustada
        frame.add(btnAgregarBebida);
        
        JButton btnHistorial = new JButton("Historial de Ventas");
        btnHistorial.setBounds(220, 100, 150, 30); // Posición ajustada
        frame.add(btnHistorial);

        // TextArea para mostrar mensajes
        JTextArea textArea = new JTextArea();
        textArea.setBounds(50, 250, 500, 200);
        textArea.setEditable(false);
        frame.add(textArea);
        
        // Acción para ver historial 
        btnHistorial.addActionListener(e -> {
    StringBuilder historialStr = new StringBuilder("Historial de Ventas:\n");
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT * FROM ventas")) {

        while (rs.next()) {
            historialStr.append("ID Venta: ").append(rs.getInt("id"))
                         .append(", Fecha: ").append(rs.getString("fecha"))
                         .append(", Total: ").append(rs.getDouble("total"))
                         .append("\n");
        }
    } catch (SQLException ex) {
        historialStr.append("Error al cargar historial: ").append(ex.getMessage());
    }
    textArea.setText(historialStr.toString());
});
        
        // Acción para mostrar inventario
        btnInventario.addActionListener(e -> {
    StringBuilder inventarioStr = new StringBuilder("Inventario:\n");
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT * FROM productos")) {

        while (rs.next()) {
            inventarioStr.append("ID: ").append(rs.getInt("id"))
                         .append(", Nombre: ").append(rs.getString("nombre"))
                         .append(", Precio: ").append(rs.getDouble("precio"))
                         .append(", Stock: ").append(rs.getInt("stock"))
                         .append("\n");
        }
    } catch (SQLException ex) {
        inventarioStr.append("Error al cargar inventario: ").append(ex.getMessage());
    }
    textArea.setText(inventarioStr.toString());
});

        // Acción para registrar venta
        btnVenta.addActionListener(e -> {
            String idProductoStr = JOptionPane.showInputDialog(frame, "Ingrese ID del Producto:");
            String cantidadStr = JOptionPane.showInputDialog(frame, "Ingrese Cantidad:");
            try {
                int idProducto = Integer.parseInt(idProductoStr);
                int cantidad = Integer.parseInt(cantidadStr);

                if (actualizarStock(idProducto, cantidad)) {
                    registrarVenta(idProducto, cantidad);
                    textArea.setText("Venta realizada con éxito.");
                } else {
                    textArea.setText("Stock insuficiente o producto no encontrado.");
                }
            } catch (NumberFormatException ex) {
                textArea.setText("Entrada inválida.");
            }
        });
        
        
        // Acción para agregar bebida
        btnAgregarBebida.addActionListener(e -> {
    try {
        String nombre = JOptionPane.showInputDialog(frame, "Ingrese el nombre de la bebida:");
        String precioStr = JOptionPane.showInputDialog(frame, "Ingrese el precio de la bebida:");
        String stockStr = JOptionPane.showInputDialog(frame, "Ingrese el stock inicial:");

        if (nombre != null && !nombre.isEmpty() && precioStr != null && stockStr != null) {
            double precio = Double.parseDouble(precioStr);
            int stock = Integer.parseInt(stockStr);

            if (agregarBebida(nombre, precio, stock)) {
                textArea.setText("Bebida agregada con éxito.");
            } else {
                textArea.setText("Error al agregar la bebida.");
            }
        } else {
            textArea.setText("Todos los campos son obligatorios.");
        }
    } catch (NumberFormatException ex) {
        textArea.setText("Entrada inválida. Verifique los datos ingresados.");
    }
});
        // Mostrar ventana
        frame.setVisible(true);
    }
    
   
    
    
    
    
    
    
    
public static boolean agregarBebida(String nombre, double precio, int stock) {
    String sql = "INSERT INTO productos (nombre, precio, stock) VALUES (?, ?, ?)";
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, nombre);
        pstmt.setDouble(2, precio);
        pstmt.setInt(3, stock);
        int rowsAffected = pstmt.executeUpdate();

        return rowsAffected > 0;
    } catch (SQLException e) {
        System.out.println("Error al agregar bebida: " + e.getMessage());
        return false;
    }
}
    

    public static boolean actualizarStock(int idProducto, int cantidad) {
        String sql = "UPDATE productos SET stock = stock - ? WHERE id = ? AND stock >= ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, cantidad);
            pstmt.setInt(2, idProducto);
            pstmt.setInt(3, cantidad);
            int rowsAffected = pstmt.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar stock: " + e.getMessage());
            return false;
        }
    }

    public static void registrarVenta(int idProducto, int cantidad) {
        String ventaSQL = "INSERT INTO ventas (fecha, total) VALUES (NOW(), ?)";
        String detalleSQL = "INSERT INTO detalles_venta (id_venta, id_producto, cantidad) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);

            // Insertar venta
            double precio = obtenerPrecioProducto(idProducto);
            try (PreparedStatement ventaStmt = conn.prepareStatement(ventaSQL, Statement.RETURN_GENERATED_KEYS)) {
                ventaStmt.setDouble(1, precio * cantidad);
                ventaStmt.executeUpdate();

                // Obtener ID de la venta generada
                ResultSet rs = ventaStmt.getGeneratedKeys();
                if (rs.next()) {
                    int idVenta = rs.getInt(1);

                    // Insertar detalles de la venta
                    try (PreparedStatement detalleStmt = conn.prepareStatement(detalleSQL)) {
                        detalleStmt.setInt(1, idVenta);
                        detalleStmt.setInt(2, idProducto);
                        detalleStmt.setInt(3, cantidad);
                        detalleStmt.executeUpdate();
                    }
                }
            }

            conn.commit();
        } catch (SQLException e) {
            System.out.println("Error al registrar venta: " + e.getMessage());
        }
    }

    // Método para obtener el precio de un producto por ID
    public static double obtenerPrecioProducto(int idProducto) {
        String sql = "SELECT precio FROM productos WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idProducto);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("precio");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener precio: " + e.getMessage());
        }
        return 0;
    }
    


    
}
