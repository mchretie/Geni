package ulb.infof307.g01.model;

import java.util.Date;
import java.util.UUID;

public class Score {
    private UUID userId;
    private UUID deckId;
    private int score;
    private Date timestamp;

    public Score(UUID userId, UUID deckId, int score, Date timestamp) {
        this.userId = userId;
        this.deckId = deckId;
        this.score = score;
        this.timestamp = timestamp;
    }

    static public Score createNewScore(UUID userId, UUID deckId) {
        return new Score(userId, deckId, 0, new Date());
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
}
