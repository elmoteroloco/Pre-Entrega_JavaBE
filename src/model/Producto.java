package model;

import exceptions.*;
import java.util.Objects;

public class Producto {
    private static int contadorId = 0;
    protected int id;
    protected String nombre;
    protected double precio;
    protected int stock;
    protected boolean activo = true;

    public Producto(String nombre, double precio, int stock) {
        this.id = ++contadorId;
        setNombre(nombre);
        setPrecio(precio);
        setStock(stock);
    }

    public Producto(int id, String nombre, double precio, int stock) {
        this.id = id;
        setNombre(nombre);
        setPrecio(precio);
        setStock(stock);
    }

    public Producto(int id, String nombre, double precio, int stock, boolean activo) {
        this.id = id;
        setNombre(nombre);
        setPrecio(precio);
        setStock(stock);
        this.activo = activo;
    }

    public static void setContadorId(int nuevoValor) {
        contadorId = nuevoValor;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new NombreInvalidoException("El nombre del producto no puede estar vacio.");
        }
        this.nombre = normalizarNombre(nombre);
    }

    private String normalizarNombre(String nombre) {
        String[] palabras = nombre.trim().toLowerCase().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String p : palabras) {
            if (!p.isEmpty()) {
                sb.append(Character.toUpperCase(p.charAt(0)))
                    .append(p.substring(1))
                    .append(" ");
            }
        }
        return sb.toString().trim();
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        if (precio <= 0) {
            throw new PrecioInvalidoException("El precio debe ser mayor a cero.");
        }
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new StockInvalidoException("El stock no puede ser negativo.");
        }
        this.stock = stock;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String toString() {
        return "ID: " + id + " | " + nombre + " | Precio: $" + precio + " | Stock: " + stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return id == producto.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
