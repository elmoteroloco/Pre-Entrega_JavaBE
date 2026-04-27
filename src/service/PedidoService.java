package service;

import model.*;
import exceptions.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoService {
    private ArrayList<Pedido> historialPedidos = new ArrayList<>();

    public ArrayList<Pedido> getHistorialPedidos() {
        return historialPedidos;
    }

    public void agregarPedidoAlHistorial(Pedido pedido) {
        historialPedidos.add(pedido);
    }

    public void actualizarEstadoPedido(List<Producto> inventario, int idPedido, EstadoPedido nuevoEstado) {
        Pedido pedidoEncontrado = historialPedidos.stream()
                .filter(p -> p.getIdPedido() == idPedido)
                .findFirst()
                .orElseThrow(() -> new ValidacionProductoException("No se encontro el pedido con ID " + idPedido));

        if (nuevoEstado == EstadoPedido.CANCELADO && pedidoEncontrado.getEstado() != EstadoPedido.CANCELADO) {
            for (LineaPedido lp : pedidoEncontrado.getLineas()) {
                lp.getProducto().setStock(lp.getProducto().getStock() + lp.getCantidad());
            }
        } else if (pedidoEncontrado.getEstado() == EstadoPedido.CANCELADO && nuevoEstado != EstadoPedido.CANCELADO) {
            throw new ValidacionProductoException("No se puede reactivar un pedido ya cancelado.");
        } else if (pedidoEncontrado.getEstado() == nuevoEstado) {
            throw new ValidacionProductoException("El pedido ya se encuentra en ese estado.");
        }
        pedidoEncontrado.setEstado(nuevoEstado);
    }

    public double[] calcularDatosReporte() {
        double recaudacionReal = 0;
        double pendienteCaja = 0;
        int totalProductosEntregados = 0;
        for (Pedido p : historialPedidos) {
            if (p.getEstado() == EstadoPedido.ENTREGADO) {
                recaudacionReal += p.getTotal();
                for (LineaPedido lp : p.getLineas()) {
                    totalProductosEntregados += lp.getCantidad();
                }
            } else if (p.getEstado() == EstadoPedido.PENDIENTE) {
                pendienteCaja += p.getTotal();
            }
        }
        return new double[]{recaudacionReal, pendienteCaja, (double) totalProductosEntregados};
    }
}
