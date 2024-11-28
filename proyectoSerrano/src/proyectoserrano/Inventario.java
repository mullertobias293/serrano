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

import java.util.ArrayList;
import java.util.List;

public class Inventario {
    private final List<Producto> listaProductos = new ArrayList<>();

    public void agregarProducto(Producto producto) {
        listaProductos.add(producto);
    }

    public Producto buscarProducto(int idProducto) {
        return listaProductos.stream().filter(p -> p.idProducto == idProducto).findFirst().orElse(null);
    }
}

