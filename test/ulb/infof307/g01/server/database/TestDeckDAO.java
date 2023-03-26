package ulb.infof307.g01.server.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.server.database.dao.DeckDAO;
import ulb.infof307.g01.server.database.exceptions.DatabaseException;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;

import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestDeckDAO extends DatabaseUsingTest {

    DeckDAO deckDAO = DeckDAO.singleton();
    private final UUID id = UUID.randomUUID();

    @Override
    @BeforeEach
    void init() throws SQLException, DatabaseException {
        super.init();
        db.initTables(DatabaseScheme.CLIENT);
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
        deckDAO.saveDeck(deck, id);
        assertTrue(deckDAO.deckNameExists("name"));
    }

    @Test
    void isDeckValid_DeckInvalid_ReturnsFalse() throws SQLException {
        Deck deck1 = new Deck("name");
        Deck deck2 = new Deck("name");
        deckDAO.saveDeck(deck1, id);

        assertFalse(deckDAO.isDeckValid(deck2, id));
    }

    @Test
    void isDeckValid_DeckValid_ReturnsTrue() throws SQLException {
        Deck deck = new Deck("name");
        assertTrue(deckDAO.isDeckValid(deck, id));

        deckDAO.saveDeck(deck, id);
        assertTrue(deckDAO.isDeckValid(deck, id));
    }

    @Test
    void saveDeck_DecksWithSameNameDiffId_OnlyFirstAdded() throws SQLException {
        Deck deck1 = new Deck("name");
        Deck deck2 = new Deck("name");

        deckDAO.saveDeck(deck1, id);
        deckDAO.saveDeck(deck2, id);

        assertEquals(Set.of(deck1), new HashSet<>(deckDAO.getAllDecks()));
    }

    @Test
    void saveDeck_DeckNotExists_CreatesDeck() throws SQLException {
        Deck deck = new Deck("name");
        deckDAO.saveDeck(deck, id);

        assertEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_DeckNameChanged_RenameDeck() throws SQLException {
        Deck deck = new Deck("name");
        deckDAO.saveDeck(deck, id);

        deck.setName("name_01");
        deckDAO.saveDeck(deck, id);

        assertEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_DeckNameNotUpdated_DeckNotUpdated() throws SQLException {
        Deck deck = new Deck("name");
        deckDAO.saveDeck(deck, id);

        deck.setName("name_01");

        assertNotEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_CardAdded_DeckAddedWithCard() throws SQLException {
        Deck deck = new Deck("name");
        Card card = new Card("front", "back");

        deck.addCard(card);
        deckDAO.saveDeck(deck, id);

        assertEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_CardDeleted_DeckUpdated() throws SQLException {
        Deck deck = new Deck("name");
        Card card = new Card("front", "back");

        deck.addCard(card);
        deckDAO.saveDeck(deck, id);
        deck.removeCard(card);
        deckDAO.saveDeck(deck, id);

        assertEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_TagAdded_DeckUpdated() throws SQLException {
        Deck deck = new Deck("name");
        Tag tag = new Tag("name");

        deck.addTag(tag);
        deckDAO.saveDeck(deck, id);

        assertEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_TagRemoved_DeckUpdated() throws SQLException {
        Deck deck = new Deck("name");
        Tag tag = new Tag("name");

        deck.addTag(tag);
        deckDAO.saveDeck(deck, id);
        deck.removeTag(tag);
        deckDAO.saveDeck(deck, id);

        assertEquals(deck, deckDAO.getDeck(deck.getId()));
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
            deckDAO.saveDeck(d, id);
        }

        assertEquals(new HashSet<>(decks), new HashSet<>(deckDAO.getAllDecks()));
    }

    @Test
    void getAllDecks_SameDeckAddedMultipleTimes_OneReturned() throws SQLException {
        Deck deck = new Deck("name");
        deckDAO.saveDeck(deck, id);
        deckDAO.saveDeck(deck, id);
        deckDAO.saveDeck(deck, id);

        assertEquals(Set.of(deck), new HashSet<>(deckDAO.getAllDecks()));
    }

    @Test
    void deleteDeck_DeckExists_DeckNotExists() throws SQLException {
        Deck deck = new Deck("name");
        deckDAO.saveDeck(deck, id);
        deckDAO.deleteDeck(deck.getId(), id);

        assertNull(deckDAO.getDeck(deck.getId()));
    }

    @Test
    void deleteDeck_DeckNotExists_NoThrow() {
        Deck deck = new Deck("name");
        assertDoesNotThrow(() -> deckDAO.deleteDeck(deck.getId(), id));
    }

    @Test
    void searchTags() throws SQLException {
        List<Deck> decks = new ArrayList<>();
        decks.add(new Deck("name1"));
        decks.add(new Deck("name2"));
        decks.add(new Deck("name3"));

        for (Deck d : decks) {
            deckDAO.saveDeck(d, id);
        }

        assertEquals(new HashSet<>(decks), new HashSet<>(deckDAO.searchDecks("name")));
    }
}
