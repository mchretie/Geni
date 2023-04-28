package ulb.infof307.g01.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IndulgentValidatorTest {

    String cardAnswer = "L'Égypte";
    String userAnswer = "egypte";
    IndulgentValidator indulgentValidator;

    @BeforeEach
    void init() {
        indulgentValidator = new IndulgentValidator();
    }

    @Test
    void addTolerance_WithAccents_WordsEqual() {
        assertEquals("egypte", indulgentValidator.addTolerance("égypte"));
    }

    @Test
    void addTolerance_WithDeterminers_WordsEqual() {
        assertEquals("egypte", this.indulgentValidator.addTolerance("l'egypte"));
    }

    @Test
    void addTolerance_WithAccentsAndDeterminers_WordsEqual() {
        assertEquals(this.userAnswer, this.indulgentValidator.addTolerance(cardAnswer));
    }

    @Test
    void areEqual_WithAccentsAndDeterminers_WordsEqual() {
        assertTrue(this.indulgentValidator.areEqual(this.cardAnswer, this.userAnswer));
    }
}