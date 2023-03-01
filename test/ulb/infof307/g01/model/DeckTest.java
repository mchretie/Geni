package ulb.infof307.g01.model;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void inOrder() {
        Card c = new Card("Front", "Back");
        Card c1 = new Card("Front1", "Back1");
        Card c2 = new Card("Front2", "Back2");

        Card[] order = new Card[]{c, c1, c2};

        Deck deck = new Deck("Test");

        deck.addCard(c);
        deck.addCard(c1);
        deck.addCard(c2);

        int index = 0;
        for (Card card : deck.inOrder()) {
            assertEquals(order[index].getFront(), card.getFront());
            ++index;
        }
    }

    @Test
    void randomOrder() {
    }
}