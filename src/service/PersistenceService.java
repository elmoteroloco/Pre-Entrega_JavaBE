package service;

import model.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

public class PersistenceService {
    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";

    public void guardarInventarioCSV(List<Producto> inventario) {
        String ruta = "inventario.csv";
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(Paths.get(ruta), StandardCharsets.UTF_8))) {
            for (Producto p : inventario) {
                boolean esAlc = (p instanceof Bebida b) && b.isEsAlcoholica();
                writer.println(p.getId() + "," + p.getNombre() + "," + p.getPrecio() + "," + p.getStock() + "," + esAlc + "," + p.isActivo());
            }
        } catch (IOException e) {
            System.out.println(RED + "Hubo un error al guardar el archivo de inventario: " + e.getMessage() + RESET);
        }
    }

    public boolean cargarInventarioCSV(List<Producto> inventario) {
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
                    boolean esAlc = datos.length >= 5 && Boolean.parseBoolean(datos[4]);
                    boolean activo = datos.length != 6 || Boolean.parseBoolean(datos[5]);

                    Producto p = esAlc ? new Bebida(id, nombre, precio, stock, true, activo)
                                        : new Producto(id, nombre, precio, stock, activo);
                    inventario.add(p);
                    if (id > maxId) maxId = id;
                }
            }
            Producto.setContadorId(maxId);
            return true;
        } catch (IOException | NumberFormatException e) {
            System.out.println(RED + "Error al cargar el inventario: " + e.getMessage() + RESET);
            return false;
        }
    }

    public void guardarPedidosCSV(List<Pedido> historial) {
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(Paths.get("pedidos.csv"), StandardCharsets.UTF_8))) {
            for (Pedido p : historial) {
                writer.println(p.getIdPedido() + "," + p.getTotal() + "," + p.getTotalSinDescuento() + "," + p.getEstado());
            }
        } catch (IOException e) {
            System.out.println(RED + "Hubo un error al guardar el archivo de pedidos: " + e.getMessage() + RESET);
        }
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(Paths.get("pedidos_detalle.csv"), StandardCharsets.UTF_8))) {
            for (Pedido p : historial) {
                for (LineaPedido lp : p.getLineas()) {
                    writer.println(p.getIdPedido() + "," + lp.getProducto().getId() + "," + lp.getCantidad() + "," + lp.getPrecioAplicado());
                }
            }
        } catch (IOException e) {
            System.out.println(RED + "Hubo un error al guardar el archivo de detalles del pedido." + RESET);
        }
    }

    public boolean cargarPedidosCSV(List<Pedido> historial, List<Producto> inventario) {
        historial.clear();
        Pedido.setContadorPedido(0);
        Path ruta = Paths.get("pedidos.csv");
        if (!Files.exists(ruta)) return true;
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
                    historial.add(new Pedido(id, total, totalSinD, estado));
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

                    Pedido ped = historial.stream().filter(h -> h.getIdPedido() == idPed).findFirst().orElse(null);
                    Producto prod = inventario.stream().filter(p -> p.getId() == idProd).findFirst().orElse(null);

                    if (ped != null && prod != null) {
                        ped.getLineas().add(new LineaPedido(prod, cant, prec));
                    }
                }
            }
            return true;
        } catch (IOException | IllegalArgumentException e) {
            System.out.println(RED + "Error al cargar pedidos: " + e.getMessage() + RESET);
            return false;
        }
    }
}
