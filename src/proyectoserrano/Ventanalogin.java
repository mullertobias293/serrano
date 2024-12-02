package proyectoserrano;

import java.awt.Color;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Ventanalogin {
    private static final String URL = "jdbc:mysql://b7bqlyp9wflopvdwklxn-mysql.services.clever-cloud.com:3306/b7bqlyp9wflopvdwklxn";
    private static final String USER = "u3kvhd5surotasij";
    private static final String PASSWORD = "98XeROAFkzzXYvQKhEor";

    public static void main(String[] args) {
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.getContentPane().setBackground(Color.RED);
        frame.setLayout(null);

        // Cargar la imagen de fondo
        ImageIcon icon = new ImageIcon(VentanaPrincipal.class.getResource("/images/logo.png")); // Asegúrate de que la ruta sea correcta
        Image scaledImage = icon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImage);
        JLabel labelImagen = new JLabel(resizedIcon);
        labelImagen.setBounds(135, 10, 300, 175);
        frame.add(labelImagen);

        // Fuente para negrita
        Font fontBold = new Font("Arial", Font.BOLD, 14);

        // Componentes del formulario con texto en negrita
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(fontBold);  // Aplica la fuente en negrita
        lblUsuario.setBounds(150, 200, 100, 30);
        frame.add(lblUsuario);

        JTextField txtUsuario = new JTextField();
        txtUsuario.setBounds(250, 200, 150, 30);
        frame.add(txtUsuario);

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(fontBold);  // Aplica la fuente en negrita
        lblPassword.setBounds(150, 250, 100, 30);
        frame.add(lblPassword);

        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setBounds(250, 250, 150, 30);
        frame.add(txtPassword);

        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBounds(250, 300, 150, 30);
        frame.add(btnLogin);

        // Botón "Ayuda"
        JButton btnAyuda = new JButton("Ayuda");
        btnAyuda.setBounds(250, 350, 150, 30);  // Posiciona el botón debajo del botón de login
        frame.add(btnAyuda);

        JLabel lblMensaje = new JLabel("");
        lblMensaje.setBounds(50, 300, 300, 30);
        frame.add(lblMensaje);

        btnLogin.addActionListener(e -> {
            String usuario = txtUsuario.getText();
            String password = new String(txtPassword.getPassword());

            if (autenticarUsuario(usuario, password)) {
                lblMensaje.setText("Inicio de sesión exitoso");
                frame.dispose();
                VentanaPrincipal.main(null); // Carga la ventana principal
            } else {
                lblMensaje.setText("Usuario o contraseña incorrectos");
            }
        });

        // Acción para el botón "Ayuda"
        btnAyuda.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Ingresa tu usuario y contraseña para iniciar sesión.\n" +
                    "Si no tienes cuenta, por favor contacta al gerente de manaos.", "Ayuda", JOptionPane.INFORMATION_MESSAGE);
        });

        frame.setVisible(true);
    }

    private static boolean autenticarUsuario(String usuario, String password) {
        String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Si encuentra un usuario, retorna true

        } catch (SQLException e) {
            System.out.println("Error al autenticar usuario: " + e.getMessage());
            return false;
        }
    }
}



