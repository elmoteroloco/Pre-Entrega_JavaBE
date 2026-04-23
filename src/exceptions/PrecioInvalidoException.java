package exceptions;

public class PrecioInvalidoException extends ValidacionProductoException {
    public PrecioInvalidoException(String mensaje) {
        super(mensaje);
    }
}
