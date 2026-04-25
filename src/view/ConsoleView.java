package view;

import model.Producto;
import model.Bebida;
import model.Pedido;
import model.LineaPedido;
import java.util.List;

public class ConsoleView {
    public static final String RED = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";
    public static final String GREEN = "\u001B[32m";
    public static final String RESET = "\u001B[0m";

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarError(String mensaje) {
        System.out.println(RED + "Error: " + mensaje + RESET);
    }

    public void mostrarAviso(String mensaje) {
        System.out.println(YELLOW + mensaje + RESET);
    }

    public void mostrarExito(String mensaje) {
        System.out.println(GREEN + mensaje + RESET);
    }

    public void mostrarMenuPrincipal() {
        System.out.println("\n--- Menú de Inventario ---");
        System.out.println("1. Listar Productos");
        System.out.println("2. Agregar Producto");
        System.out.println("3. Buscar Producto");
        System.out.println("4. Modificar Producto");
        System.out.println("5. Eliminar Producto");
        System.out.println("6. Realizar Pedido (Venta)");
        System.out.println("7. Listar Pedidos Realizados");
        System.out.println("8. Actualizar Estado de Pedido");
        System.out.println("9. Reporte de Ventas");
        System.out.println("10. Reactivar Producto");
        System.out.println("0. Salir");
        System.out.print("Seleccioná una opción: ");
    }

    public void mostrarCatalogo(List<Producto> inventario) {
        if (inventario.isEmpty()) {
            System.out.println("El inventario esta vacio.");
            return;
        }
        System.out.printf("%-5s | %-25s | %-12s | %-8s%n", "ID", "Nombre", "Precio", "Stock");
        System.out.println("------------------------------------------------------------------");
        for (Producto p : inventario) {
            if (p.isActivo()) {
                String nombreDisplay = p.getNombre() + (p instanceof Bebida ? " (Alc)" : "");
                System.out.printf("%-5d | %-25s | $%-11.2f | %-8d%n", p.getId(), nombreDisplay, p.getPrecio(), p.getStock());
            }
        }
    }

    public void mostrarInactivos(List<Producto> inventario) {
        boolean hayInactivos = false;
        for (Producto p : inventario) {
            if (!p.isActivo()) {
                if (!hayInactivos) {
                    System.out.printf("%-5s | %-25s | %-12s | %-8s%n", "ID", "Nombre", "Precio", "Stock");
                    System.out.println("------------------------------------------------------------------");
                    hayInactivos = true;
                }
                System.out.printf("%-5d | %-25s | $%-11.2f | %-8d%n", p.getId(), p.getNombre(), p.getPrecio(), p.getStock());
            }
        }
        if (!hayInactivos) {
            System.out.println("No hay productos inactivos.");
        }
    }

    public void mostrarHistorialPedidos(List<Pedido> historial) {
        if (historial.isEmpty()) {
            System.out.println("No hay pedidos registrados.");
            return;
        }
        System.out.println("\n--- Historial de Pedidos ---");
        for (Pedido p : historial) {
            System.out.printf("Pedido ID: %d | Estado: %s | Total Final: $%.2f%n", p.getIdPedido(), p.getEstado(), p.getTotal());
            for (LineaPedido lp : p.getLineas()) {
                System.out.printf("   > %-20s x %-3d (Subt: $%.2f)%n",
                    lp.getProducto().getNombre(), lp.getCantidad(), (lp.getCantidad() * lp.getPrecioAplicado()));
            }
            System.out.println("---------------------------------------------------------------");
        }
    }

    public void mostrarReporte(double recaudacion, double pendiente, int totalEntregados) {
        System.out.println("\n" + YELLOW + "--- REPORTE DE VENTAS (CAJA) ---" + RESET);
        System.out.printf("Recaudacion Real (ENTREGADOS): $%.2f%n", recaudacion);
        System.out.printf("Ventas en Espera (PENDIENTES): $%.2f%n", pendiente);
        System.out.println("Total de productos entregados: " + totalEntregados);
    }
}
