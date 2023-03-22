package ulb.infof307.g01.backend;
import java.util.logging.Logger;

import static spark.Spark.*;

public class Server {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger("Server");
        logger.info("Starting server");
        port(8080);
        get("/hello", (req, res) -> "Hello World");

    }
}
