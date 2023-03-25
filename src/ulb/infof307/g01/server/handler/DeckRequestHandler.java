package ulb.infof307.g01.server.handler;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.server.database.dao.DeckDAO;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static spark.Spark.*;

public class DeckRequestHandler extends Handler {

    private final Map<String, String> successfulResponse
            = Map.of("success", "true");

    private final Map<String, String> failedResponse
            = Map.of("success", "false");

    private final DeckDAO deckDAO = DeckDAO.singleton();

    @Override
    public void init() {
        logger.info("Starting deck handler");
        path("/api", () -> {
            path("/deck", () -> {
                get("/get", this::getDeck, toJson());
                post("/save", this::saveDeck, toJson());
                delete("/delete", this::deleteDeck, toJson());
                get("/all", this::getAllDecks, toJson());
                get("/search", this::searchDecks, toJson());
            });
        });

        before("/api/deck/*", (req, res) -> {
            logger.info("Received request: "
                    + req.requestMethod()
                    + " "
                    + req.pathInfo()
                    + " "
                    + req.queryParams());
        });

        after("/api/deck/*", (req, res) -> {
            logger.info("Sent response code: " + res.status());
        });

        logger.info("Deck handler started");
    }

    private Map<String, String> saveDeck(Request req, Response res) {
        try {
            UUID userId = UUID.fromString(req.queryParams("user_id"));
            Deck deck = new Gson().fromJson(req.body(), Deck.class);
            deckDAO.saveDeck(deck, userId);
            return successfulResponse;

        } catch (Exception e) {
            logger.warning("Failed to save deck: " + e.getMessage());
            return failedResponse;
        }
    }

    private String getDeck(Request req, Response res) {
        UUID userId = UUID.fromString(req.queryParams("userid"));
        return null;
    }


    private Map<String, String> deleteDeck(Request req, Response res) {
        try {
            UUID userId = UUID.fromString(req.queryParams("userid"));
            Deck deck = new Gson().fromJson(req.body(), Deck.class);
            deckDAO.deleteDeck(deck, userId);
            return successfulResponse;

        } catch (Exception e) {
            logger.warning("Failed to delete deck: " + e.getMessage());
            return failedResponse;
        }
    }

    private List<Deck> getAllDecks(Request req, Response res) {
        try {
            UUID userId = UUID.fromString(req.queryParams("userid"));
            return deckDAO.getAllDecks();

        } catch (Exception e) {
            logger.warning("Failed to get all decks: " + e.getMessage());
            return null;
        }

    }

    private List<Deck> searchDecks(Request req, Response res) {
        try {
            String userSearch = req.queryParams("name");
            return deckDAO.searchDecks(userSearch);

        } catch (Exception e) {
            logger.warning("Failed to search decks: " + e.getMessage());
            return null;
        }
    }
}
