package ulb.infof307.g01.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    void addCard_FromInit_SizeIncrease() {
        Deck deck = new Deck();

        assertEquals(0, deck.size());

        deck.addCard(new Card("Front", "Back"));

        assertEquals(1, deck.size());
    }

    @Test
    void removeCard_FromInit_SizeDecrease() {
        Deck deck = new Deck();
        Card card = new Card("Front", "Back");

        deck.addCard(card);

        assertEquals(1, deck.size());

        deck.removeCard(card);

        assertEquals(0, deck.size());
    }

    @Test
    void size_FromInit_IsZero() {
        Deck deck = new Deck();
        assertEquals(0, deck.size());
    }
}