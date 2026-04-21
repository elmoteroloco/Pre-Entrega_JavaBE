package main;

import model.Producto;
import model.Pedido;
import model.EstadoPedido;
import service.ProductoService;
import exceptions.StockInsuficienteException;
import java.util.*;

public class Main {
    private static class CancelarException extends RuntimeException {}
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RESET = "\u001B[0m";
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
            } catch (NumberFormatException e) {
                System.out.println(RED + "Error: Ingrese un numero entero valido." + RESET);
            } catch (IllegalArgumentException e) {
                System.out.println(RED + "Error: El valor debe ser mayor o igual a " + min + RESET);
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
            } catch (NumberFormatException e) {
                System.out.println(RED + "Error: Ingrese un valor numerico valido (ej: 100.50)." + RESET);
            } catch (IllegalArgumentException e) {
                System.out.println(RED + "Error: El valor debe ser mayor o igual a " + min + RESET);
            }
        }
    }

    public static void main(String[] args) {
        ArrayList<Producto> inventario = new ArrayList<>();
        ProductoService service = new ProductoService();
        service.cargarCSV(inventario);
        service.cargarPedidosCSV(inventario);
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
            System.out.println("10. Reactivar Producto (Baja Logica)");
            System.out.println("0. Salir");
            System.out.print("Seleccioná una opción: ");
            try {
                opcion = leerInt(scanner, "", 0);
                switch (opcion) {
                    case 1:
                        System.out.println("\n--- Listado ---");
                        service.listarProductos(inventario);
                        break;
                    case 2:
                        service.listarProductos(inventario);
                        System.out.println("\n" + YELLOW + "=== INGRESO DE NUEVO PRODUCTO ===" + RESET);
                        System.out.print("Nombre (o 'x' para cancelar): ");
                        String nombre = scanner.nextLine();
                        if (nombre.equalsIgnoreCase(TECLA_CANCELAR)) throw new CancelarException();
                        double precio = leerDouble(scanner, "Precio: ", 0.01);
                        int stock = leerInt(scanner, "Stock: ", 0);
                        System.out.print("¿Es una bebida alcohólica? (s/n): ");
                        boolean esAlc = scanner.nextLine().equalsIgnoreCase("s");
                        service.agregarProducto(inventario, nombre, precio, stock, esAlc);
                        service.guardarCSV(inventario);
                        break;
                    case 3:
                        System.out.print("Nombre o ID a buscar: ");
                        String criterio = scanner.nextLine();
                        service.buscarProducto(inventario, criterio);
                        break;
                    case 4:
                        service.listarProductos(inventario);
                        System.out.println("\n" + YELLOW + "=== MODIFICACION DE PRODUCTO ===" + RESET);
                        int idMod = leerInt(scanner, "Ingrese el ID del producto a modificar (o 'x' para cancelar): ", 1);
                        Producto pMod = service.buscarProducto(inventario, idMod);
                        if (pMod != null) {
                            System.out.println("Valores actuales: " + pMod);
                            System.out.print("Nuevo Nombre (o 'x' para cancelar): ");
                            String nNom = scanner.nextLine();
                            if (nNom.equalsIgnoreCase(TECLA_CANCELAR)) throw new CancelarException();
                            double nPre = leerDouble(scanner, "Nuevo Precio: ", 0.01);
                            int nSto = leerInt(scanner, "Nuevo Stock: ", 0);
                            service.modificarProducto(inventario, idMod, nNom, nPre, nSto);
                            service.guardarCSV(inventario);
                        } else {
                            System.out.println(RED + "No se encontró el producto." + RESET);
                        }
                        break;
                    case 5:
                        service.listarProductos(inventario);
                        System.out.println("\n" + YELLOW + "=== ELIMINACION DE PRODUCTO ===" + RESET);
                        int idEliminar = leerInt(scanner, "ID a eliminar (o 'x' para cancelar): ", 1);
                        System.out.print("¿Estás seguro que deseas eliminar el producto? (s/n): ");
                        if (scanner.nextLine().equalsIgnoreCase("s")) {
                            service.eliminarProducto(inventario, idEliminar);
                            service.guardarCSV(inventario);
                        } else {
                            System.out.println("Eliminación cancelada.");
                        }
                        break;
                    case 6:
                        service.listarProductos(inventario);
                        System.out.println("\n" + YELLOW + "=== PROCESO DE VENTA ===" + RESET);
                        Pedido nuevoPedido = new Pedido();
                        boolean cargando = true;
                        while (cargando) {
                            int idProd = leerInt(scanner, "ID del producto (0 para finalizar, 'x' para cancelar): ", 0);
                            if (idProd == 0) break;
                            int cant = leerInt(scanner, "Cantidad: ", 1);
                            try {
                                service.procesarItemVenta(inventario, nuevoPedido, idProd, cant);
                            } catch (StockInsuficienteException e) {
                                System.out.println(RED + "Error de Stock: " + e.getMessage() + RESET);
                            }
                            System.out.print("¿Desea agregar otro producto al pedido? (s/n): ");
                            if (!scanner.nextLine().equalsIgnoreCase("s")) {
                                cargando = false;
                            }
                        }
                        if (!nuevoPedido.getLineas().isEmpty()) {
                            System.out.println("\n--- RESUMEN DEL PEDIDO ---");
                            System.out.printf("Importe Bruto: $%.2f%n", nuevoPedido.getTotalSinDescuento());
                            System.out.printf("Descuentos aplicados: -$%.2f%n", nuevoPedido.getAhorroTotal());
                            System.out.println("--------------------------");
                            System.out.printf("TOTAL FINAL: $%.2f%n", nuevoPedido.getTotal());
                            service.agregarPedidoAlHistorial(nuevoPedido);
                            service.guardarCSV(inventario);
                            service.guardarPedidosCSV();
                        } else {
                            System.out.println("\nPedido cancelado (no se agregaron productos).");
                        }
                        break;
                    case 7:
                        service.listarPedidos();
                        break;
                    case 8:
                        service.listarPedidos();
                        System.out.println("\n" + YELLOW + "=== ACTUALIZAR ESTADO DE PEDIDO ===" + RESET);
                        int idPed = leerInt(scanner, "ID del pedido a modificar (o 'x' para cancelar): ", 1);
                        System.out.println("Seleccione el nuevo estado:");
                        System.out.println("1. PENDIENTE");
                        System.out.println("2. ENTREGADO");
                        System.out.println("3. CANCELADO");
                        int estOpcion = leerInt(scanner, "Opcion: ", 1);
                        EstadoPedido nuevoEstado = EstadoPedido.values()[estOpcion - 1];
                        service.actualizarEstadoPedido(inventario, idPed, nuevoEstado);
                        service.guardarCSV(inventario);
                        service.guardarPedidosCSV();
                        break;
                    case 9:
                        service.mostrarReporteVentas();
                        break;
                    case 10:
                        System.out.println("\n" + YELLOW + "=== PRODUCTOS INACTIVOS ===" + RESET);
                        service.listarProductosInactivos(inventario);
                        int idReactivar = leerInt(scanner, "Ingrese ID a reactivar (o 'x' para cancelar): ", 1);
                        service.reactivarProducto(inventario, idReactivar);
                        service.guardarCSV(inventario);
                        break;
                    case 0:
                        System.out.println("Saliendo...");
                        break;
                    default:
                        System.out.println(RED + "Opcion no valida." + RESET);
                }
            } catch (CancelarException e) {
                System.out.println("\n" + YELLOW + "[!] Operacion cancelada por el usuario. Volviendo al menu..." + RESET);
                opcion = -1;
            } catch (Exception e) {
                System.out.println(RED + "Error: Ingreso un dato invalido." + RESET);
            }
        }
        scanner.close();
    }
}
