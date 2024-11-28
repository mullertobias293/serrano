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
import java.awt.Color;
import java.awt.Image;
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
        frame.setSize(700, 600);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.RED);
        
        ImageIcon icon = new ImageIcon(VentanaPrincipal.class.getResource("/images/logo.png"));

        // Redimensionar imagen
        Image scaledImage = icon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImage);

        // Mostrar imagen en un JLabel
        JLabel labelImagen = new JLabel(resizedIcon);
        labelImagen.setBounds(175, 10, 300, 175);
        frame.add(labelImagen);

        // Botón para mostrar inventario
        JButton btnInventario = new JButton("Ver Inventario");
        btnInventario.setBounds(50, 250, 150, 30);
        frame.add(btnInventario);

        // Botón para registrar venta
        JButton btnVenta = new JButton("Registrar Venta");
        btnVenta.setBounds(50, 300, 150, 30);
        frame.add(btnVenta);
        
        JButton btnAgregarBebida = new JButton("Agregar Bebida");
        btnAgregarBebida.setBounds(220, 250, 150, 30); // Posición ajustada
        frame.add(btnAgregarBebida);
        
        JButton btnHistorial = new JButton("Historial de Ventas");
        btnHistorial.setBounds(220, 300, 150, 30); // Posición ajustada
        frame.add(btnHistorial);
        
        //borrar gaseosa
        JButton btnEliminarBebida = new JButton("Eliminar Bebida");
        btnEliminarBebida.setBounds(390, 250, 150, 30); // Ajusta la posición
        frame.add(btnEliminarBebida);
        
        JButton btnActualizarStock = new JButton("Actualizar Stock");
        btnActualizarStock.setBounds(390, 300, 150, 30);
        frame.add(btnActualizarStock);

        // TextArea para mostrar mensajes
        JTextArea textArea = new JTextArea();
        textArea.setBounds(50, 350, 600, 200);
        textArea.setEditable(false);
        frame.add(textArea);
        
        
        btnEliminarBebida.addActionListener(e -> {
    try {
        String nombre = JOptionPane.showInputDialog(frame, "Ingrese el nombre de la bebida a eliminar:");

        if (nombre != null && !nombre.isEmpty()) {
            if (eliminarBebida(nombre)) {
                textArea.setText("Bebida eliminada con éxito.");
            } else {
                textArea.setText("Error al eliminar la bebida. No se encontró.");
            }
        } else {
            textArea.setText("El nombre de la bebida es obligatorio.");
        }
    } catch (Exception ex) {
        textArea.setText("Error: " + ex.getMessage());
    }
});
        
        
        
        
        
        
        btnActualizarStock.addActionListener(e -> {
    try {
        String nombreBebida = JOptionPane.showInputDialog(frame, "Ingrese el nombre de la bebida:");
        String cantidadStr = JOptionPane.showInputDialog(frame, "Ingrese la cantidad de stock a agregar:");

        if (nombreBebida != null && !nombreBebida.isEmpty() && cantidadStr != null && !cantidadStr.isEmpty()) {
            int cantidad = Integer.parseInt(cantidadStr);
            
            if (actualizarStockPorNombre(nombreBebida, cantidad)) {
                textArea.setText("Stock actualizado correctamente.");
            } else {
                textArea.setText("Error al actualizar el stock.");
            }
        } else {
            textArea.setText("Todos los campos son obligatorios.");
        }
    } catch (NumberFormatException ex) {
        textArea.setText("Entrada inválida. Verifique los datos ingresados.");
    }
});
        
        
        
         
        
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
    String nombreProducto = JOptionPane.showInputDialog(frame, "Ingrese el nombre del Producto:");
    String cantidadStr = JOptionPane.showInputDialog(frame, "Ingrese Cantidad:");

    try {
        if (nombreProducto != null && !nombreProducto.isEmpty() && cantidadStr != null && !cantidadStr.isEmpty()) {
            int cantidad = Integer.parseInt(cantidadStr);

            // Buscar el ID del producto por nombre
            int idProducto = obtenerIdProductoPorNombre(nombreProducto);

            if (idProducto == -1) {
                textArea.setText("Producto no encontrado.");
            } else {
                // Actualizar stock y registrar la venta
                if (actualizarStock(idProducto, cantidad)) {
                    registrarVenta(idProducto, cantidad);
                    textArea.setText("Venta realizada con éxito.");
                } else {
                    textArea.setText("Stock insuficiente.");
                }
            }
        } else {
            textArea.setText("Todos los campos son obligatorios.");
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
            // Verificar que el nombre contenga "manaos"
            if (!nombre.toLowerCase().contains("manaos")) {
                textArea.setText("El nombre de la bebida debe contener la palabra 'manaos'.");
                return; // Salir si no cumple la condición
            }

            // Verificar si el producto ya existe
            if (productoExiste(nombre)) {
                textArea.setText("La bebida ya existe en el inventario.");
                return; // Salir si el producto ya existe
            }

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
    
public static boolean eliminarBebida(String nombre) {
    // Primero, eliminar las filas en detalles_venta que hacen referencia al producto
    String sqlDetallesVenta = "DELETE FROM detalles_venta WHERE id_producto = (SELECT id FROM productos WHERE nombre = ?)";
    
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement pstmtDetallesVenta = conn.prepareStatement(sqlDetallesVenta)) {
        
        pstmtDetallesVenta.setString(1, nombre);
        pstmtDetallesVenta.executeUpdate();  // Eliminar las referencias en detalles_venta
        
        // Ahora, eliminar el producto de la tabla productos
        String sqlProducto = "DELETE FROM productos WHERE nombre = ?";
        try (PreparedStatement pstmtProducto = conn.prepareStatement(sqlProducto)) {
            pstmtProducto.setString(1, nombre);
            int rowsAffected = pstmtProducto.executeUpdate();
            
            return rowsAffected > 0;
        }
    } catch (SQLException e) {
        System.out.println("Error al eliminar bebida: " + e.getMessage());
        return false;
    }
}
    
public static boolean productoExiste(String nombre) {
    String sql = "SELECT COUNT(*) FROM productos WHERE nombre = ?";
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, nombre);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next() && rs.getInt(1) > 0) {
            return true; // El producto ya existe
        }
    } catch (SQLException e) {
        System.out.println("Error al verificar si el producto existe: " + e.getMessage());
    }
    return false; // El producto no existe
}

