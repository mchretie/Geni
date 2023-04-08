package ulb.infof307.g01.server.handler;

import static spark.Spark.*;
import static ulb.infof307.g01.shared.constants.ServerPaths.*;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import ulb.infof307.g01.model.Leaderboard;
import ulb.infof307.g01.model.Score;
import ulb.infof307.g01.server.database.Database;
import ulb.infof307.g01.server.service.JWTService;

import java.util.Map;
import java.util.UUID;

public class LeaderboardRequestHandler extends Handler {
    private final Database database;
    private final JWTService jwtService;

    public LeaderboardRequestHandler(Database database, JWTService jwtService) {
        this.database = database;
        this.jwtService = jwtService;
    }


    @Override
    public void init() {
        post(SAVE_SCORE_PATH, this::saveScore, toJson());
        get(GET_LEADERBOARD_PATH, this::getLeaderboardByDeckId, toJson());
    }

    private Map<String, String> saveScore(Request req, Response res) {
        try {
            // TODO same code in DeckRequestHandler::usernameFromRequest
            String token = req.headers("Authorization");
            if (token == null || !jwtService.isTokenValid(token))
                throw new RuntimeException("Invalid token.");

            String username = jwtService.getUsernameFromToken(token);
            UUID userId = UUID.fromString(database.getUserId(username));

            Score score = new Gson().fromJson(req.body(), Score.class);
            if (! userId.equals(score.getUserId()))
                throw new RuntimeException("Wrong userId");
            database.saveScore(score);
            return successfulResponse;
        } catch (Exception e) {
            String errorMessage = "Failed to add score: " + e.getMessage();
            logger.warning(errorMessage);
            halt(500, errorMessage);

            return failedResponse;
        }
    }

    private Leaderboard getLeaderboardByDeckId(Request req, Response res) {
        // TODO: if deckId is wrong it doesn't throw error
        try {
            UUID deckId = UUID.fromString(req.queryParams("deck"));
            return database.getLeaderboardFromDeckId(deckId);
        } catch (Exception e) {
            String errorMessage = "Failed to get leaderboard: " + e.getMessage();
            logger.warning(errorMessage);
            halt(500, errorMessage);
            return null;
        }
    }
}
