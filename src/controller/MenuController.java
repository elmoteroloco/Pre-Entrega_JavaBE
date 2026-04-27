package controller;

import model.*;
import service.*;
import view.ConsoleView;
import exceptions.*;
import java.util.*;

public class MenuController {
    private final ProductoService productoService;
    private final PedidoService pedidoService;
    private final PersistenceService persistence;
    private final ConsoleView view;
    private final Scanner scanner;
    private final ArrayList<Producto> inventario;
    private static final String TECLA_CANCELAR = "x";

    public static class CancelarException extends RuntimeException {}

    public MenuController(ProductoService productoService, PedidoService pedidoService, PersistenceService persistence, ConsoleView view) {
        this.productoService = productoService;
        this.pedidoService = pedidoService;
        this.persistence = persistence;
        this.view = view;
        this.scanner = new Scanner(System.in);
        this.inventario = new ArrayList<>();
    }

    public void iniciar() {
        persistence.cargarInventarioCSV(inventario);
        persistence.cargarPedidosCSV(pedidoService.getHistorialPedidos(), inventario);

        int opcion = -1;
        while (opcion != 0) {
            view.mostrarMenuPrincipal();
            try {
                opcion = leerInt("", 0);
                procesarOpcion(opcion);
            } catch (CancelarException e) {
                view.mostrarAviso("Operacion cancelada.");
            } catch (ValidacionProductoException e) {
                view.mostrarError(e.getMessage());
            } catch (Exception e) {
                view.mostrarError("Ocurrio un error inesperado.");
            }
        }
        scanner.close();
    }

    private void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> view.mostrarCatalogo(inventario);
            case 2 -> gestionarAltaProducto();
            case 3 -> gestionarBusqueda();
            case 4 -> gestionarModificacion();
            case 5 -> gestionarBajaProducto();
            case 6 -> gestionarVenta();
            case 7 -> view.mostrarHistorialPedidos(pedidoService.getHistorialPedidos());
            case 8 -> gestionarCambioEstado();
            case 9 -> {
                double[] datos = pedidoService.calcularDatosReporte();
                view.mostrarReporte(datos[0], datos[1], (int) datos[2]);
            }
            case 10 -> gestionarReactivacion();
        }
    }

    private void gestionarAltaProducto() {
        view.mostrarCatalogo(inventario);
        System.out.print("Nombre ('x' para cancelar): ");
        String nombre = scanner.nextLine();
        if (nombre.equalsIgnoreCase(TECLA_CANCELAR)) throw new CancelarException();
        double precio = leerDouble("Precio: ", 0.01);
        int stock = leerInt("Stock: ", 0);
        System.out.print("¿Es una bebida alcohólica? (s/n): ");
        boolean esAlc = scanner.nextLine().equalsIgnoreCase("s");
        productoService.agregarProducto(inventario, nombre, precio, stock, esAlc);
        persistence.guardarInventarioCSV(inventario);
        view.mostrarExito("Producto agregado correctamente.");
    }

    private void gestionarBusqueda() {
        view.mostrarCatalogo(inventario);
        System.out.print("Ingresar Nombre o ID para buscar: ");
        String crit = scanner.nextLine();
        Producto pBuscado = productoService.buscarProductoPorCriterio(inventario, crit);
        if (pBuscado != null) view.mostrarMensaje("Encontrado: " + pBuscado);
        else view.mostrarError("No se encontro el producto.");
    }

    private void gestionarModificacion() {
        view.mostrarCatalogo(inventario);
        int idMod = leerInt("ID a modificar: ", 1);
        System.out.print("Nuevo Nombre: ");
        String nNom = scanner.nextLine();
        double nPre = leerDouble("Nuevo Precio: ", 0.01);
        int nSto = leerInt("Nuevo Stock: ", 0);
        productoService.modificarProducto(inventario, idMod, nNom, nPre, nSto);
        persistence.guardarInventarioCSV(inventario);
        view.mostrarExito("Producto actualizado.");
    }

    private void gestionarBajaProducto() {
        view.mostrarCatalogo(inventario);
        int idElim = leerInt("ID a eliminar: ", 1);
        productoService.eliminarProducto(inventario, idElim);
        persistence.guardarInventarioCSV(inventario);
        view.mostrarExito("Producto desactivado.");
    }

    private void gestionarVenta() {
        view.mostrarCatalogo(inventario);
        Pedido nPed = new Pedido();
        while (true) {
            int idProd = leerInt("ID (0 para terminar): ", 0);
            if (idProd == 0) break;
            int cant = leerInt("Cantidad: ", 1);
            try {
                productoService.procesarItemVenta(inventario, nPed, idProd, cant);
            } catch (Exception e) {
                view.mostrarError(e.getMessage());
            }
        }
        if (!nPed.getLineas().isEmpty()) {
            pedidoService.agregarPedidoAlHistorial(nPed);
            persistence.guardarInventarioCSV(inventario);
            persistence.guardarPedidosCSV(pedidoService.getHistorialPedidos());
            view.mostrarExito("Pedido registrado. Total: $" + nPed.getTotal());
        }
    }

    private void gestionarCambioEstado() {
        view.mostrarHistorialPedidos(pedidoService.getHistorialPedidos());
        int idPed = leerInt("ID Pedido: ", 1);
        int est = leerInt("1.PENDIENTE 2.ENTREGADO 3.CANCELADO: ", 1);
        pedidoService.actualizarEstadoPedido(inventario, idPed, EstadoPedido.values()[est - 1]);
        persistence.guardarInventarioCSV(inventario);
        persistence.guardarPedidosCSV(pedidoService.getHistorialPedidos());
        view.mostrarExito("Estado actualizado.");
    }

    private void gestionarReactivacion() {
        boolean tieneInactivos = inventario.stream().anyMatch(p -> !p.isActivo());
        if (!tieneInactivos) {
            view.mostrarAviso("No hay productos inactivos registrados para reactivar.");
            return;
        }

        view.mostrarInactivos(inventario);
        int idReac = leerInt("ID a reactivar: ", 1);
        productoService.reactivarProducto(inventario, idReac);
        persistence.guardarInventarioCSV(inventario);
        view.mostrarExito("Producto reactivado.");
    }

    private int leerInt(String mensaje, int min) {
        while (true) {
            try {
                if (!mensaje.isEmpty()) System.out.print(mensaje);
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase(TECLA_CANCELAR)) throw new CancelarException();
                int val = Integer.parseInt(input);
                if (val < min) throw new IllegalArgumentException();
                return val;
            } catch (IllegalArgumentException e) {
                System.out.println("\u001B[31mError: Ingrese un numero valido mayor o igual a " + min + "\u001B[0m");
            }
        }
    }

    private double leerDouble(String mensaje, double min) {
        while (true) {
            try {
                System.out.print(mensaje);
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase(TECLA_CANCELAR)) throw new CancelarException();
                double val = Double.parseDouble(input);
                if (val < min) throw new IllegalArgumentException();
                return val;
            } catch (IllegalArgumentException e) {
                System.out.println("\u001B[31mError: Ingrese un valor numerico valido mayor o igual a " + min + "\u001B[0m");
            }
        }
    }
}
