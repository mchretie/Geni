package ulb.infof307.g01.server.handler;

import spark.Request;
import spark.Response;
import ulb.infof307.g01.model.deck.Deck;
import ulb.infof307.g01.model.deck.DeckMetadata;
import ulb.infof307.g01.server.database.Database;
import ulb.infof307.g01.server.service.JWTService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static spark.Spark.*;
import static ulb.infof307.g01.shared.constants.ServerPaths.*;
import static java.util.stream.Collectors.toList;


public class DeckRequestHandler extends Handler {

    public DeckRequestHandler(Database database, JWTService jwtService) {
        super(database, jwtService);
    }

    @Override
    public void init() {
        post(SAVE_DECK_PATH, this::saveDeck, toJson());
        delete(DELETE_DECK_PATH, this::deleteDeck, toJson());
        get(GET_ALL_DECKS_PATH, this::getAllDecks, toJson());
        get(SEARCH_DECKS_PATH, this::searchDecks, toJson());
        get(GET_DECK_PATH, this::getDeck, toJson());
        get(DECK_EXISTS_PATH, this::deckExists, toJson());
        post(SAVE_DECK_IMAGE_PATH, this::saveImage);
        get(NUMBER_OF_PUBLIC_PLAYED_DECKS_PATH, this::getNumberOfPublicPlayedDecks, toJson());
    }

    private int getNumberOfPublicPlayedDecks(Request request, Response response) {
        try {
            String username = usernameFromRequest(request);
            UUID userId = UUID.fromString(database.getUserId(username));

            return database.getNumberOfPublicPlayedDecks(userId);

        } catch (Exception e) {
            String message = "Failed to get number of public played decks: " + e.getMessage();
            logger.warning(message);
            halt(500, message);
            return 0;
        }


    }

    private boolean deckExists(Request request, Response response) {
        try {
            String username = usernameFromRequest(request);
            UUID userId = UUID.fromString(database.getUserId(username));

            String deckName = request.queryParams("name");
            deckName = deckName.replace("_", " ");

            return database.deckNameExists(deckName, userId);

        } catch (Exception e) {
            String message = "Failed to check if deck exists: " + e.getMessage();
            logger.warning(message);
            halt(500, message);

            return false;
        }
    }

    private Map<String, Boolean> saveDeck(Request req, Response res) {
        try {
            String username = usernameFromRequest(req);
            UUID userId = UUID.fromString(database.getUserId(username));

            Deck deck = Deck.fromJson(req.body());
            deck.setImage(deck.getImage().replace(BASE_URL, ""));

            database.saveDeck(deck, userId);

            database.addDeckToUserCollection(deck.getId(), userId);
            return successfulResponse;

        } catch (Exception e) {
            String message = "Failed to save deck: " + e.getMessage();
            logger.warning(message);
            halt(500, message);

            return failedResponse;
        }
    }

    private Map<String, Boolean> deleteDeck(Request req, Response res) {
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

    private Deck getDeck(Request req, Response res) {
        try {
            UUID deckId = UUID.fromString(req.queryParams("deck_id"));
            Deck deck = database.getDeck(deckId);
            deck.setImage(BASE_URL + deck.getImage());
            return deck;
        } catch (Exception e) {
            String message = "Failed to get deck: " + e.getMessage();
            logger.warning(message);
            halt(500, message);

            return null;
        }

    }

    private DeckMetadata setupImagePath(DeckMetadata deckMetadata) {
        return new DeckMetadata(deckMetadata.id(),
                deckMetadata.name(),
                deckMetadata.isPublic(),
                deckMetadata.color(),
                BASE_URL + deckMetadata.image(),
                deckMetadata.colorName(),
                deckMetadata.cardCount(),
                deckMetadata.tags(),
                deckMetadata.deckHashCode());
    }
    private List<DeckMetadata> setupImagePath(List<DeckMetadata> deckMetadatas) {
        return deckMetadatas.stream()
                .map(this::setupImagePath)
                .collect(toList());
    }

    private List<DeckMetadata> getAllDecks(Request req, Response res) {
        try {
            String username = usernameFromRequest(req);
            UUID userId = UUID.fromString(database.getUserId(username));

            return setupImagePath(database.getAllUserDecksMetadata(userId));

        } catch (Exception e) {
            String message = "Failed to get all decks: " + e.getMessage();
            logger.warning(message);
            halt(500, message);

            return new ArrayList<>();
        }
    }

    private List<DeckMetadata> searchDecks(Request req, Response res) {
        try {
            String username = usernameFromRequest(req);
            UUID userId = UUID.fromString(database.getUserId(username));

            String userSearch = req.queryParams("name");
            userSearch = userSearch.replace("_", " ");

            return setupImagePath(database.searchDecksMetadata(userSearch, userId));

        } catch (Exception e) {
            String message = "Failed to search decks: " + e.getMessage();
            logger.warning(message);
            halt(500, message);

            return new ArrayList<>();
        }
    }

    private Map<String, Boolean> saveImage(Request req, Response res) {
        try {
            String fileName = req.headers("File-Name");
            byte[] fileContent = req.bodyAsBytes();
            // Save the file to disk
            Path filePath = Paths.get("images", fileName);
            Files.write(filePath, fileContent);
            return successfulResponse;

        } catch (Exception e) {
            String message = "Failed to save image: " + e.getMessage();
            logger.warning(message);
            halt(500, message);

            return failedResponse;
        }
    }
}
