package ulb.infof307.g01.server;
import ulb.infof307.g01.server.deck.DeckHandler;
import ulb.infof307.g01.server.user.GuestHandler;

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
