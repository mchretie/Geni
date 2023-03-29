package ulb.infof307.g01.server.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.server.database.dao.DeckDAO;
import ulb.infof307.g01.server.database.dao.TagDAO;
import ulb.infof307.g01.server.database.dao.UserDAO;
import ulb.infof307.g01.server.database.exceptions.DatabaseException;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;

import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestDeckDAO extends DatabaseUsingTest {

    DeckDAO deckDAO;
    TagDAO tagDAO;
    UserDAO userDAO;

    UUID user = UUID.randomUUID();

    @Override
    @BeforeEach
    void init() throws DatabaseException {
        super.init();

        db.initTables(DatabaseScheme.SERVER);

        this.deckDAO = new DeckDAO(this.db);
        this.tagDAO = new TagDAO(this.db);
        this.userDAO = new UserDAO(this.db);

        this.deckDAO.setTagDao(this.tagDAO);
        this.tagDAO.setDeckDao(this.deckDAO);
    }

    @Test
    void getDeck_DeckNotExists_ThrowsException() throws SQLException {
        assertNull(deckDAO.getDeck(new Deck("name").getId()));
    }

    @Test
    void deckNameExists_NameNotExists_ReturnsFalse() throws SQLException {
        assertFalse(deckDAO.deckNameExists("name"));
    }

    @Test
    void deckNameExists_NameExists_ReturnsTrue() throws SQLException {
        Deck deck = new Deck("name");
        deckDAO.saveDeck(deck, user);

        assertTrue(deckDAO.deckNameExists("name"));
    }

    @Test
    void isDeckValid_DeckInvalid_ReturnsFalse() throws SQLException {
        Deck deck1 = new Deck("name");
        Deck deck2 = new Deck("name");
        deckDAO.saveDeck(deck1, user);

        assertFalse(deckDAO.isDeckValid(deck2, user));
    }

    @Test
    void isDeckValid_DeckValid_ReturnsTrue() throws SQLException {
        Deck deck = new Deck("name");
        assertTrue(deckDAO.isDeckValid(deck, user));

        deckDAO.saveDeck(deck, user);
        assertTrue(deckDAO.isDeckValid(deck, user));
    }

    @Test
    void saveDeck_DecksWithSameNameDiffId_OnlyFirstAdded() throws SQLException {
        Deck deck1 = new Deck("name");
        Deck deck2 = new Deck("name");

        deckDAO.saveDeck(deck1, user);
        deckDAO.saveDeck(deck2, user);

        assertEquals(Set.of(deck1), new HashSet<>(deckDAO.getAllDecks()));
    }

    @Test
    void saveDeck_DeckNotExists_CreatesDeck() throws SQLException {
        Deck deck = new Deck("name");
        deckDAO.saveDeck(deck, user);

        assertEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_DeckNameChanged_RenameDeck() throws SQLException {
        Deck deck = new Deck("name");
        deckDAO.saveDeck(deck, user);

        deck.setName("name_01");
        deckDAO.saveDeck(deck, user);

        assertEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_DeckNameNotUpdated_DeckNotUpdated() throws SQLException {
        Deck deck = new Deck("name");
        deckDAO.saveDeck(deck, user);

        deck.setName("name_01");

        assertNotEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_CardAdded_DeckAddedWithCard() throws SQLException {
        Deck deck = new Deck("name");
        Card card = new Card("front", "back");

        deck.addCard(card);
        deckDAO.saveDeck(deck, user);

        assertEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_CardDeleted_DeckUpdated() throws SQLException {
        Deck deck = new Deck("name");
        Card card = new Card("front", "back");

        deck.addCard(card);
        deckDAO.saveDeck(deck, user);
        deck.removeCard(card);
        deckDAO.saveDeck(deck, user);

        assertEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_TagAdded_DeckUpdated() throws SQLException {
        Deck deck = new Deck("name");
        Tag tag = new Tag("name");

        deck.addTag(tag);
        deckDAO.saveDeck(deck, user);

        assertEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_TagRemoved_DeckUpdated() throws SQLException {
        Deck deck = new Deck("name");
        Tag tag = new Tag("name");

        deck.addTag(tag);
        deckDAO.saveDeck(deck, user);
        deck.removeTag(tag);
        deckDAO.saveDeck(deck, user);

        assertEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDecks_SameNameTagsAdded_DeckUpdated() throws SQLException {
        Deck deck1 = new Deck("deck1");
        Tag tag1 = new Tag("tag");
        deck1.addTag(tag1);
        deckDAO.saveDeck(deck1);

        Deck deck2 = new Deck("deck2");
        Tag tag2 = new Tag("tag");
        deck2.addTag(tag2);
        deckDAO.saveDeck(deck2);

        Deck updatedDeck1 = deckDAO.getDeck(deck1.getId());
        Deck updatedDeck2 = deckDAO.getDeck(deck2.getId());

        assertEquals(updatedDeck1.getTags().get(0).getId(), updatedDeck2.getTags().get(0).getId());
    }

    @Test
    void getAllDecks_NoDecks_EmptyList() throws SQLException {
        assertTrue(deckDAO.getAllDecks().isEmpty());
    }

    @Test
    void getAllDecks_ManyDecks_AllReturned() throws SQLException {
        List<Deck> decks = new ArrayList<>();
        decks.add(new Deck("name1"));
        decks.add(new Deck("name2"));
        decks.add(new Deck("name3"));

        for (Deck d : decks) {
            deckDAO.saveDeck(d, user);
        }

        assertEquals(new HashSet<>(decks), new HashSet<>(deckDAO.getAllDecks()));
    }

    @Test
    void getAllDecks_SameDeckAddedMultipleTimes_OneReturned() throws SQLException {
        Deck deck = new Deck("name");
        deckDAO.saveDeck(deck, user);
        deckDAO.saveDeck(deck, user);
        deckDAO.saveDeck(deck, user);
        assertEquals(Set.of(deck), new HashSet<>(deckDAO.getAllDecks()));
    }

    @Test
    void deleteDeck_DeckExists_DeckNotExists() throws SQLException {
        Deck deck = new Deck("name");
        deckDAO.saveDeck(deck, user);
        deckDAO.deleteDeck(deck.getId(), user);

        assertNull(deckDAO.getDeck(deck.getId()));
    }

    @Test
    void deleteDeck_DeckNotExists_NoThrow() {
        Deck deck = new Deck("name");
        assertDoesNotThrow(() -> deckDAO.deleteDeck(deck.getId(), user));
    }

    @Test
    void searchTags() throws SQLException {
        List<Deck> decks = new ArrayList<>();
        decks.add(new Deck("name1"));
        decks.add(new Deck("name2"));
        decks.add(new Deck("name3"));

        for (Deck d : decks) {
            deckDAO.saveDeck(d, user);
        }

        assertEquals(new HashSet<>(decks), new HashSet<>(deckDAO.searchDecks("name", user)));
    }
}
