package ulb.infof307.g01.gui.httpdao.dao;

import com.google.gson.Gson;
import ulb.infof307.g01.model.Leaderboard;
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
        HttpResponse<String> response = post(ServerPaths.SAVE_TAG_PATH, new Gson().toJson(score));

        checkResponseCode(response.statusCode());
    }

    public Leaderboard getLeaderboardForDeck(UUID deckId) throws IOException, InterruptedException {
        HttpResponse<String> response = get(ServerPaths.GET_LEADERBOARD_PATH + "?deck=" + deckId.toString());

        checkResponseCode(response.statusCode());

        return new Gson().fromJson(response.body(), Leaderboard.class);
    }
}
