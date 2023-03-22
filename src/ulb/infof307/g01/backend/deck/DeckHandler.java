package ulb.infof307.g01.backend.deck;

import spark.Request;
import spark.Response;
import ulb.infof307.g01.backend.Handler;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import static spark.Spark.*;
import static ulb.infof307.g01.backend.utils.JsonUtil.json;

public class DeckHandler implements Handler {
    private static final Logger logger = Logger.getLogger("DeckHandler");

    @Override
    public void init() {
        logger.info("Starting deck handler");
        path("/api", () -> {
            path("/deck", () -> {
                get(":user_id", this::getDeck, json());
                post(":user_id", this::saveDeck, json());
                delete(":deck_id", this::deleteDeck, json());
                get("/all/:user_id", this::getAllDecks, json());
                get("/search/:user_search", this::searchDecks, json());
            });
        });
        logger.info("Deck handler started");
    }

    private Map<String, String> getDeck(Request req, Response res) {
        UUID userId = UUID.fromString(req.params(":user_id"));
        return null;
    }

    private Map<String, String> saveDeck(Request req, Response res) {
        UUID userId = UUID.fromString(req.params(":user_id"));
        return null;
    }

    private Map<String, String> deleteDeck(Request req, Response res) {
        UUID userId = UUID.fromString(req.params(":user_id"));
        return null;
    }

    private Map<String, String> getAllDecks(Request req, Response res) {
        UUID userId = UUID.fromString(req.params(":user_id"));
        return null;
    }

    private List<String> searchDecks(Request req, Response res){
        String userSearch = req.params(":user_search");
        return null;
    }
}
