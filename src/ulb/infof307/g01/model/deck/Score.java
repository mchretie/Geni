package ulb.infof307.g01.model.deck;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Score {
    private final String username;
    private final UUID deckId;
    private int score;
    private final List<Integer> scoreHistory;
    private final List<Double> times;
    private final long timestamp;

    public Score(String username, UUID deckId, int score, long timestamp) {
        this.username = username;
        this.deckId = deckId;
        this.score = score;
        this.timestamp = timestamp;
        this.times = new ArrayList<>();
        this.scoreHistory = new ArrayList<>();
    }

    public Score(String username, UUID deckId) {
        this(username, deckId, 0, System.currentTimeMillis());
    }

    public void increment(int value) {
        this.score += value;
        this.scoreHistory.add(this.score);
    }

    public void addTime(double timeLeft) {
        this.times.add(timeLeft);
        System.out.println("Time left: " + timeLeft);
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUsername() {
        return this.username;
    }

    public UUID getDeckId() {
        return this.deckId;
    }

    public int getScore() {
        return score;
    }

    public int getAmountScores() {
        return scoreHistory.size();
    }

    public int getAmountCorrectAnswers() {
        return scoreHistory.size();
    }

    public int getScoreAt(int index) {
        return scoreHistory.get(index);
    }

    public double getTotalTime() {
        if (times.isEmpty()) {
            return 0;
        }

        double sum = 0;
        for (double time : times) {
            sum += time;
        }
        return Math.round(sum * 100.0) / 100.0;
    }

    public double getAvgTime() {
        if (times.isEmpty()) {
            return 0;
        }

        double totalTime = getTotalTime();
        return Math.round(totalTime / times.size() * 100.0) / 100.0;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;

        Score other = (Score) o;
        return username.equals(other.username) &&
                deckId.equals(other.deckId) &&
                score == other.score &&
                timestamp == other.timestamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, deckId, score, timestamp);
    }
}
