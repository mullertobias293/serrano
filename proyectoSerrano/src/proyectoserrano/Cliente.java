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

public class Cliente {
    private int idCliente;
    private String nombre;
    private String email;

    public Cliente(int idCliente, String nombre, String email) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.email = email;
    }

    public String obtenerInfo() {
        return "ID Cliente: " + idCliente + ", Nombre: " + nombre + ", Email: " + email;
    }
}
