package ulb.infof307.g01.database;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Card;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestDeckManager {
    DeckManager dm = DeckManager.singleton();
    CardManager cm = CardManager.singleton();

    @Test
    void getDeck_DeckExists_ReturnDeck() {
        dm.createDeck("testExists");
        assertEquals(dm.getDeck("testExists").getName(), new Deck("test").getName());
        dm.delDeck(dm.getDeck("testExists"));
    }

    @Test
    void getDeck_DeckNotExists_ThrowsException() {
        assertThrows(DeckNotExistsException.class, () -> dm.getDeck("testNotExists"));
    }

    @Test
    void getAllDecks_ReturnListDeck() {
        dm.createDeck("test1");
        dm.createDeck("test2");
        assertEquals(dm.getAllDecks().size(), 2);
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
    void addToDeck_DeckExistsAndCardsNotInDeck_CardsInDeck() {
        dm.createDeck("testAdd");
        Deck deck = dm.getDeck("testAdd");
        dm.addToDeck(deck, List.of(new Card("front", "back"), new Card("front2", "back2")));
        assertEquals(cm.getCardsFrom(deck.getId()).size(), 2);
        dm.delDeck(deck);
    }

    @Test
    void addToDeck_DeckNotExistsOrCardsInDeck_ThrowsException() {
        assertThrows(DeckNotExistsException.class, () -> dm.addToDeck(new Deck("testNotExists"), List.of(new Card("front", "back"))));
    }

    @Test
    void delDeck_DeckExists_DeckNotInDB() {
        dm.createDeck("testDel");
        Deck deck = dm.getDeck("testDel");
        dm.delDeck(deck);
        assertThrows(DeckNotExistsException.class, () -> dm.getDeck("testDel"));
    }

    @Test
    void delDeck_DeckNotExists_ThrowsException() {
        assertThrows(DeckNotExistsException.class, () -> dm.delDeck(new Deck("testNotExists")));
    }

}
