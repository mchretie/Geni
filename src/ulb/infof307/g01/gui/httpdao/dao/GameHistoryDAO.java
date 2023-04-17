package ulb.infof307.g01.gui.httpdao.dao;

import com.google.gson.Gson;
import ulb.infof307.g01.model.GameHistory;
import ulb.infof307.g01.shared.constants.ServerPaths;

import java.io.IOException;
import java.net.http.HttpResponse;

public class GameHistoryDAO extends HttpDAO {
    /* ====================================================================== */
    /*                               DAO methods                              */
    /* ====================================================================== */

    public GameHistory getGameHistory() throws IOException, InterruptedException {
        HttpResponse<String> response = get(ServerPaths.GAME_HISTORY_PATH);
        checkResponseCode(response.statusCode());

        System.out.println(response.body());

        return new Gson().fromJson(response.body(), GameHistory.class);
    }
}
