package ulb.infof307.g01.model;

public class Game {
    private final String timestamp;
    private final String deckName;
    private final String score;

    public Game(String timestamp, String deckName, String score) {
        this.timestamp = timestamp;
        this.deckName = deckName;
        this.score = score;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getDeckName() {
        return deckName;
    }

    public String getScore() {
        return score;
    }
}
