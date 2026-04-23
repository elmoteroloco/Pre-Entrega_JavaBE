package main;

import model.*;
import service.*;
import view.ConsoleView;
import exceptions.*;
import java.util.*;

public class Main {
    private static class CancelarException extends RuntimeException {}
    private static final String TECLA_CANCELAR = "x";

    private static int leerInt(Scanner sc, String mensaje, int min) {
        while (true) {
            try {
                if (!mensaje.isEmpty()) System.out.print(mensaje);
                String input = sc.nextLine();
                if (input.equalsIgnoreCase(TECLA_CANCELAR)) throw new CancelarException();
                int val = Integer.parseInt(input);
                if (val < min) throw new IllegalArgumentException();
                return val;
            } catch (Exception e) {
                System.out.println("\u001B[31mError: Ingrese un numero valido mayor o igual a " + min + "\u001B[0m");
            }
        }
    }

    private static double leerDouble(Scanner sc, String mensaje, double min) {
        while (true) {
            try {
                System.out.print(mensaje);
                String input = sc.nextLine();
                if (input.equalsIgnoreCase(TECLA_CANCELAR)) throw new CancelarException();
                double val = Double.parseDouble(input);
                if (val < min) throw new IllegalArgumentException();
                return val;
            } catch (Exception e) {
                System.out.println("\u001B[31mError: Ingrese un valor numerico valido mayor o igual a " + min + "\u001B[0m");
            }
        }
    }

    public static void main(String[] args) {
        ArrayList<Producto> inventario = new ArrayList<>();
        ProductoService service = new ProductoService();
        PersistenceService persistence = new PersistenceService();
        ConsoleView view = new ConsoleView();

        persistence.cargarInventarioCSV(inventario);
        persistence.cargarPedidosCSV(service.getHistorialPedidos(), inventario);

        Scanner scanner = new Scanner(System.in);
        int opcion = -1;
        while (opcion != 0) {
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
            try {
                opcion = leerInt(scanner, "", 0);
                switch (opcion) {
                    case 1:
                        view.mostrarCatalogo(inventario);
                        break;
                    case 2:
                        System.out.print("Nombre ('x' para cancelar): ");
                        String nombre = scanner.nextLine();
                        if (nombre.equalsIgnoreCase(TECLA_CANCELAR)) throw new CancelarException();
                        double precio = leerDouble(scanner, "Precio: ", 0.01);
                        int stock = leerInt(scanner, "Stock: ", 0);
                        System.out.print("¿Es una bebida alcohólica? (s/n): ");
                        boolean esAlc = scanner.nextLine().equalsIgnoreCase("s");
                        service.agregarProducto(inventario, nombre, precio, stock, esAlc);
                        persistence.guardarInventarioCSV(inventario);
                        view.mostrarExito("Producto agregado correctamente.");
                        break;
                    case 3:
                        System.out.print("Ingresar Nombre o ID para buscar: ");
                        String crit = scanner.nextLine();
                        Producto pBuscado = service.buscarProductoPorCriterio(inventario, crit);
                        if (pBuscado != null) view.mostrarMensaje("Encontrado: " + pBuscado);
                        else view.mostrarError("No se encontro el producto.");
                        break;
                    case 4:
                        int idMod = leerInt(scanner, "ID a modificar: ", 1);
                        String nNom = scanner.nextLine();
                        double nPre = leerDouble(scanner, "Nuevo Precio: ", 0.01);
                        int nSto = leerInt(scanner, "Nuevo Stock: ", 0);
                        service.modificarProducto(inventario, idMod, nNom, nPre, nSto);
                        persistence.guardarInventarioCSV(inventario);
                        view.mostrarExito("Producto actualizado.");
                        break;
                    case 5:
                        int idElim = leerInt(scanner, "ID a eliminar: ", 1);
                        service.eliminarProducto(inventario, idElim);
                        persistence.guardarInventarioCSV(inventario);
                        view.mostrarExito("Producto desactivado.");
                        break;
                    case 6:
                        Pedido nPed = new Pedido();
                        boolean cargando = true;
                        while (cargando) {
                            int idProd = leerInt(scanner, "ID (0 para terminar): ", 0);
                            if (idProd == 0) break;
                            int cant = leerInt(scanner, "Cantidad: ", 1);
                            try { service.procesarItemVenta(inventario, nPed, idProd, cant); }
                            catch (Exception e) { view.mostrarError(e.getMessage()); }
                        }
                        if (!nPed.getLineas().isEmpty()) {
                            service.agregarPedidoAlHistorial(nPed);
                            persistence.guardarInventarioCSV(inventario);
                            persistence.guardarPedidosCSV(service.getHistorialPedidos());
                            view.mostrarExito("Pedido registrado. Total: $" + nPed.getTotal());
                        }
                        break;
                    case 7:
                        view.mostrarHistorialPedidos(service.getHistorialPedidos());
                        break;
                    case 8:
                        int idPed = leerInt(scanner, "ID Pedido: ", 1);
                        int est = leerInt(scanner, "1.PENDIENTE 2.ENTREGADO 3.CANCELADO: ", 1);
                        service.actualizarEstadoPedido(inventario, idPed, EstadoPedido.values()[est-1]);
                        persistence.guardarInventarioCSV(inventario);
                        persistence.guardarPedidosCSV(service.getHistorialPedidos());
                        view.mostrarExito("Estado actualizado.");
                        break;
                    case 9:
                        double[] datos = service.calcularDatosReporte();
                        view.mostrarReporte(datos[0], datos[1], (int)datos[2]);
                        break;
                    case 10:
                        view.mostrarInactivos(inventario);
                        int idReac = leerInt(scanner, "ID a reactivar: ", 1);
                        service.reactivarProducto(inventario, idReac);
                        persistence.guardarInventarioCSV(inventario);
                        view.mostrarExito("Producto reactivado.");
                        break;
                }
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
}
