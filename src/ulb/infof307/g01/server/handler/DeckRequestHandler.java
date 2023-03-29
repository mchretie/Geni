package ulb.infof307.g01.server.handler;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.server.database.dao.DeckDAO;
import ulb.infof307.g01.server.service.JWTService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static spark.Spark.*;

public class DeckRequestHandler extends Handler {

    private final DeckDAO deckDAO = DeckDAO.singleton();

    @Override
    public void init() {
        logStart();

        path("/api", () -> {
            path("/deck", () -> {
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

        logStarted();
    }

    private Map<String, String> saveDeck(Request req, Response res) {
        try {
            String username = usernameFromToken(req);
            Deck deck = new Gson().fromJson(req.body(), Deck.class);

            deckDAO.saveDeckToUsername(deck, username);
            return successfulResponse;

        } catch (Exception e) {
            String message = "Failed to save deck: " + e.getMessage();
            logger.warning(message);
            halt(500, message);

            return failedResponse;
        }
    }


    private Map<String, String> deleteDeck(Request req, Response res) {
        try {
            String username = usernameFromToken(req);
            UUID deckId = UUID.fromString(req.queryParams("deck_id"));

            deckDAO.deleteDeckFromUsername(deckId, username);
            return successfulResponse;

        } catch (Exception e) {
            String message = "Failed to delete deck: " + e.getMessage();
            logger.warning(message);
            halt(500, message);

            return failedResponse;
        }
    }

    private List<Deck> getAllDecks(Request req, Response res) {
        try {
            String username = usernameFromToken(req);

            return deckDAO.getAllDecksFromUsername(username);

        } catch (Exception e) {
            String message = "Failed to get all decks: " + e.getMessage();
            logger.warning(message);
            halt(500, message);

            return new ArrayList<>();
        }
    }

    private List<Deck> searchDecks(Request req, Response res) {
        try {
            String username = usernameFromToken(req);
            String userSearch = req.queryParams("search");

            return deckDAO.searchDecksFromUsername(userSearch, username);

        } catch (Exception e) {
            String message = "Failed to search decks: " + e.getMessage();
            logger.warning(message);
            halt(500, message);

            return new ArrayList<>();
        }
    }

    private String usernameFromToken(Request req) {
        String token = req.headers("Authorization");

        if (!JWTService.getInstance().isTokenValid(token)) {
            System.out.println("Invalid token");
            halt(401, "Invalid token");
        }

        return JWTService.getInstance().getUsernameFromToken(token);
    }
}
