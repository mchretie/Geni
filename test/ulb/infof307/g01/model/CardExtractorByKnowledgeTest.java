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

    @BeforeEach
    void init() {
        deck = new Deck("sorted deck test");

        cardUnseen = new Card("", "", Card.KnowledgeLevel.NEVER_SEEN);
        cardVeryBad = new Card("", "", Card.KnowledgeLevel.VERY_BAD);
        cardBad = new Card("", "", Card.KnowledgeLevel.BAD);
        cardAverage = new Card("", "", Card.KnowledgeLevel.AVERAGE);
        cardGood = new Card("", "", Card.KnowledgeLevel.GOOD);
        cardVeryGood = new Card("", "", Card.KnowledgeLevel.VERY_GOOD);

        deck.addCard(cardGood);
        deck.addCard(cardUnseen);
        deck.addCard(cardVeryBad);
        deck.addCard(cardVeryGood);
        deck.addCard(cardAverage);
        deck.addCard(cardBad);
    }

    @Test
    void sortDeckTest() {
        CardExtractorByKnowledge t = new CardExtractorByKnowledge(deck);

        ArrayList<Card> rightOrder = new ArrayList<>(Arrays.asList(cardUnseen, cardVeryBad, cardBad, cardAverage, cardGood, cardVeryGood));
        assertEquals(rightOrder, t.getSortedCards());
    }

    @Test
    void getNextCardTest() {
        CardExtractorByKnowledge t = new CardExtractorByKnowledge(deck);

        assertEquals(cardUnseen, t.getNextCard());
        assertEquals(cardVeryBad, t.getNextCard());
        assertEquals(cardBad, t.getNextCard());
        assertEquals(cardAverage, t.getNextCard());
        assertEquals(cardGood, t.getNextCard());
        assertEquals(cardVeryGood, t.getNextCard());
        assertEquals(cardUnseen, t.getNextCard());

    }
}