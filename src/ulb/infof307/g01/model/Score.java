package ulb.infof307.g01.model;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Score {
    private final UUID userId;
    private final String username;
    private final UUID deckId;
    private int score;
    private final Date timestamp;

    public Score(UUID userId, String username, UUID deckId, int score, Date timestamp) {
        this.userId = userId;
        this.username = username;
        this.deckId = deckId;
        this.score = score;
        this.timestamp = timestamp;
    }

    static public Score createNewScore(UUID userId, String username, UUID deckId) {
        return new Score(userId, username, deckId, 0, new Date());
    }

    public void increment(int value) {
        this.score += value;
    }

    public UUID getUserId() {
        return this.userId;
    }

    public UUID getDeckId() {
        return this.deckId;
    }

    public int getScore() {
        return score;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;

        Score other = (Score) o;
        return userId.equals(other.userId) &&
                deckId.equals(other.deckId) &&
                score == other.score &&
                timestamp.equals(other.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, deckId, score, timestamp);
    }
}
