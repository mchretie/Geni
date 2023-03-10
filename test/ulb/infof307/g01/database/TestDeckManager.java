package ulb.infof307.g01.database;

import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Card;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.SQLException;
import java.util.*;

public class TestDeckManager extends DatabaseUsingTest {

    DeckManager deckManager = DeckManager.singleton();

    @Override
    @BeforeEach
    void init() throws SQLException, OpenedDatabaseException {
        super.init();
        db.initTables(DatabaseScheme.CLIENT);
    }

    @Test
    void getDeck_DeckNotExists_ThrowsException() {
        assertEquals(null, deckManager.getDeck(new Deck("name").getId()));
    }

    @Test
    void saveDeck_DeckNotExists_CreatesDeck() {
        Deck deck = new Deck("name");
        deckManager.saveDeck(deck);

        assertEquals(deck, deckManager.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_DeckNameChanged_RenameDeck() {
        Deck deck = new Deck("name");
        deckManager.saveDeck(deck);

        deck.setName("name_01");
        deckManager.saveDeck(deck);

        assertEquals(deck, deckManager.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_DeckNameNotUpdated_DeckNotUpdated() {
        Deck deck = new Deck("name");
        deckManager.saveDeck(deck);

        deck.setName("name_01");

        assertNotEquals(deck, deckManager.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_CardAdded_DeckAddedWithCard() {
        Deck deck = new Deck("name");
        Card card = new Card("front", "back");

        deck.addCard(card);
        deckManager.saveDeck(deck);

        assertEquals(deck, deckManager.getDeck(deck.getId()));
    }

    @Test
    void saveDeck_CardDeleted_DeckUpdated() {
        Deck deck = new Deck("name");
        Card card = new Card("front", "back");

        deck.addCard(card);
        deckManager.saveDeck(deck);
        deck.removeCard(card);
        deckManager.saveDeck(deck);

        assertEquals(deck, deckManager.getDeck(deck.getId()));
    }

    @Test
    void getAllDecks_NoDecks_EmptyList() {
        assertTrue(deckManager.getAllDecks().isEmpty());
    }

    @Test
    void getAllDecks_ManyDecks_AllReturned() {
        List<Deck> decks = new ArrayList();
        decks.add(new Deck("name1"));
        decks.add(new Deck("name2"));
        decks.add(new Deck("name3"));

        decks.forEach((d) -> deckManager.saveDeck(d));

        assertEquals(new HashSet(decks), new HashSet(deckManager.getAllDecks()));
    }

    @Test
    void getAllDecks_SameDeckAddedMultipleTimes_OneReturned() {
        Deck deck = new Deck("name");
        deckManager.saveDeck(deck);
        deckManager.saveDeck(deck);
        deckManager.saveDeck(deck);

        assertEquals(Set.of(deck), new HashSet(deckManager.getAllDecks()));
    }

    @Test
    void deleteDeck_DeckExists_DeckNotExists() {
        Deck deck = new Deck("name");
        deckManager.saveDeck(deck);
        deckManager.deleteDeck(deck);

        assertEquals(null, deckManager.getDeck(deck.getId()));
    }

    @Test
    void deleteDeck_DeckNotExists_NoThrow() {
        Deck deck = new Deck("name");
        assertDoesNotThrow(() -> deckManager.deleteDeck(deck));
    }
}
