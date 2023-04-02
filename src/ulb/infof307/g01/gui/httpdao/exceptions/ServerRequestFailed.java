package ulb.infof307.g01.gui.httpdao.exceptions;

import java.io.IOException;

public class ServerRequestFailed extends IOException {
    public ServerRequestFailed(String message) {
        super(message);
    }
}
