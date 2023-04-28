package ulb.infof307.g01.gui.http.exceptions;

import java.io.IOException;

public class AuthenticationFailedException extends IOException {
    public AuthenticationFailedException(String message) {
        super(message);
    }
}
