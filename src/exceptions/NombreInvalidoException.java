package exceptions;

public class NombreInvalidoException extends ValidacionProductoException {
    public NombreInvalidoException(String mensaje) {
        super(mensaje);
    }
}
