package ulb.infof307.g01.gui.httpdao.dao;

import com.google.gson.Gson;
import ulb.infof307.g01.model.GlobalLeaderboard;
import ulb.infof307.g01.model.DeckLeaderboard;
import ulb.infof307.g01.model.Score;
import ulb.infof307.g01.shared.constants.ServerPaths;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.UUID;

public class LeaderboardDAO extends HttpDAO {

    /* ====================================================================== */
    /*                               DAO methods                              */
    /* ====================================================================== */

    public void addScore(Score score) throws IOException, InterruptedException {
        HttpResponse<String> response = post(ServerPaths.SAVE_SCORE_PATH, new Gson().toJson(score));

        checkResponseCode(response.statusCode());
    }

    public DeckLeaderboard getLeaderboardForDeck(UUID deckId) throws IOException, InterruptedException {
        HttpResponse<String> response = get(ServerPaths.GET_LEADERBOARD_PATH + "?deck=" + deckId.toString());

        checkResponseCode(response.statusCode());

        return new Gson().fromJson(response.body(), DeckLeaderboard.class);
    }

    public Score getBestScoreForDeck(UUID deckId) throws IOException, InterruptedException {
        HttpResponse<String> response = get(ServerPaths.GET_BEST_SCORE_PATH + "?deck=" + deckId.toString());

        checkResponseCode(response.statusCode());

        return new Gson().fromJson(response.body(), Score.class);
    }

    public GlobalLeaderboard getGlobalLeaderboard() throws IOException, InterruptedException {
        HttpResponse<String> response = get(ServerPaths.GET_BEST_SCORE_USER_ID_PATH);

        checkResponseCode(response.statusCode());

        return new Gson().fromJson(response.body(), GlobalLeaderboard.class);
    }
}
