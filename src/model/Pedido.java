package model;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private static int contadorPedido = 0;
    private int idPedido;
    private List<LineaPedido> lineas;
    private double total;
    private double totalSinDescuento;
    private EstadoPedido estado;

    public Pedido() {
        this.idPedido = ++contadorPedido;
        this.lineas = new ArrayList<>();
        this.total = 0;
        this.totalSinDescuento = 0;
        this.estado = EstadoPedido.PENDIENTE;
    }

    public Pedido(int id, double total, double totalSinDescuento) {
        this.idPedido = id;
        this.lineas = new ArrayList<>();
        this.total = total;
        this.totalSinDescuento = totalSinDescuento;
        this.estado = EstadoPedido.PENDIENTE;
    }

    public Pedido(int id, double total, double totalSinDescuento, EstadoPedido estado) {
        this.idPedido = id;
        this.lineas = new ArrayList<>();
        this.total = total;
        this.totalSinDescuento = totalSinDescuento;
        this.estado = estado;
    }

    public void agregarProducto(Producto producto, int cantidad, double precioAplicado) {
        this.lineas.add(new LineaPedido(producto, cantidad, precioAplicado));
        this.totalSinDescuento += (producto.getPrecio() * cantidad);
        this.total += (precioAplicado * cantidad);
    }

    public static void setContadorPedido(int nuevoValor) {
        contadorPedido = nuevoValor;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public double getTotal() {
        return total;
    }

    public double getTotalSinDescuento() {
        return totalSinDescuento;
    }

    public double getAhorroTotal() {
        return totalSinDescuento - total;
    }

    public List<LineaPedido> getLineas() {
        return lineas;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }
}
