/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoserrano;

import java.util.List;

/**
 *
 * @author Administrator
 */
public class Venta {
    private int idVenta;
    private String fecha;
    private List<Producto> listaProductos;
    private double total;

    public Venta(int idVenta, String fecha, List<Producto> listaProductos) {
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.listaProductos = listaProductos;
        this.total = calcularTotal();
    }

    private double calcularTotal() {
        return listaProductos.stream().mapToDouble(p -> p.precio).sum();
    }

    public String generarFactura() {
        StringBuilder factura = new StringBuilder("Factura - ID Venta: " + idVenta + "\\n");
        for (Producto p : listaProductos) {
            factura.append(p.obtenerInfo()).append("\\n");
        }
        factura.append("Total: ").append(total);
        return factura.toString();
    }
}