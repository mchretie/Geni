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
        indulgentValidator = new IndulgentValidator(this.cardAnswer, this.userAnswer);
    }

    @Test
    void removeAccents() {
        assertEquals("l'egypte", this.indulgentValidator.removeAccents("l'égypte"));
    }

    @Test
    void removeDeterminers() {
        assertEquals("égypte", this.indulgentValidator.removeDeterminers("l'égypte"));
    }

    @Test
    void addTolerance() {
        assertEquals(this.userAnswer, this.indulgentValidator.addTolerance(cardAnswer));
    }

    @Test
    void isAnswerValid() {
        assertTrue(this.indulgentValidator.isAnswerValid());
    }
}