package ulb.infof307.g01.database;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Card;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestDeckManager {
    DeckManager dm = DeckManager.singleton();
    CardManager cm = CardManager.singleton();

    @Test
    void getDeck_DeckExists_ReturnDeck() throws DatabaseNotInitException, DeckNotExistsException {
        Deck deck = dm.createDeck("testExists");
        assertEquals(deck.getName(), new Deck("testExists").getName());
        dm.delDeck(deck);
    }

    @Test
    void getDeck_DeckNotExists_ThrowsException() {
        assertThrows(DeckNotExistsException.class, () -> dm.getDeck(new Deck("testNotExists").getId()));
    }

    @Test
    void getAllDecks_ReturnListDeck() throws DeckNotExistsException, DatabaseNotInitException {
        Deck deck1 = dm.createDeck("test1");
        Deck deck2 = dm.createDeck("test2");
        assertEquals(dm.getAllDecks().size(), 2);
        assertEquals(dm.getAllDecks().get(0).getId(), deck1.getId());
        dm.delDeck(deck1);
        dm.delDeck(deck2);
    }

    // we using uuids so obsolete tests ?
//    @Test void createDeck_DeckUnique_DeckInDB(){
//        dm.createDeck("testUnique");
//        assertEquals(dm.getDeck("testUnique").getName(), new Deck("testUnique").getName());
//        dm.delDeck(dm.getDeck("testUnique"));
//    }
//
//    @Test void createDeck_DeckNotUnique_ThrowsException(){
//
//    }

    @Test
    void addToDeck_DeckExistsAndCardsNotInDeck_CardsInDeck() throws DatabaseNotInitException, DeckNotExistsException {
        Deck deck = dm.createDeck("testAdd");
        dm.addToDeck(deck, List.of(new Card("front", "back"), new Card("front2", "back2")));
        assertEquals(cm.getCardsFrom(deck.getId()).size(), 2);
        dm.delDeck(deck);
    }

    @Test
    void addToDeck_DeckNotExistsOrCardsInDeck_ThrowsException() {
        assertThrows(DeckNotExistsException.class, () -> dm.addToDeck(new Deck("testNotExists"), List.of(new Card("front", "back"))));
    }

    @Test
    void delDeck_DeckExists_DeckNotInDB() throws DatabaseNotInitException, DeckNotExistsException {
        Deck deck = dm.createDeck("testDel");
        UUID uuid = deck.getId();
        dm.delDeck(deck);
        assertThrows(DeckNotExistsException.class, () -> dm.getDeck(uuid));
    }

    @Test
    void delDeck_DeckNotExists_ThrowsException() {
        assertThrows(DeckNotExistsException.class, () -> dm.delDeck(new Deck("testNotExists")));
    }

}
