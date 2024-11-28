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
// Clase Usuario
public class Usuario {
    private int idUsuario;
    private String nombre;
    private String rol;

    public Usuario(int idUsuario, String nombre, String rol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.rol = rol;
    }

    public boolean iniciarSesion(String nombre, String contrasena) {
        // Simulación de autenticación
        return nombre.equals(this.nombre);
    }
}