package main;

import service.*;
import view.ConsoleView;
import controller.MenuController;

public class Main {
    public static void main(String[] args) {
        ProductoService service = new ProductoService();
        PersistenceService persistence = new PersistenceService();
        ConsoleView view = new ConsoleView();

        MenuController controller = new MenuController(service, persistence, view);
        controller.iniciar();
    }
}
