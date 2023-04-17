package ulb.infof307.g01.model.deck;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Score {
    private final String username;
    private final UUID deckId;
    private int score;
    private final Date timestamp;

    public Score(String username, UUID deckId, int score, Date timestamp) {
        this.username = username;
        this.deckId = deckId;
        this.score = score;
        this.timestamp = timestamp;
    }

    static public Score createNewScore(String username, UUID deckId) {
        return new Score(username, deckId, 0, new Date());
    }

    public void increment(int value) {
        this.score += value;
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

    public Date getTimestamp() {
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
                timestamp.equals(other.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, deckId, score, timestamp);
    }
}
