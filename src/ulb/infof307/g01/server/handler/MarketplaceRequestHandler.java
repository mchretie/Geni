package ulb.infof307.g01.server.handler;

import spark.Request;
import spark.Response;
import ulb.infof307.g01.model.deck.Deck;
import ulb.infof307.g01.model.rating.UserRating;
import ulb.infof307.g01.model.deck.MarketplaceDeckMetadata;
import ulb.infof307.g01.server.database.Database;
import ulb.infof307.g01.server.service.JWTService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
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
        delete(REMOVE_DECK_FROM_COLLECTION_PATH, this::removeDeckFromCollection, toJson());
        get(GET_SAVED_DECKS_FROM_MARKETPLACE, this::getUsersCollection, toJson());
        post(ADD_RATING, this::addRating, toJson());
        get(GET_USER_RATING, this::getUserRating, toJson());
    }


    private MarketplaceDeckMetadata setupImagePath(MarketplaceDeckMetadata deckMetadata) {
        return new MarketplaceDeckMetadata(
                deckMetadata.id(),
                deckMetadata.name(),
                deckMetadata.isPublic(),
                deckMetadata.color(),
                BASE_URL + deckMetadata.image(),
                deckMetadata.colorName(),
                deckMetadata.cardCount(),
                deckMetadata.tags(),
                deckMetadata.owner(),
                deckMetadata.rating(),
                deckMetadata.downloads(),
                deckMetadata.deckHashCode()
        );
    }

    private List<MarketplaceDeckMetadata> setupImagePath(List<MarketplaceDeckMetadata> decksMetadata) {
        return decksMetadata.stream()
                .map(this::setupImagePath)
                .collect(toList());
    }

    private List<MarketplaceDeckMetadata> getAllMarketplaceDecks(Request req, Response res) {
        try {
            return setupImagePath(database.getMarketplaceDecksMetadata());

        } catch (Exception e) {
            String message = "Failed to get marketplace decks: " + e.getMessage();
            logger.warning(message);
            halt(500, message);

            return new ArrayList<>();
        }
    }

    /**
     * when adding deck from marketplace, the owner's score has to be deleted for this deck
     */
    private Map<String, Boolean> addDeck(Request req, Response res) {
        try {
            Deck deck = Deck.fromJson(req.body());

            database.addDeckToMarketplace(deck.getId());
            database.deleteScoresForDeck(deck.getId());

            return successfulResponse;

        } catch (Exception e) {
            String message = "Failed to add deck to the marketplace : " + e.getMessage();
            logger.warning(message);
            halt(500, message);

            return failedResponse;
        }
    }

    /**
     * when removing deck from marketplace, scores for all users have to be deleted for this deck
     */
    private Map<String, Boolean> removeDeck(Request req, Response res) {
        try {
            UUID userId = userIdFromRequest(req);
            UUID deckId = UUID.fromString(req.queryParams("deck_id"));
            UUID deckOwnerId = database.getDeckOwnerId(deckId);

            if (!userId.equals(deckOwnerId)) {
                String message = "User " + userId + " is not the owner of deck " + deckId;
                halt(403, message);
                return failedResponse;
            }

            database.removeDeckFromMarketplace(deckId);
            database.deleteScoresForDeck(deckId);
            database.removeDeckFromNonOwnerCollection(deckId, deckOwnerId);
            if (! database.isDeckInUserCollection(deckId, deckOwnerId)) {
                database.addDeckToUserCollection(deckId, deckOwnerId);
            }

            return successfulResponse;
        } catch (Exception e) {
            String message = "Failed to remove deck from the marketplace : " + e.getMessage();
            logger.warning(message);
            halt(500, message);

            return failedResponse;
        }
    }

    private Map<String, Boolean> addDeckToCollection(Request req, Response res) {
        try {
            UUID deckId = UUID.fromString(req.queryParams("deck_id"));
            UUID userId = userIdFromRequest(req);

            database.addDeckToUserCollection(deckId, userId);

            return successfulResponse;
        } catch (Exception e) {
            String message = "Failed to add deck to user's collection : " + e.getMessage();
            logger.warning(message);
            halt(500, message);

            return failedResponse;
        }
    }

    private Map<String, Boolean> removeDeckFromCollection(Request req, Response res) {
        try {
            UUID deckId = UUID.fromString(req.queryParams("deck_id"));
            String username = usernameFromRequest(req);
            UUID userId = UUID.fromString(database.getUserId(username));

            database.removeDeckFromUserCollection(deckId, userId);

            return successfulResponse;

        } catch (Exception e) {
            String message = "Failed to remove deck from user's collection : " + e.getMessage();
            logger.warning(message);
            halt(500, message);

            return failedResponse;
        }
    }

    private List<MarketplaceDeckMetadata> getUsersCollection(Request req, Response res) {
        try {
            String username = usernameFromRequest(req);
            UUID userId = UUID.fromString(database.getUserId(username));
            return setupImagePath(database.getUsersCollectionFromMarketplace(userId));

        } catch (Exception e) {
            String message = "Failed to get the user's deck collection from the marketplace: " + e.getMessage();
            logger.warning(message);
            halt(500, message);

            return new ArrayList<>();
        }
    }

    private Map<String, Boolean> addRating(Request req, Response res) {
        try {
            UserRating userRating = UserRating.fromJson(req.body());
            UUID userId = userIdFromRequest(req);

            database.addRating(new UserRating(userRating.deckId(), userId, userRating.value()));

            return successfulResponse;

        } catch (Exception e) {
            String message = "Failed to add rating to the marketplace : " + e.getMessage();
            logger.warning(message);
            halt(500, message);

            return failedResponse;
        }
    }


    private UserRating getUserRating(Request req, Response res) {

        try {
            UUID deckId = UUID.fromString(req.queryParams("deck_id"));
            UUID userId = userIdFromRequest(req);
            return database.getUserRating(deckId, userId);

        } catch (Exception e) {
            String message = "Failed to get rating from the marketplace : " + e.getMessage();
            logger.warning(message);
            halt(500, message);

            return null;
        }
    }
}