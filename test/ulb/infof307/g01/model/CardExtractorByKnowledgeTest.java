package ulb.infof307.g01.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CardExtractorByKnowledgeTest {

    Deck deck;
    Card cardUnseen;
    Card cardVeryBad;
    Card cardBad;
    Card cardAverage;
    Card cardGood;
    Card cardVeryGood;

    ArrayList<Card> rightOrder;


    @BeforeEach
    void init() {
        deck = new Deck("sorted deck test");

        cardUnseen = new FlashCard("", "", Card.KnowledgeLevel.NEVER_SEEN);
        cardVeryBad = new FlashCard("", "", Card.KnowledgeLevel.VERY_BAD);
        cardBad = new FlashCard("", "", Card.KnowledgeLevel.BAD);
        cardAverage = new FlashCard("", "", Card.KnowledgeLevel.AVERAGE);
        cardGood = new FlashCard("", "", Card.KnowledgeLevel.GOOD);
        cardVeryGood = new FlashCard("", "", Card.KnowledgeLevel.VERY_GOOD);

        deck.addCard(cardGood);
        deck.addCard(cardUnseen);
        deck.addCard(cardVeryBad);
        deck.addCard(cardVeryGood);
        deck.addCard(cardAverage);
        deck.addCard(cardBad);

        rightOrder = new ArrayList<>(Arrays.asList(cardUnseen, cardVeryBad, cardBad, cardAverage, cardGood, cardVeryGood));

    }

    @Test
    void sortDeckTest() {
        CardExtractorByKnowledge extractor = new CardExtractorByKnowledge(deck);

        ArrayList<Card> rightOrder = new ArrayList<>(Arrays.asList(cardUnseen, cardVeryBad, cardBad, cardAverage, cardGood, cardVeryGood));
        assertEquals(rightOrder, extractor.getSortedCards());
    }

    @Test
    void iteratorTest() {
        CardExtractorByKnowledge extractor = new CardExtractorByKnowledge(deck);

        int currentIndex = 0;

        for (Card c : extractor) {
            assertEquals(c.getKnowledge(), rightOrder.get(currentIndex).getKnowledge());
            currentIndex++;
        }
    }

    @Test
    void getPreviousAndNextCardTest() {
        CardExtractorByKnowledge t = new CardExtractorByKnowledge(deck);

        assertEquals(cardUnseen, t.getNextCard());
        assertNull(t.getPreviousCard());

        assertEquals(cardVeryBad, t.getNextCard());
        assertEquals(cardUnseen, t.getPreviousCard());
        assertEquals(cardVeryBad, t.getNextCard());

        assertEquals(cardBad, t.getNextCard());
        assertEquals(cardAverage, t.getNextCard());
        assertEquals(cardGood, t.getNextCard());
        assertEquals(cardVeryGood, t.getNextCard());
        assertNull(t.getNextCard());
    }
}