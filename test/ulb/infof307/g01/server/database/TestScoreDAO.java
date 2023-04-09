package ulb.infof307.g01.server.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Score;
import ulb.infof307.g01.server.database.dao.DeckDAO;
import ulb.infof307.g01.server.database.dao.ScoreDAO;
import ulb.infof307.g01.server.database.dao.TagDAO;
import ulb.infof307.g01.server.database.dao.UserDAO;
import ulb.infof307.g01.server.database.exceptions.DatabaseException;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class TestScoreDAO extends DatabaseUsingTest {
    private ScoreDAO scoreDAO;
    private UserDAO userDAO;
    private DeckDAO deckDAO;
    private TagDAO tagDAO;

    @Override
    @BeforeEach
    void init() throws DatabaseException {
        super.init();

        db.initTables(DatabaseScheme.SERVER);

        this.scoreDAO = new ScoreDAO(this.db);
        this.userDAO = new UserDAO(this.db);
        this.deckDAO = new DeckDAO(this.db);
        this.tagDAO = new TagDAO(this.db);

        this.deckDAO.setTagDao(this.tagDAO);
        this.tagDAO.setDeckDao(this.deckDAO);
        this.scoreDAO.setUserDAO(userDAO);
    }

    @Test
    void addScore_NewScore_ScoreInDatabase() {
        String username = "hihi";
        userDAO.registerUser(username, "hoho");
        UUID userId = UUID.fromString(userDAO.getUserId("hihi"));

        Deck deck = new Deck("pom");
        deckDAO.saveDeck(deck, userId);

        Score score = new Score(userId, username, deck.getId(), 25, new Date());
        scoreDAO.addScore(score);

        Score scoreFromDAO = scoreDAO.getScoreFromUserId(userId).get(0);
        assertEquals(scoreFromDAO, score);
    }

    @Test
    void getScoreFromDeckId_DeckWithMultipleScore_ScoresAreOrdered() {
        String username = "hihi";
        userDAO.registerUser(username, "hoho");
        UUID userId = UUID.fromString(userDAO.getUserId("hihi"));

        Deck deck1 = new Deck("pom");
        deckDAO.saveDeck(deck1, userId);
        Deck deck2 = new Deck("xD");
        deckDAO.saveDeck(deck2, userId);
        Score score1 = new Score(userId, username, deck1.getId(), 200, new Date(123456));
        Score score2 = new Score(userId, username, deck1.getId(), 100, new Date(123458));
        Score score3 = new Score(userId, username, deck2.getId(), 100, new Date(123459));
        scoreDAO.addScore(score1);
        scoreDAO.addScore(score2);
        scoreDAO.addScore(score3);

        List<Score> scoresDeck1 = scoreDAO.getScoreFromDeckId(deck1.getId());
        List<Score> expectedScoresDeck1 = new ArrayList<>(Arrays.asList(score1, score2));
        assertEquals(expectedScoresDeck1, scoresDeck1);
    }
}
