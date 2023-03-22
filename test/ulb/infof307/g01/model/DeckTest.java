package ulb.infof307.g01.model;

import org.junit.jupiter.api.Test;
import ulb.infof307.g01.frontend.model.Card;
import ulb.infof307.g01.frontend.model.Deck;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeckTest {

    @Test
    void addCard_FromInit_SizeIncrease() {
        Deck deck = new Deck("Test");

        assertEquals(0, deck.cardCount());

        deck.addCard(new Card("Front", "Back"));

        assertEquals(1, deck.cardCount());
    }

    @Test
    void removeCard_FromInit_SizeDecrease() {
        Deck deck = new Deck("Test");
        Card card = new Card("Front", "Back");

        deck.addCard(card);

        assertEquals(1, deck.cardCount());

        deck.removeCard(card);

        assertEquals(0, deck.cardCount());
    }

    @Test
    void cardCount_FromInit_IsZero() {
        Deck deck = new Deck("Test");
        assertEquals(0, deck.cardCount());
    }
}