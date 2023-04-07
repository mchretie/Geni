package ulb.infof307.g01.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestLeaderboard {
    private Deck deck;
    private Leaderboard leaderboard;

    @BeforeEach
    void init() {
        this.deck = new Deck("test");
        this.leaderboard = new Leaderboard(deck.getId());
    }

    @Test
    void addScore_FromInit_ScoreAdded() {
        User user = new User("user", "pass");
        Score scoreBad = new Score(user.getUserId(), deck.getId(), 360, new Date(1586503737));
        Score scoreGood = new Score(user.getUserId(), deck.getId(), 1024, new Date(1585812537));
        leaderboard.addScore(scoreGood);
        leaderboard.addScore(scoreBad);

        List<Score> expectedLeaderboard = new ArrayList<>(Arrays.asList(scoreBad, scoreGood));
        assertEquals(expectedLeaderboard, leaderboard.getLeaderboard());
    }

    @Test
    void addScores_FromInit_AddingMultipleScoresAtOnce() {
        User user = new User("user", "pass");
        Score scoreBad = new Score(user.getUserId(), deck.getId(), 360, new Date(1586503737));
        Score scoreGood = new Score(user.getUserId(), deck.getId(), 1024, new Date(1585812537));
        Score scoreAverage = new Score(user.getUserId(), deck.getId(), 512, new Date(1096961337));

        List<Score> scores = new ArrayList<>(Arrays.asList(scoreAverage, scoreBad, scoreGood));
        leaderboard.addScores(scores);

        List<Score> expectedLeaderboard = new ArrayList<>(Arrays.asList(scoreBad, scoreAverage, scoreGood));

        assertEquals(expectedLeaderboard, leaderboard.getLeaderboard());
    }
}
