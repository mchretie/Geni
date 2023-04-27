package ulb.infof307.g01.server.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.deck.Deck;
import ulb.infof307.g01.model.deck.MarketplaceDeckMetadata;
import ulb.infof307.g01.server.database.dao.*;
import ulb.infof307.g01.server.database.exceptions.DatabaseException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TestMarketplaceDAO extends DatabaseUsingTest {
    UserDAO userDAO;
    DeckDAO deckDAO;
    TagDAO tagDAO;
    MarketplaceDAO marketplaceDAO;
    UUID user;

    @Override
    @BeforeEach
    void init() throws DatabaseException {
        super.init();

        db.initTables(DatabaseSchema.SERVER);

        this.userDAO = new UserDAO(this.db);

        this.deckDAO = new DeckDAO(this.db);
        this.tagDAO = new TagDAO(this.db);

        ScoreDAO scoreDAO = new ScoreDAO(this.db);
        scoreDAO.setUserDAO(userDAO);
        this.deckDAO.setTagDao(this.tagDAO);
        this.deckDAO.setScoreDAO(scoreDAO);
        this.tagDAO.setDeckDao(this.deckDAO);

        this.marketplaceDAO = new MarketplaceDAO(this.db);

        this.marketplaceDAO.setDeckDAO(deckDAO);

        userDAO.registerUser("user", "pass");
        this.user = UUID.fromString(userDAO.getUserId("user"));
    }


    @Test
    void getMarketplaceDecksMetadata_EmptyDecks_AllReturned() {
        Deck deck1 = new Deck("deck1");
        Deck deck2 = new Deck("deck2");
        Deck deck3 = new Deck("deck3");

        userDAO.registerUser("user1", "pass");
        deckDAO.saveDeck(deck1, UUID.fromString(userDAO.getUserId("user1")));
        deckDAO.saveDeck(deck2, UUID.fromString(userDAO.getUserId("user1")));

        marketplaceDAO.addDeckToMarketplace(deck1.getId());
        marketplaceDAO.addDeckToMarketplace(deck2.getId());

        List<MarketplaceDeckMetadata> expected = new ArrayList<>();
        expected.add(new MarketplaceDeckMetadata(deck1, "user1", 0, 0));
        expected.add(new MarketplaceDeckMetadata(deck2, "user1", 0, 0));

        assertEquals(expected, marketplaceDAO.getMarketplaceDecksMetadata());

        userDAO.registerUser("user2", "pass");
        deckDAO.saveDeck(deck3, UUID.fromString(userDAO.getUserId("user2")));
        marketplaceDAO.addDeckToMarketplace(deck3.getId());

        expected.add(new MarketplaceDeckMetadata(deck3, "user2", 0, 0));

        assertEquals(expected, marketplaceDAO.getMarketplaceDecksMetadata());
    }
}
