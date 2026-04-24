package model;

public class LineaPedido {
    private Producto producto;
    private int cantidad;
    private double precioAplicado;

    public LineaPedido(Producto producto, int cantidad, double precioAplicado) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioAplicado = precioAplicado;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioAplicado() {
        return precioAplicado;
    }
}
