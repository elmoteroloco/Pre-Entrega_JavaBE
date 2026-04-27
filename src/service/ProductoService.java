package service;

import exceptions.*;
import model.Producto;
import model.Bebida;
import model.Pedido;
import java.util.List;

public class ProductoService {
    public void agregarProducto(List<Producto> inventario, String nombre, double precio, int stock, boolean esAlc) {
        for (Producto p : inventario) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                if (p.isActivo()) throw new ValidacionProductoException("Ya existe un producto activo con ese nombre.");
                else throw new ValidacionProductoException("Existe un producto inactivo con ese nombre (ID: " + p.getId() + "). Reactivalo desde el menu.");
            }
        }
        Producto nuevo = esAlc ? new Bebida(nombre, precio, stock, true) : new Producto(nombre, precio, stock);
        inventario.add(nuevo);
    }

    public Producto buscarProductoPorCriterio(List<Producto> inventario, String criterio) {
        for (Producto p : inventario) {
            if (p.getNombre().equalsIgnoreCase(criterio) || String.valueOf(p.getId()).equals(criterio)) {
                return p;
            }
        }
        return null;
    }

    public Producto buscarProducto(List<Producto> inventario, int id) {
        for (Producto p : inventario) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public void eliminarProducto(List<Producto> inventario, int id) {
        Producto p = buscarProducto(inventario, id);
        if (p == null) throw new ValidacionProductoException("No se encontro el ID " + id);
        p.setActivo(false);
    }

    public void reactivarProducto(List<Producto> inventario, int id) {
        Producto p = buscarProducto(inventario, id);
        if (p == null || p.isActivo()) throw new ValidacionProductoException("No se encontro un producto inactivo con ese ID.");
        p.setActivo(true);
    }

    public void modificarProducto(List<Producto> inventario, int id, String nombre, double precio, int stock) {
        Producto p = buscarProducto(inventario, id);
        if (p == null) throw new ValidacionProductoException("No se encontro el producto con ID " + id);
        p.setNombre(nombre);
        p.setPrecio(precio);
        p.setStock(stock);
    }

    public double procesarItemVenta(List<Producto> inventario, Pedido pedido, int id, int cantidad) throws StockInsuficienteException {
        Producto p = buscarProducto(inventario, id);
        if (p == null) throw new StockInsuficienteException("Producto no encontrado.");
        if (!p.isActivo()) throw new StockInsuficienteException("El producto está inactivo.");
        if (p.getStock() < cantidad) throw new StockInsuficienteException("Stock insuficiente para " + p.getNombre());

        double precioFinal = p.getPrecio();
        if (cantidad > 100) precioFinal *= 0.8;
        else if (cantidad > 50) precioFinal *= 0.9;

        p.setStock(p.getStock() - cantidad);
        pedido.agregarProducto(p, cantidad, precioFinal);
        return precioFinal;
    }
}
