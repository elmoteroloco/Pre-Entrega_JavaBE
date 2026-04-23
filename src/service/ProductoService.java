package service;

import exceptions.StockInsuficienteException;
import model.Producto;
import model.Bebida;
import model.EstadoPedido;
import model.LineaPedido;
import model.Pedido;
import java.util.ArrayList;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

public class ProductoService {
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RESET = "\u001B[0m";
    private ArrayList<Pedido> historialPedidos = new ArrayList<>();

    public ArrayList<Pedido> getHistorialPedidos() {
        return historialPedidos;
    }

    public void agregarPedidoAlHistorial(Pedido pedido) {
        historialPedidos.add(pedido);
    }

    public void agregarProducto(ArrayList<Producto> inventario, String nombre, double precio, int stock, boolean esAlc) {
        if (nombre == null || nombre.trim().isEmpty()) {
            System.out.println(RED + "Error: El nombre del producto no puede estar vacio." + RESET);
            return;
        }
        Producto existente = inventario.stream()
            .filter(p -> p.getNombre().equalsIgnoreCase(nombre))
            .findFirst()
            .orElse(null);
        if (existente != null) {
            if (existente.isActivo()) {
                System.out.println(RED + "Error: Ya existe un producto activo con el nombre '" + nombre + "'." + RESET);
            } else {
                System.out.println(YELLOW + "Aviso: Existe un producto inactivo con ese nombre (ID: " + existente.getId() + ").");
                System.out.println("Podes reactivarlo usando la opcion 10 del menu." + RESET);
            }
            return;
        }
        if (precio <= 0) {
            System.out.println(RED + "Error: El precio debe ser mayor a cero." + RESET);
            return;
        }
        if (stock < 0) {
            System.out.println(RED + "Error: El stock no puede ser negativo." + RESET);
            return;
        }
        Producto nuevo;
        if (esAlc) {
            nuevo = new Bebida(nombre, precio, stock, true);
        } else {
            nuevo = new Producto(nombre, precio, stock);
        }
        inventario.add(nuevo);
        System.out.println("Se agregó el producto '" + nombre + "'.");
    }

    public void listarProductos(ArrayList<Producto> inventario) {
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

    public void listarProductosInactivos(ArrayList<Producto> inventario) {
        List<Producto> inactivos = inventario.stream().filter(p -> !p.isActivo()).toList();
        if (inactivos.isEmpty()) {
            System.out.println("No hay productos inactivos.");
            return;
        }
        System.out.printf("%-5s | %-25s | %-12s | %-8s%n", "ID", "Nombre", "Precio", "Stock");
        System.out.println("------------------------------------------------------------------");
        for (Producto p : inactivos) {
            System.out.printf("%-5d | %-25s | $%-11.2f | %-8d%n", p.getId(), p.getNombre(), p.getPrecio(), p.getStock());
        }
    }

    public void buscarProducto(ArrayList<Producto> inventario, String criterio) {
        boolean encontrado = false;
        for (Producto p : inventario) {
            if (p.getNombre().equalsIgnoreCase(criterio) || String.valueOf(p.getId()).equals(criterio)) {
                if (p.isActivo()) {
                    System.out.println("Producto encontrado: " + p);
                } else {
                    System.out.println(RED + "Producto encontrado (INACTIVO): " + p + RESET);
                }
                encontrado = true;
            }
        }
        if (!encontrado) {
            System.out.println("No se encontro ningun producto con ese nombre o ID.");
        }
    }

    public Producto buscarProducto(List<Producto> inventario, int id) {
        for (Producto p : inventario) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public void eliminarProducto(ArrayList<Producto> inventario, int id) {
        Producto p = buscarProducto(inventario, id);
        if (p != null) {
            p.setActivo(false);
            System.out.println("El Producto con ID: " + id + " se marcó como inactivo.");
        } else {
            System.out.println("No se encontro el ID " + id);
        }
    }

    public void reactivarProducto(ArrayList<Producto> inventario, int id) {
        Producto p = buscarProducto(inventario, id);
        if (p != null && !p.isActivo()) {
            p.setActivo(true);
            System.out.println("El Producto '" + p.getNombre() + "' fue reactivado con éxito.");
        } else {
            System.out.println(RED + "No se encontró un producto inactivo con ese ID." + RESET);
        }
    }

    public void modificarProducto(ArrayList<Producto> inventario, int id, String nombre, double precio, int stock) {
        if (nombre == null || nombre.trim().isEmpty()) {
            System.out.println(RED + "Error: El nombre del producto no puede estar vacio." + RESET);
            return;
        }
        if (precio <= 0) {
            System.out.println(RED + "Error: El precio debe ser mayor a cero." + RESET);
            return;
        }
        if (stock < 0) {
            System.out.println(RED + "Error: El stock no puede ser negativo." + RESET);
            return;
        }
        for (Producto p : inventario) {
            if (p.getId() == id) {
                p.setNombre(nombre);
                p.setPrecio(precio);
                p.setStock(stock);
                System.out.println("Producto ID " + id + " actualizado correctamente.");
                return;
            }
        }
        System.out.println("No se encontró el producto con ID " + id);
    }

    public double procesarItemVenta(ArrayList<Producto> inventario, Pedido pedido, int id, int cantidad) throws StockInsuficienteException {
        for (Producto p : inventario) {
            if (p.getId() == id) {
                if (p.getStock() < cantidad) {
                    throw new StockInsuficienteException("Stock insuficiente de: " + p.getNombre());
                }
                double precioFinal = p.getPrecio();
                if (cantidad > 100) {
                    precioFinal *= 0.80;
                } else if (cantidad > 50) {
                    precioFinal *= 0.90;
                }
                p.setStock(p.getStock() - cantidad);
                pedido.agregarProducto(p, cantidad, precioFinal);
                System.out.printf("Agregado: %s | Cantidad: %d | Subtotal: $%.2f%n", p.getNombre(), cantidad, (precioFinal * cantidad));
                return precioFinal;
            }
        }
        System.out.println(RED + "El Producto con ID " + id + " no fue encontrado." + RESET);
        return 0;
    }

    public void guardarCSV(ArrayList<Producto> inventario) {
        String ruta = "inventario.csv";
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(Paths.get(ruta), StandardCharsets.UTF_8))) {
            for (Producto p : inventario) {
                boolean esAlc = false;
                if (p instanceof Bebida) {
                    Bebida b = (Bebida) p;
                    esAlc = b.isEsAlcoholica();
                }
                writer.println(p.getId() + "," + p.getNombre() + "," + p.getPrecio() + "," + p.getStock() + "," + esAlc + "," + p.isActivo());
            }
        } catch (IOException e) {
            System.out.println(RED + "Hubo un error al guardar el archivo: " + e.getMessage() + RESET);
        }
    }

