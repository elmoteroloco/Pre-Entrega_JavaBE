package model;

public class Cafeteria extends Producto {

    public Cafeteria(String nombre, double precio, int stock) {
        super(nombre, precio, stock);
    }

    public Cafeteria(int id, String nombre, double precio, int stock) {
        super(id, nombre, precio, stock);
    }

    public Cafeteria(int id, String nombre, double precio, int stock, boolean activo) {
        super(id, nombre, precio, stock, activo);
    }

    @Override
    public String getDetalleTipo() {
        return "Cafetería";
    }
}
