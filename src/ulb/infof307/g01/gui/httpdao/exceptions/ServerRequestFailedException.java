package ulb.infof307.g01.gui.httpdao.exceptions;

import java.io.IOException;

public class ServerRequestFailedException extends IOException {
    public ServerRequestFailedException(String message) {
        super(message);
    }
}
