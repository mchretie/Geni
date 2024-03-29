package ulb.infof307.g01.server.handler;

import ulb.infof307.g01.model.gamehistory.GameHistory;
import ulb.infof307.g01.server.database.Database;
import ulb.infof307.g01.server.service.JWTService;

import static spark.Spark.*;
import spark.Request;
import spark.Response;

import java.util.UUID;

import static ulb.infof307.g01.shared.constants.ServerPaths.GAME_HISTORY_PATH;
import static ulb.infof307.g01.shared.constants.ServerPaths.SPECIFIC_GAME_HISTORY_PATH;

public class GameHistoryRequestHandler extends Handler {

    public GameHistoryRequestHandler(Database database, JWTService jwtService) {
        super(database, jwtService);
    }

    @Override
    public void init() {
        get(GAME_HISTORY_PATH, this::getGameHistory, toJson());
        get(SPECIFIC_GAME_HISTORY_PATH, this::getSpecificGameHistory, toJson());
    }

    private GameHistory getGameHistory(Request request, Response response) {
        try {
            String username = usernameFromRequest(request);
            UUID userId = UUID.fromString(database.getUserId(username));
            return database.getGameHistory(userId);

        } catch (Exception e) {
            String message = "Failed to get game history: " + e.getMessage();
            logger.warning(message);
            halt(500, message);

            return null;
        }
    }

    private GameHistory getSpecificGameHistory(Request request, Response response) {
        try {
            String username = usernameFromRequest(request);
            UUID userId = UUID.fromString(database.getUserId(username));
            UUID deckId = UUID.fromString(request.queryParams("deck_id"));
            return database.getGameHistory(userId, deckId);

        } catch (Exception e) {
            String message = "Failed to get game history: " + e.getMessage();
            logger.warning(message);
            halt(500, message);

            return null;
        }
    }
}