public static boolean actualizarStockPorNombre(String nombreBebida, int cantidad) {
    String sql = "UPDATE productos SET stock = stock + ? WHERE nombre = ?";

    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        // Verificar los parámetros antes de ejecutarlos
        System.out.println("Actualizando stock de la bebida: " + nombreBebida + ", cantidad a agregar: " + cantidad);

        pstmt.setInt(1, cantidad); // Añadir la cantidad al stock
        pstmt.setString(2, nombreBebida); // Usar el nombre de la bebida para filtrar

        int rowsAffected = pstmt.executeUpdate(); // Ejecutar la actualización

        if (rowsAffected > 0) {
            System.out.println("Stock de la bebida '" + nombreBebida + "' actualizado correctamente.");
            return true;
        } else {
            System.out.println("Error: Bebida no encontrada o stock no actualizado.");
            return false;
        }

    } catch (SQLException e) {
        System.out.println("Error al actualizar el stock: " + e.getMessage());
        return false;
    }
}
public static int obtenerIdProductoPorNombre(String nombre) {
    String sql = "SELECT id FROM productos WHERE nombre = ?";
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, nombre);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            return rs.getInt("id");
        } else {
            return -1; // Producto no encontrado
        }

    } catch (SQLException e) {
        System.out.println("Error al obtener el ID del producto: " + e.getMessage());
        return -1; // Error al obtener el producto
    }
}

}
