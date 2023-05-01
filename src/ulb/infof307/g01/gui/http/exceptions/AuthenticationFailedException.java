package ulb.infof307.g01.gui.http.exceptions;

public class AuthenticationFailedException extends ServerCommunicationFailedException {
    public AuthenticationFailedException(String message) {
        super(message);
    }
}
