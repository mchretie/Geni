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
        deckDAO.saveDeck(deck);

        assertTrue(deckDAO.deckNameExists("name"));
    }

    @Test
    void isDeckValid_DeckInvalid_ReturnsFalse() throws SQLException {
        Deck deck1 = new Deck("name");
        Deck deck2 = new Deck("name");
        deckDAO.saveDeck(deck1);

        assertFalse(deckDAO.isDeckValid(deck2));
    }

    @Test
    void isDeckValid_DeckValid_ReturnsTrue() throws SQLException {
        Deck deck = new Deck("name");
        assertTrue(deckDAO.isDeckValid(deck));

        deckDAO.saveDeck(deck);
        assertTrue(deckDAO.isDeckValid(deck));
    }

    @Test
    void saveDeck_DecksWithSameNameDiffId_OnlyFirstAdded() throws SQLException {
        Deck deck1 = new Deck("name");
        Deck deck2 = new Deck("name");

        deckDAO.saveDeck(deck1);
        deckDAO.saveDeck(deck2);

        assertEquals(Set.of(deck1), new HashSet<>(deckDAO.getAllDecks()));
    }

    @Test
    void saveDeck_DeckNotExists_CreatesDeck() throws SQLException {
        Deck deck = new Deck("name");
        deckDAO.saveDeck(deck);

        assertEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_DeckNameChanged_RenameDeck() throws SQLException {
        Deck deck = new Deck("name");
        deckDAO.saveDeck(deck);

        deck.setName("name_01");
        deckDAO.saveDeck(deck);

        assertEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_DeckNameNotUpdated_DeckNotUpdated() throws SQLException {
        Deck deck = new Deck("name");
        deckDAO.saveDeck(deck);

        deck.setName("name_01");

        assertNotEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_CardAdded_DeckAddedWithCard() throws SQLException {
        Deck deck = new Deck("name");
        Card card = new Card("front", "back");

        deck.addCard(card);
        deckDAO.saveDeck(deck);

        assertEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_CardDeleted_DeckUpdated() throws SQLException {
        Deck deck = new Deck("name");
        Card card = new Card("front", "back");

        deck.addCard(card);
        deckDAO.saveDeck(deck);
        deck.removeCard(card);
        deckDAO.saveDeck(deck);

        assertEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_TagAdded_DeckUpdated() throws SQLException {
        Deck deck = new Deck("name");
        Tag tag = new Tag("name");

        deck.addTag(tag);
        deckDAO.saveDeck(deck);

        assertEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_TagRemoved_DeckUpdated() throws SQLException {
        Deck deck = new Deck("name");
        Tag tag = new Tag("name");

        deck.addTag(tag);
        deckDAO.saveDeck(deck);
        deck.removeTag(tag);
        deckDAO.saveDeck(deck);

        assertEquals(deck, deckDAO.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_MultipleTagsAdded_DeckUpdated() throws SQLException {
        Deck deck1 = new Deck("deck1");
        Deck deck2 = new Deck("deck2");
        Tag tag1 = new Tag("tag");
        Tag tag2 = new Tag("tag");

        deck1.addTag(tag1);
        deckDAO.saveDeck(deck1);

        deck2.addTag(tag2);
        deckDAO.saveDeck(deck2);

        System.out.println(deck1.getTags().get(0).getName());
        System.out.println(deck2.getTags().get(0).getName());

        assertEquals(deck1.getTags().get(0).getId(), deck2.getTags().get(0).getId());
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
            deckDAO.saveDeck(d);
        }

        assertEquals(new HashSet<>(decks), new HashSet<>(deckDAO.getAllDecks()));
    }

    @Test
    void getAllDecks_SameDeckAddedMultipleTimes_OneReturned() throws SQLException {
        Deck deck = new Deck("name");
        deckDAO.saveDeck(deck);
        deckDAO.saveDeck(deck);
        deckDAO.saveDeck(deck);

        assertEquals(Set.of(deck), new HashSet<>(deckDAO.getAllDecks()));
    }

    @Test
    void deleteDeck_DeckExists_DeckNotExists() throws SQLException {
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
    void searchTags() throws SQLException {
        List<Deck> decks = new ArrayList<>();
        decks.add(new Deck("name1"));
        decks.add(new Deck("name2"));
        decks.add(new Deck("name3"));

        for (Deck d : decks) {
            deckDAO.saveDeck(d);
        }

        assertEquals(new HashSet<>(decks), new HashSet<>(deckDAO.searchDecks("name")));
    }
}
