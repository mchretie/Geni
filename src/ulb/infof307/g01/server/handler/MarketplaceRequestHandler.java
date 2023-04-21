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
import static ulb.infof307.g01.shared.constants.ServerPaths.ADD_DECK_TO_MARKETPLACE_PATH;
import static ulb.infof307.g01.shared.constants.ServerPaths.GET_MARKETPLACE_PATH;

public class MarketplaceRequestHandler extends Handler {

    public MarketplaceRequestHandler(Database database, JWTService jwtService) {
        super(database, jwtService);
    }

    @Override
    public void init() {
        get(GET_MARKETPLACE_PATH, this::getAllMarketplaceDecks, toJson());
        post(ADD_DECK_TO_MARKETPLACE_PATH, this::addDeck, toJson());
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
}