    public boolean cargarCSV(ArrayList<Producto> inventario) {
        inventario.clear();
        Producto.setContadorId(0);
        Path ruta = Paths.get("inventario.csv");
        if (!Files.exists(ruta)) return true;
        try {
            List<String> lineas = Files.readAllLines(ruta, StandardCharsets.UTF_8);
            int maxId = 0;
            for (String linea : lineas) {
                String[] datos = linea.split(",");
                if (datos.length >= 4) {
                    int id = Integer.parseInt(datos[0]);
                    String nombre = datos[1];
                    double precio = Double.parseDouble(datos[2]);
                    int stock = Integer.parseInt(datos[3]);
                    boolean esAlc = false;
                    if (datos.length >= 5) {
                        esAlc = Boolean.parseBoolean(datos[4]);
                    }
                    boolean activo = true;
                    if (datos.length == 6) {
                        activo = Boolean.parseBoolean(datos[5]);
                    }
                    Producto p;
                    if (esAlc) {
                        p = new Bebida(id, nombre, precio, stock, true, activo);
                    } else {
                        p = new Producto(id, nombre, precio, stock, activo);
                    }
                    inventario.add(p);
                    if (id > maxId) maxId = id;
                }
            }
            Producto.setContadorId(maxId);
            return true;
        } catch (IOException e) {
            System.out.println(RED + "Hubo un error al cargar el archivo: " + e.getMessage() + RESET);
            return false;
        } catch (Exception e) {
            System.out.println(RED + "Hay un error en el formato de datos." + RESET);
            return false;
        }
    }

    public void listarPedidos() {
        if (historialPedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados.");
            return;
        }
        System.out.println("\n--- Historial de Pedidos ---");
        for (Pedido p : historialPedidos) {
            System.out.printf("Pedido ID: %d | Estado: %s | Total Final: $%.2f%n", p.getIdPedido(), p.getEstado(), p.getTotal());
            for (LineaPedido lp : p.getLineas()) {
                System.out.printf("   > %-20s x %-3d (Subt: $%.2f)%n",
                    lp.getProducto().getNombre(), lp.getCantidad(), (lp.getCantidad() * lp.getPrecioAplicado()));
            }
            System.out.println("---------------------------------------------------------------");
        }
    }

