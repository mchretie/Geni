package ulb.infof307.g01.exception;

public class NeverThrown extends RuntimeException {
    public NeverThrown() {
        super("This exception is never be thrown.");
    }
}
