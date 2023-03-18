package ulb.infof307.g01.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.database.exceptions.DatabaseException;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TestDeckDAO extends DatabaseUsingTest {

    DeckDAO deckDAO = DeckDAO.singleton();

    @Override
    @BeforeEach
    void init() throws SQLException, DatabaseException {
        super.init();
        db.initTables(DatabaseScheme.CLIENT);
    }

    @Test
    void getDeck_DeckNotExists_ThrowsException() {
        assertNull(deckDAO.getDeck(new Deck("name").getId()));
    }

    @Test
    void deckNameExists_NameNotExists_ReturnsFalse() {
        assertFalse(deckDAO.deckNameExists("name"));
    }

    @Test
    void deckNameExists_NameExists_ReturnsTrue() {
        Deck deck = new Deck("name");
        deckDAO.saveDeck(deck);

        assertTrue(deckDAO.deckNameExists("name"));
    }

    @Test
    void isDeckValid_DeckInvalid_ReturnsFalse() {
        Deck deck1 = new Deck("name");
        Deck deck2 = new Deck("name");
        deckDAO.saveDeck(deck1);

        assertFalse(deckDAO.isDeckValid(deck2));
    }

    @Test
    void isDeckValid_DeckValid_ReturnsTrue() {
        Deck deck = new Deck("name");
        assertTrue(deckDAO.isDeckValid(deck));

        deckDAO.saveDeck(deck);
        assertTrue(deckDAO.isDeckValid(deck));
    }

    @Test
    void saveDeck_DecksWithSameNameDiffId_OnlyFirstAdded() {
        Deck deck1 = new Deck("name");
        Deck deck2 = new Deck("name");

        deckDAO.saveDeck(deck1);
        deckDAO.saveDeck(deck2);

        assertEquals(Set.of(deck1), new HashSet<>(deckDAO.getAllDecks()));
    }

    @Test
    void saveDeck_DeckNotExists_CreatesDeck() {
        Deck deck = new Deck("name");
        deckDAO.saveDeck(deck);

        assertEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_DeckNameChanged_RenameDeck() {
        Deck deck = new Deck("name");
        deckDAO.saveDeck(deck);

        deck.setName("name_01");
        deckDAO.saveDeck(deck);

        assertEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_DeckNameNotUpdated_DeckNotUpdated() {
        Deck deck = new Deck("name");
        deckDAO.saveDeck(deck);

        deck.setName("name_01");

        assertNotEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_CardAdded_DeckAddedWithCard() {
        Deck deck = new Deck("name");
        Card card = new Card("front", "back");

        deck.addCard(card);
        deckDAO.saveDeck(deck);

        assertEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_CardDeleted_DeckUpdated() {
        Deck deck = new Deck("name");
        Card card = new Card("front", "back");

        deck.addCard(card);
        deckDAO.saveDeck(deck);
        deck.removeCard(card);
        deckDAO.saveDeck(deck);

        assertEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_TagAdded_DeckUpdated() {
        Deck deck = new Deck("name");
        Tag tag = new Tag("name");

        deck.addTag(tag);
        deckDAO.saveDeck(deck);

        assertEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_TagRemoved_DeckUpdated() {
        Deck deck = new Deck("name");
        Tag tag = new Tag("name");

        deck.addTag(tag);
        deckDAO.saveDeck(deck);
        deck.removeTag(tag);
        deckDAO.saveDeck(deck);

        assertEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void getAllDecks_NoDecks_EmptyList() {
        assertTrue(deckDAO.getAllDecks().isEmpty());
    }

    @Test
    void getAllDecks_ManyDecks_AllReturned() {
        List<Deck> decks = new ArrayList<>();
        decks.add(new Deck("name1"));
        decks.add(new Deck("name2"));
        decks.add(new Deck("name3"));

        decks.forEach((d) -> deckDAO.saveDeck(d));

        assertEquals(new HashSet<>(decks), new HashSet<>(deckDAO.getAllDecks()));
    }

    @Test
    void getAllDecks_SameDeckAddedMultipleTimes_OneReturned() {
        Deck deck = new Deck("name");
        deckDAO.saveDeck(deck);
        deckDAO.saveDeck(deck);
        deckDAO.saveDeck(deck);

        assertEquals(Set.of(deck), new HashSet<>(deckDAO.getAllDecks()));
    }

    @Test
    void deleteDeck_DeckExists_DeckNotExists() {
        Deck deck = new Deck("name");
        deckDAO.saveDeck(deck);
        deckDAO.deleteDeck(deck);

        assertNull(deckDAO.getDeck(deck.getId()));
    }

    @Test
    void deleteDeck_DeckNotExists_NoThrow() {
        Deck deck = new Deck("name");
        assertDoesNotThrow(() -> deckDAO.deleteDeck(deck));
    }

    @Test
    void searchTags() {
        List<Deck> decks = new ArrayList<>();
        decks.add(new Deck("name1"));
        decks.add(new Deck("name2"));
        decks.add(new Deck("name3"));

        decks.forEach((d) -> deckDAO.saveDeck(d));

        assertEquals(new HashSet<>(decks), new HashSet<>(deckDAO.searchDecks("name")));
    }
}
