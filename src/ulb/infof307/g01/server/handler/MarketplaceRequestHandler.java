package ulb.infof307.g01.server.handler;

import spark.Request;
import spark.Response;
import ulb.infof307.g01.model.deck.MarketplaceDeckMetadata;
import ulb.infof307.g01.server.database.Database;
import ulb.infof307.g01.server.service.JWTService;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.halt;
import static ulb.infof307.g01.shared.constants.ServerPaths.GET_MARKETPLACE_PATH;

public class MarketplaceRequestHandler extends Handler {

    public MarketplaceRequestHandler(Database database, JWTService jwtService) {
        super(database, jwtService);
    }

    @Override
    public void init() {
        get(GET_MARKETPLACE_PATH, this::getAllMarketplaceDecks, toJson());
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
}