package ulb.infof307.g01.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class DeckLeaderboard {
    final private UUID deckId;
    final private List<Score> scores;

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
        scores.sort(Comparator.comparing(Score::getScore).reversed());
    }
}
