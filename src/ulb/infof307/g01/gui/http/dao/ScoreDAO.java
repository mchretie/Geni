package ulb.infof307.g01.gui.http.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ulb.infof307.g01.model.deck.DeckMetadata;
import ulb.infof307.g01.model.deck.MarketplaceDeckMetadata;
import ulb.infof307.g01.model.leaderboard.GlobalLeaderboard;
import ulb.infof307.g01.model.leaderboard.DeckLeaderboard;
import ulb.infof307.g01.model.deck.Score;
import ulb.infof307.g01.shared.constants.ServerPaths;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ScoreDAO extends HttpDAO {

    /* ====================================================================== */
    /*                               DAO methods                              */
    /* ====================================================================== */

    public void addScore(Score score) throws IOException, InterruptedException {
        HttpResponse<String> response = post(ServerPaths.SAVE_SCORE_PATH, new Gson().toJson(score));

        checkResponseCode(response.statusCode());
    }

    public Score getBestScoreForDeck(UUID deckId) throws IOException, InterruptedException {
        HttpResponse<String> response
                = get(ServerPaths.GET_BEST_SCORES_PATH + "?deckId[]=" + deckId);

        checkResponseCode(response.statusCode());

        TypeToken<List<Score>> typeToken = new TypeToken<>() {};
        List<Score> bestScores = new Gson().fromJson(response.body(), typeToken);
        if (bestScores.isEmpty())
            return null;
        return bestScores.get(0);
    }

    public HashMap<UUID, Score> getBestScoreForDecks(List<DeckMetadata> decks)
            throws IOException, InterruptedException {
        String path = ServerPaths.GET_BEST_SCORES_PATH + "?";
        for (DeckMetadata deck : decks)
            path += "deckId[]=" + deck.id() + "&";

        HttpResponse<String> response
                = get(path);

        checkResponseCode(response.statusCode());

        TypeToken<List<Score>> typeToken = new TypeToken<>() {};
        List<Score> bestScoresList = new Gson().fromJson(response.body(), typeToken);

        HashMap<UUID, Score> bestScores = new HashMap<>();
        for (Score bestScore : bestScoresList)
            bestScores.put(bestScore.getDeckId(), bestScore);
        return bestScores;
    }

    public DeckLeaderboard getLeaderboardForDeck(UUID deckId) throws IOException, InterruptedException {
        HttpResponse<String> response
                = get(ServerPaths.GET_LEADERBOARD_PATH + "?deck=" + deckId);

        checkResponseCode(response.statusCode());

        return new Gson().fromJson(response.body(), DeckLeaderboard.class);
    }

    public GlobalLeaderboard getGlobalLeaderboard() throws IOException, InterruptedException {
        HttpResponse<String> response = get(ServerPaths.GET_GLOBAL_LEADERBOARD);

        checkResponseCode(response.statusCode());

        return new Gson().fromJson(response.body(), GlobalLeaderboard.class);
    }
}
