package ulb.infof307.g01.model.leaderboard;

import ulb.infof307.g01.model.deck.Score;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class DeckLeaderboard {
    private final UUID deckId;
    private final List<Score> scores;

    public DeckLeaderboard(UUID deckId) {
        this.deckId = deckId;
        this.scores = new ArrayList<>();
    }

    public DeckLeaderboard(UUID deckId, List<Score> scores) {
        this.deckId = deckId;
        this.scores = scores;
        this.sortByScore();
    }

    public List<Score> getLeaderboard() {
        return scores;
    }

    public boolean isEmpty() {
        return scores.isEmpty();
    }

    public void addScore(Score score) {
        scores.add(score);
        this.sortByScore();
    }

    public void addScores(List<Score> newScores) {
        scores.addAll(newScores);
        this.sortByScore();
    }

    private void sortByScore() {
        scores.sort(Comparator.comparing(Score::getScoreValue).reversed());
    }
}
