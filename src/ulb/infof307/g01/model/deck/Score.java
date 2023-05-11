package ulb.infof307.g01.model.deck;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Score {
    private final String username;
    private final UUID deckId;
    private int scoreValue;
    private final List<Double> times;
    private final long timestamp;

    public Score(String username, UUID deckId, int scoreValue, long timestamp) {
        this.username = username;
        this.deckId = deckId;
        this.scoreValue = scoreValue;
        this.timestamp = timestamp;
        this.times = new ArrayList<>();
    }

    public Score(String username, UUID deckId) {
        this(username, deckId, 0, System.currentTimeMillis());
    }

    public void increment(int value) {
        this.scoreValue += value;
    }

    public void addTime(double timeLeft) {
        this.times.add(timeLeft);
    }

    public void setScoreValue(int scoreValue) {
        this.scoreValue = scoreValue;
    }

    public String getUsername() {
        return this.username;
    }

    public UUID getDeckId() {
        return this.deckId;
    }

    public int getScoreValue() {
        return scoreValue;
    }

    public double getTotalTime() {
        double sum = times.stream()
                           .mapToDouble(e -> e)
                           .sum();

        return Math.round(sum * 100.0) / 100.0;
    }

    public double getAvgTime() {
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
                scoreValue == other.scoreValue &&
                timestamp == other.timestamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, deckId, scoreValue, timestamp);
    }
}
