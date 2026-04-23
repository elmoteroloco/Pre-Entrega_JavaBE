package model;

import java.util.Objects;

public class Producto {
    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";

    private static int contadorId = 0;
    protected int id;
    protected String nombre;
    protected double precio;
    protected int stock;
    protected boolean activo = true;

    public Producto(String nombre, double precio, int stock) {
        this.id = ++contadorId;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public Producto(int id, String nombre, double precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public Producto(int id, String nombre, double precio, int stock, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
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
            System.out.println(RED + "Error: El nombre del producto no puede estar vacio." + RESET);
            return;
        }
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        if (precio <= 0) {
            System.out.println(RED + "Error: El precio debe ser mayor a cero." + RESET);
            return;
        }
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            System.out.println(RED + "Error: El stock no puede ser negativo." + RESET);
            return;
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
