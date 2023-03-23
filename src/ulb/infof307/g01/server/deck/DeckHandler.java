package ulb.infof307.g01.server.deck;

import com.google.gson.Gson;
import org.apache.commons.httpclient.NameValuePair;
import spark.Request;
import spark.Response;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;
import ulb.infof307.g01.server.Handler;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import static spark.Spark.*;
import static ulb.infof307.g01.server.utils.JsonUtil.json;

public class DeckHandler implements Handler {
    private static final Logger logger = Logger.getLogger("DeckHandler");

    private final Map<String, String> successfulResponse = Map.of("success", "true");
    private final Map<String, String> failedResponse = Map.of("success", "false");

    private final DeckDAO deckDAO = DeckDAO.singleton();

    @Override
    public void init() {
        logger.info("Starting deck handler");
        path("/api", () -> {
            path("/deck", () -> {
                post("/save", this::saveDeck, json());
                get("/get", this::getDeck, json());
                delete("/delete", this::deleteDeck, json());
                get("/all", this::getAllDecks, json());
                get("/search", this::searchDecks, json());
            });
        });
        logger.info("Deck handler started");
    }

    private Deck bodyToDeck(Request req, Response res) {
        Map deckMap = new Gson().fromJson(req.body(), Map.class);
        return new Deck(deckMap);
    }

    private Map<String, String> saveDeck(Request req, Response res) {
        try{
            UUID userId = UUID.fromString(req.queryParams("user_id"));
            Deck deck = bodyToDeck(req, res);
            deckDAO.saveDeck(deck, userId);
            return successfulResponse;
        } catch (Exception e) {
            logger.warning("Failed to save deck: " + e.getMessage());
            return failedResponse;
        }
    }

    private Map<String, String> getDeck(Request req, Response res) {
        UUID userId = UUID.fromString(req.queryParams("user_id"));
        return null;
    }


    private Map<String, String> deleteDeck(Request req, Response res) {
        try {
            UUID userId = UUID.fromString(req.queryParams("user_id"));
            Deck deck = bodyToDeck(req, res);
            deckDAO.deleteDeck(deck, userId);
            return successfulResponse;
        } catch (Exception e) {
            logger.warning("Failed to delete deck: " + e.getMessage());
            return failedResponse;
        }
    }

    private Map<String, String> getAllDecks(Request req, Response res) {
        UUID userId = UUID.fromString(req.queryParams("user_id"));
        return null;
    }

    private List<String> searchDecks(Request req, Response res) {
        String userSearch = req.queryParams("user_search");
        return null;
    }
}
