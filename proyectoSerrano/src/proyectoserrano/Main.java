/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoserrano;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Crear inventario y productos
        Inventario inventario = new Inventario();
        Producto p1 = new Producto(1, "Manaos Cola", 50.0, 100);
        Producto p2 = new Producto(2, "Manaos Naranja", 55.0, 50);
        inventario.agregarProducto(p1);
        inventario.agregarProducto(p2);

        // Crear cliente
        Cliente cliente = new Cliente(1, "Juan Perez", "juan@example.com");

        // Registrar venta
        List<Producto> productosVenta = new ArrayList<>();
        productosVenta.add(p1);
        Venta venta = new Venta(1, "2024-11-26", productosVenta);
        
        // Actualizar stock
        if (p1.actualizarStock(1)) {
            System.out.println("Venta realizada con éxito:");
            System.out.println(venta.generarFactura());
        } else {
            System.out.println("Stock insuficiente.");
        }
    }
}
/**
 *
 * @author Administrator
 */