    public void guardarPedidosCSV() {
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(Paths.get("pedidos.csv"), StandardCharsets.UTF_8))) {
            for (Pedido p : historialPedidos) {
                writer.println(p.getIdPedido() + "," + p.getTotal() + "," + p.getTotalSinDescuento() + "," + p.getEstado());
            }
        } catch (IOException e) {
            System.out.println(RED + "Hubo un error al guardar en el archivo de pedidos: " + e.getMessage() + RESET);
        }
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(Paths.get("pedidos_detalle.csv"), StandardCharsets.UTF_8))) {
            for (Pedido p : historialPedidos) {
                for (LineaPedido lp : p.getLineas()) {
                    writer.println(p.getIdPedido() + "," + lp.getProducto().getId() + "," + lp.getCantidad() + "," + lp.getPrecioAplicado());
                }
            }
        } catch (IOException e) {
            System.out.println(RED + "Hubo un error al guardar en el archivo de detalles del pedido ." + RESET);
        }
    }

    public boolean cargarPedidosCSV(ArrayList<Producto> inventario) {
        historialPedidos.clear();
        Pedido.setContadorPedido(0);
        Path ruta = Paths.get("pedidos.csv");
        if (!Files.exists(ruta)) return true;
        if (inventario.isEmpty()) {
            System.out.println(YELLOW + "Aviso: El inventario esta vacio. Los pedidos se cargaran sin detalle de productos." + RESET);
        }

        try {
            List<String> lineas = Files.readAllLines(ruta, StandardCharsets.UTF_8);
            int maxId = 0;
            for (String linea : lineas) {
                String[] datos = linea.split(",");
                if (datos.length >= 3) {
                    int id = Integer.parseInt(datos[0]);
                    double total = Double.parseDouble(datos[1]);
                    double totalSinD = Double.parseDouble(datos[2]);
                    EstadoPedido estado = datos.length == 4 ? EstadoPedido.valueOf(datos[3]) : EstadoPedido.PENDIENTE;
                    historialPedidos.add(new Pedido(id, total, totalSinD, estado));
                    if (id > maxId) maxId = id;
                }
            }
            Pedido.setContadorPedido(maxId);

            Path rutaDetalle = Paths.get("pedidos_detalle.csv");
            if (Files.exists(rutaDetalle)) {
                List<String> lineasDetalle = Files.readAllLines(rutaDetalle, StandardCharsets.UTF_8);
                for (String ld : lineasDetalle) {
                    String[] d = ld.split(",");
                    int idPed = Integer.parseInt(d[0]);
                    int idProd = Integer.parseInt(d[1]);
                    int cant = Integer.parseInt(d[2]);
                    double prec = Double.parseDouble(d[3]);

                    Pedido ped = historialPedidos.stream()
                        .filter(h -> h.getIdPedido() == idPed)
                        .findFirst().orElse(null);
                    Producto prod = buscarProducto(inventario, idProd);

                    if (ped != null && prod != null) {
                        ped.getLineas().add(new LineaPedido(prod, cant, prec));
                    }
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error al cargar pedidos.");
            return false;
        }
    }

    public void actualizarEstadoPedido(ArrayList<Producto> inventario, int idPedido, EstadoPedido nuevoEstado) {
        for (Pedido p : historialPedidos) {
            if (p.getIdPedido() == idPedido) {
                if (nuevoEstado == EstadoPedido.CANCELADO && p.getEstado() != EstadoPedido.CANCELADO) {
                    for (LineaPedido lp : p.getLineas()) {
                        lp.getProducto().setStock(lp.getProducto().getStock() + lp.getCantidad());
                    }
                } else if (p.getEstado() == EstadoPedido.CANCELADO && nuevoEstado == EstadoPedido.ENTREGADO) {
                    System.out.println(RED + "No se puede reactivar un pedido ya cancelado (no tiene validacion de stock). Se debe realizar un nuevo pedido." + RESET);
                    return;
                } else if (p.getEstado() == nuevoEstado) {
                    System.out.println(YELLOW + "El pedido ya se encuentra en estado " + nuevoEstado + RESET);
                    return;
                }
                p.setEstado(nuevoEstado);
                System.out.println("Estado del pedido " + idPedido + " actualizado a " + nuevoEstado);
                return;
            }
        }
        System.out.println(RED + "No se encontro el pedido con ID " + idPedido + RESET);
    }

    public void mostrarReporteVentas() {
        double recaudacionReal = 0;
        double pendienteCaja = 0;
        int totalProductosEntregados = 0;
        for (Pedido p : historialPedidos) {
            if (p.getEstado() == EstadoPedido.ENTREGADO) {
                recaudacionReal += p.getTotal();
                for (LineaPedido lp : p.getLineas()) totalProductosEntregados += lp.getCantidad();
            } else if (p.getEstado() == EstadoPedido.PENDIENTE) {
                pendienteCaja += p.getTotal();
            }
        }
        System.out.println("\n" + YELLOW + "--- REPORTE DE VENTAS (CAJA) ---" + RESET);
        System.out.printf("Recaudacion Real (ENTREGADOS): $%.2f%n", recaudacionReal);
        System.out.printf("Ventas en Espera (PENDIENTES): $%.2f%n", pendienteCaja);
        System.out.println("Total de productos entregados: " + totalProductosEntregados);
    }
}
