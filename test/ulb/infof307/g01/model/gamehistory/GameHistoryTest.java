package ulb.infof307.g01.model.gamehistory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameHistoryTest {

    private GameHistory gameHistory;

    @BeforeEach
    void setUp() {
        String deckName = "InterestingDeck";
        String score = "10";

        Game game = new Game(100, deckName, score);
        Game game2 = new Game(1000, deckName, score);
        Game game3 = new Game(10000, deckName, score);

        List<Game> games = new ArrayList<>();
        games.add(game);
        games.add(game2);
        games.add(game3);

        gameHistory = new GameHistory(games);
    }

    @Test
    void getNumberOfGames() {
        assertEquals(3, gameHistory.getNumberOfGames());
    }

    @Test
    void totalScore() {
        assertEquals(30, gameHistory.totalScore());
    }

    @Test
    void forEach() {
        int previousTimestamp = 1000000000;
        for (Game game : gameHistory) {
            assertTrue(new Date(game.timestamp()).before(new Date(previousTimestamp)));
            previousTimestamp = game.timestamp();
        }
    }
}