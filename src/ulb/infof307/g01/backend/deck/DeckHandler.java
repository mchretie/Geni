package ulb.infof307.g01.backend.deck;

import spark.Request;
import spark.Response;
import ulb.infof307.g01.backend.Handler;

import java.util.logging.Logger;

import static spark.Spark.*;

public class DeckHandler implements Handler {
    private static final Logger logger = Logger.getLogger("DeckHandler");

    @Override
    public void init() {
        logger.info("Starting deck handler");
        path("/api", () -> {
            get("/deck", this::getDeck);
        });
        logger.info("Deck handler started");
    }

    private String getDeck(Request req, Response res) {
        return "You have reached the deck handler";
    }
}
