package ulb.infof307.g01.model;

import org.junit.jupiter.api.Test;
import ulb.infof307.g01.model.card.Card;
import ulb.infof307.g01.model.card.FlashCard;
import ulb.infof307.g01.model.card.MCQCard;
import ulb.infof307.g01.model.deck.Deck;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DeckTest {

    @Test
    void addCard_FromInit_SizeIncrease() {
        Deck deck = new Deck("Test");

        assertEquals(0, deck.cardCount());

        deck.addCard(new FlashCard());
        deck.addCard(new MCQCard());

        assertEquals(2, deck.cardCount());
    }

    @Test
    void removeCard_FromInit_SizeDecrease() {
        Deck deck = new Deck("Test");
        Card card = new FlashCard();
        Card card2 = new MCQCard();

        deck.addCard(card);
        deck.addCard(card2);

        assertEquals(2, deck.cardCount());

        deck.removeCard(card);
        deck.removeCard(card2);

        assertEquals(0, deck.cardCount());
    }

    @Test
    void cardCount_FromInit_IsZero() {
        Deck deck = new Deck("Test");
        assertEquals(0, deck.cardCount());
    }

    @Test
    void getMetadata_TwoSameDecks_SameMetadata() {
        Deck deckOne = new Deck("name");
        Deck deckTwo = new Deck("name", deckOne.getId());

        assertEquals(deckOne, deckTwo);
        assertEquals(deckOne.getMetadata(), deckTwo.getMetadata());
    }

    @Test
    void getMetadata_DifferentId_DifferentMetadata() {
        Deck deckOne = new Deck("name");
        Deck deckTwo = new Deck("name");

        assertNotEquals(deckOne.getMetadata(), deckTwo.getMetadata());
    }
}