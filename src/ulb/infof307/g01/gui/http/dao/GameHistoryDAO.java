package ulb.infof307.g01.gui.http.dao;

import com.google.gson.Gson;
import ulb.infof307.g01.model.gamehistory.GameHistory;
import ulb.infof307.g01.shared.constants.ServerPaths;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.UUID;

public class GameHistoryDAO extends HttpDAO {
    /* ====================================================================== */
    /*                               DAO methods                              */
    /* ====================================================================== */

    public GameHistory getGameHistory() throws IOException, InterruptedException {
        HttpResponse<String> response = get(ServerPaths.GAME_HISTORY_PATH);
        checkResponseCode(response.statusCode());

        return new Gson().fromJson(response.body(), GameHistory.class);
    }

    public GameHistory getGameHistory(UUID deckId) throws IOException, InterruptedException {
        HttpResponse<String> response
                = get(ServerPaths.SPECIFIC_GAME_HISTORY_PATH + "?deck_id=" + deckId);
        checkResponseCode(response.statusCode());

        return new Gson().fromJson(response.body(), GameHistory.class);
    }
}
