package ulb.infof307.g01.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.deck.Deck;
import ulb.infof307.g01.model.deck.Score;
import ulb.infof307.g01.model.leaderboard.DeckLeaderboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDeckLeaderboard {
    private Deck deck;
    private DeckLeaderboard leaderboard;

    @BeforeEach
    void init() {
        this.deck = new Deck("test");
        this.leaderboard = new DeckLeaderboard(deck.getId());
    }

    @Test
    void addScore_FromInit_ScoreAdded() {
        User user = new User("user", "pass");
        Score scoreBad = new Score(user.getUsername(), deck.getId(), 360, 1586503737);
        Score scoreGood = new Score(user.getUsername(), deck.getId(), 1024, 1585812537);
        
        leaderboard.addScore(scoreGood);
        leaderboard.addScore(scoreBad);

        List<Score> expectedLeaderboard = new ArrayList<>(Arrays.asList(scoreGood, scoreBad));
        assertEquals(expectedLeaderboard, leaderboard.getLeaderboard());
    }

    @Test
    void addScores_FromInit_AddingMultipleScoresAtOnce() {
        User user = new User("user", "pass");
        Score scoreBad = new Score(user.getUsername(), deck.getId(), 360, 1585812537);
        Score scoreGood = new Score(user.getUsername(), deck.getId(), 1024, 1585812537);
        Score scoreAverage = new Score(user.getUsername(), deck.getId(), 512, 1585812537);

        List<Score> scores = new ArrayList<>(Arrays.asList(scoreAverage, scoreBad, scoreGood));
        leaderboard.addScores(scores);

        List<Score> expectedLeaderboard = new ArrayList<>(Arrays.asList(scoreGood, scoreAverage, scoreBad));

        assertEquals(expectedLeaderboard, leaderboard.getLeaderboard());
    }
}
