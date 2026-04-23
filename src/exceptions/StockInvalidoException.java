package exceptions;

public class StockInvalidoException extends ValidacionProductoException {
    public StockInvalidoException(String mensaje) {
        super(mensaje);
    }
}
