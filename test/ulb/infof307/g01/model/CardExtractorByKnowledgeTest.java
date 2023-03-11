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

        rightOrder = new ArrayList<>(Arrays.asList(cardUnseen, cardVeryBad, cardBad, cardAverage, cardGood, cardVeryGood));

    }

    @Test
    void sortDeckTest() {
        CardExtractorByKnowledge extractor = new CardExtractorByKnowledge(deck);

        ArrayList<Card> rightOrder = new ArrayList<>(Arrays.asList(cardUnseen, cardVeryBad, cardBad, cardAverage, cardGood, cardVeryGood));
        assertEquals(rightOrder, extractor.getSortedCards());
    }

    @Test
    void getNextCardTest() {
        CardExtractorByKnowledge extractor = new CardExtractorByKnowledge(deck);

        int currentIndex = 0;

        for (Card c : extractor) {
            assertEquals(c.getKnowledge(), rightOrder.get(currentIndex).getKnowledge());
            currentIndex++;
        }
    }
}