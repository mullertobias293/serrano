/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoserrano;
// Clase Producto

import java.util.ArrayList;
import java.util.List;

public class Producto {
    int idProducto;
    private String nombre;
    double precio;
    private int stock;

    public Producto(int idProducto, String nombre, double precio, int stock) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public boolean actualizarStock(int cantidad) {
        if (stock >= cantidad) {
            stock -= cantidad;
            return true;
        } else {
            return false;
        }
    }

    public String obtenerInfo() {
        return "ID: " + idProducto + ", Nombre: " + nombre + ", Precio: " + precio + ", Stock: " + stock;
    }
}
// Clase Cliente
