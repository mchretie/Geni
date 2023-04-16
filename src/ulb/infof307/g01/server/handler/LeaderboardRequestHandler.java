package ulb.infof307.g01.server.handler;

import static spark.Spark.*;
import static ulb.infof307.g01.shared.constants.ServerPaths.*;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import ulb.infof307.g01.model.GlobalLeaderboard;
import ulb.infof307.g01.model.DeckLeaderboard;
import ulb.infof307.g01.model.Score;
import ulb.infof307.g01.server.database.Database;
import ulb.infof307.g01.server.service.JWTService;

import java.util.Map;
import java.util.UUID;

public class LeaderboardRequestHandler extends Handler {

    public LeaderboardRequestHandler(Database database, JWTService jwtService) {
        super(database, jwtService);
    }

    @Override
    public void init() {
        post(SAVE_SCORE_PATH, this::saveScore, toJson());
        get(GET_LEADERBOARD_PATH, this::getLeaderboardByDeckId, toJson());
        get(GET_BEST_SCORE_PATH, this::getBestScoreByDeckId, toJson());
        get(GET_BEST_SCORE_USER_ID_PATH, this::getBestScoreByUserID, toJson());
    }

    private Map<String, Boolean> saveScore(Request req, Response res) {
        try {
            String username = usernameFromRequest(req);
            Score score = new Gson().fromJson(req.body(), Score.class);

            // Does this ever happen?
            if (!username.equals(score.getUsername()))
                throw new RuntimeException("UserId from token doesn't match userId from score.");

            database.saveScore(score);

            return successfulResponse;

        } catch (Exception e) {
            String errorMessage = "Failed to add score: " + e.getMessage();
            logger.warning(errorMessage);
            halt(500, errorMessage);

            return failedResponse;
        }
    }

    private DeckLeaderboard getLeaderboardByDeckId(Request req, Response res) {
        try {
            UUID deckId = UUID.fromString(req.queryParams("deck"));

            // Does this ever happen?
            if (!database.deckIdExists(deckId))
                return null;

            return database.getLeaderboardFromDeckId(deckId);

        } catch (Exception e) {
            String errorMessage = "Failed to get leaderboard: " + e.getMessage();
            logger.warning(errorMessage);
            halt(500, errorMessage);
            return null;
        }
    }

    private Score getBestScoreByDeckId(Request request, Response response) {
        DeckLeaderboard leaderboard = getLeaderboardByDeckId(request, response);

        if (leaderboard == null || leaderboard.isEmpty())
            return null;

        return leaderboard.getLeaderboard().get(0);
    }

    public GlobalLeaderboard getBestScoreByUserID(Request req, Response res) {
        try {
            return database.getLeaderboardFromUserID();

        } catch (Exception e) {
            String errorMessage = "Failed to get user score: " + e.getMessage();
            logger.warning(errorMessage);
            halt(500, errorMessage);
            return null;
        }
    }
}
