package ulb.infof307.g01.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    Card cardVeryGood;
    Card cardUnseen;

    @BeforeEach
    void init() {
        cardUnseen = new Card("", "", Card.KnowledgeLevel.NEVER_SEEN);
        cardVeryGood = new Card("", "", Card.KnowledgeLevel.VERY_GOOD);
    }
    @Test
    void compareToTest() {
        Card cardVeryGoodBis = new Card("bis", "bis", Card.KnowledgeLevel.VERY_GOOD);
        assertEquals(1, cardVeryGood.compareTo(cardUnseen));
        assertEquals(0, cardVeryGood.compareTo(cardVeryGoodBis));
        assertEquals(-1, cardUnseen.compareTo(cardVeryGood));
    }
}