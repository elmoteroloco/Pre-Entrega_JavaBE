package model;

public class Bebida extends Producto {
    private boolean esAlcoholica;

    public Bebida(String nombre, double precio, int stock, boolean esAlcoholica) {
        super(nombre, precio, stock);
        this.esAlcoholica = esAlcoholica;
    }

    public Bebida(int id, String nombre, double precio, int stock, boolean esAlcoholica, boolean activo) {
        super(id, nombre, precio, stock, activo);
        this.esAlcoholica = esAlcoholica;
    }

    public Bebida(int id, String nombre, double precio, int stock, boolean esAlcoholica) {
        super(id, nombre, precio, stock);
        this.esAlcoholica = esAlcoholica;
    }

    public boolean isEsAlcoholica() {
        return esAlcoholica;
    }
}
