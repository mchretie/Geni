package ulb.infof307.g01.backend;
import ulb.infof307.g01.backend.deck.DeckHandler;
import ulb.infof307.g01.backend.user.GuestHandler;

import java.util.logging.Logger;

import static spark.Spark.*;

public class Server {
    static int serverPort = 8080;
    public static void main(String[] args) {
        Logger logger = Logger.getLogger("Server");
        logger.info("Starting server");
        port(serverPort);
        get("/", (req, res) -> "You have reached the server");
        launchHandlers();
        logger.info("Server started on port " + serverPort);
    }

    public static void launchHandlers() {
        new DeckHandler().init();
        new GuestHandler().init();
    }
}
