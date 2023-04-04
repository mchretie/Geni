package ulb.infof307.g01.model;

import java.util.Date;

public class Score {
    private User user;
    private Deck deck;
    private int score;
    private Date timestamp;

    public Score(User user, Deck deck, int score, Date timestamp) {
        this.user = user;
        this.deck = deck;
        this.score = score;
        this.timestamp = timestamp;
    }

    static public Score createNewScore(User user, Deck deck) {
        return new Score(user, deck, 0, new Date());
    }
}
