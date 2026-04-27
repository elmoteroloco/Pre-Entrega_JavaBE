package main;

import service.*;
import view.ConsoleView;
import controller.MenuController;

public class Main {
    public static void main(String[] args) {
        ProductoService productoService = new ProductoService();
        PedidoService pedidoService = new PedidoService();
        PersistenceService persistence = new PersistenceService();
        ConsoleView view = new ConsoleView();

        MenuController controller = new MenuController(productoService, pedidoService, persistence, view);
        controller.iniciar();
    }
}
