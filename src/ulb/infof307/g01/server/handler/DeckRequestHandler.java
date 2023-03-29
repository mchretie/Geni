package ulb.infof307.g01.server.handler;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.server.database.Database;
import ulb.infof307.g01.server.service.JWTService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static spark.Spark.*;

@SuppressWarnings("FieldCanBeLocal")
public class DeckRequestHandler extends Handler {

    private final String SAVE_DECK_PATH     = "/api/deck/save";
    private final String DELETE_DECK_PATH   = "/api/deck/delete";
    private final String GET_ALL_DECKS_PATH = "/api/deck/all";
    private final String SEARCH_DECKS_PATH  = "/api/deck/search";

    private final Database database;
    private final JWTService jwtService;

    public DeckRequestHandler(JWTService jwtService, Database database) {
        this.jwtService = jwtService;
        this.database = database;
    }

    @Override
    public void init() {
        logStart();

        post(SAVE_DECK_PATH, this::saveDeck, toJson());
        delete(DELETE_DECK_PATH, this::deleteDeck, toJson());
        get(GET_ALL_DECKS_PATH, this::getAllDecks, toJson());
        get(SEARCH_DECKS_PATH, this::searchDecks, toJson());

        logStarted();
    }

    private Map<String, String> saveDeck(Request req, Response res) {
        try {
            String username = usernameFromRequest(req);
            UUID userId = UUID.fromString(database.getUserId(username));

            Deck deck = new Gson().fromJson(req.body(), Deck.class);
            System.out.println(deck.getName());
            database.saveDeck(deck, userId);
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
            String username = usernameFromRequest(req);
            UUID userId = UUID.fromString(database.getUserId(username));

            UUID deckId = UUID.fromString(req.queryParams("deck_id"));

            database.deleteDeck(deckId, userId);
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
            String username = usernameFromRequest(req);
            System.out.println(username);
            UUID userId = UUID.fromString(database.getUserId(username));
            System.out.println(userId);

            return database.getAllUserDecks(userId);

        } catch (Exception e) {
            String message = "Failed to get all decks: " + e.getMessage();
            logger.warning(message);
            halt(500, message);

            return new ArrayList<>();
        }
    }

    private List<Deck> searchDecks(Request req, Response res) {
        try {
            String username = usernameFromRequest(req);
            UUID userId = UUID.fromString(database.getUserId(username));

            String userSearch = req.queryParams("name");
            System.out.println(userSearch);
            return database.searchDecks(userSearch, userId);

        } catch (Exception e) {
            String message = "Failed to search decks: " + e.getMessage();
            logger.warning(message);
            halt(500, message);

            return new ArrayList<>();
        }
    }

    private String usernameFromRequest(Request req) {
        String token = req.headers("Authorization");
        if (token == null || !jwtService.isTokenValid(token)) {
            halt(401, "Token is " + (token == null ? "null" : "not valid"));
        }
        return jwtService.getUsernameFromToken(token);
    }
}
