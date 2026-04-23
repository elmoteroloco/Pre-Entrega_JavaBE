package exceptions;

public class ValidacionProductoException extends RuntimeException {
    public ValidacionProductoException(String mensaje) {
        super(mensaje);
    }
}
