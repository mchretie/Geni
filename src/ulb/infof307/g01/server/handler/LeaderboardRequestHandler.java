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
        get(GET_BEST_SCORE_PATH, this::getBestScoreByDeckId, toJson());
    }

    private Map<String, String> saveScore(Request req, Response res) {
        try {
            // check if token is valid
            String token = req.headers("Authorization");
            if (token == null || !jwtService.isTokenValid(token))
                throw new RuntimeException("Token is " + (token == null ? "null" : "not valid"));

            // check if username from token and username from score are the same
            String username = jwtService.getUsernameFromToken(token);
            Score score = new Gson().fromJson(req.body(), Score.class);
            if (! username.equals(score.getUsername()))
                throw new RuntimeException("userId from token doesn't match userId from score.");

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
        try {
            UUID deckId = UUID.fromString(req.queryParams("deck"));
            if (! database.deckIdExists(deckId))
                throw new RuntimeException("DeckId '"+ deckId +"' does not exists.");
            return database.getLeaderboardFromDeckId(deckId);
        } catch (Exception e) {
            String errorMessage = "Failed to get leaderboard: " + e.getMessage();
            logger.warning(errorMessage);
            halt(500, errorMessage);
            return null;
        }
    }

    private Score getBestScoreByDeckId(Request request, Response response) {
        Leaderboard leaderboard = getLeaderboardByDeckId(request, response);

        if (leaderboard == null || leaderboard.isEmpty())
            return null;

        return leaderboard.getLeaderboard().get(0);
    }
}
