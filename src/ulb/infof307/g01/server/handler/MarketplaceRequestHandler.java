package ulb.infof307.g01.server.handler;

import spark.Request;
import spark.Response;
import ulb.infof307.g01.model.deck.MarketplaceDeckMetadata;
import ulb.infof307.g01.server.database.Database;
import ulb.infof307.g01.server.service.JWTService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static spark.Spark.*;
import static ulb.infof307.g01.shared.constants.ServerPaths.*;

public class MarketplaceRequestHandler extends Handler {

    public MarketplaceRequestHandler(Database database, JWTService jwtService) {
        super(database, jwtService);
    }

    @Override
    public void init() {
        get(GET_MARKETPLACE_PATH, this::getAllMarketplaceDecks, toJson());
        post(ADD_DECK_TO_MARKETPLACE_PATH, this::addDeck, toJson());
        delete(REMOVE_DECK_FROM_MARKETPLACE_PATH, this::removeDeck, toJson());
        post(ADD_DECK_TO_COLLECTION_PATH, this::addDeckToCollection, toJson());
    }

    private List<MarketplaceDeckMetadata> getAllMarketplaceDecks(Request req, Response res) {
        try {

            return database.getMarketplaceDecksMetadata();

        } catch (Exception e) {
            String message = "Failed to get marketplace decks: " + e.getMessage();
            logger.warning(message);
            halt(500, message);

            return new ArrayList<>();
        }
    }

    private Map<String, Boolean> addDeck(Request req, Response res) {
        try {
            UUID deckId = UUID.fromString(req.queryParams("deck_id"));
            database.addDeckToMarketplace(deckId);

            return successfulResponse;
        } catch (Exception e) {
            String message = "Failed to add deck to the marketplace : " + e.getMessage();
            logger.warning(message);
            halt(500, message);

            return failedResponse;
        }
    }

    private Map<String, Boolean> removeDeck(Request req, Response res) {
        try {
            // TODO to improve security, should check if req sender is the deck owner
            UUID deckId = UUID.fromString(req.queryParams("deck_id"));
            database.removeDeckFromMarketplace(deckId);

            return successfulResponse;
        } catch (Exception e) {
            String message = "Failed to remove deck from the marketplace : " + e.getMessage();
            logger.warning(message);
            halt(500, message);

            return failedResponse;
        }
    }

    public Map<String, Boolean> addDeckToCollection(Request req, Response res) {
        try {
            UUID deckId = UUID.fromString(req.queryParams("deck_id"));
            String username = usernameFromRequest(req);
            UUID userId = UUID.fromString(database.getUserId(username));

            database.addDeckToUserCollection(deckId, userId);

            return successfulResponse;
        } catch (Exception e) {
            String message = "Failed to add deck to the user collection : " + e.getMessage();
            logger.warning(message);
            halt(500, message);

            return failedResponse;
        }
    }
}