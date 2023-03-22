package ulb.infof307.g01.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ulb.infof307.g01.frontend.model.AnswerValidator;

import static org.junit.jupiter.api.Assertions.*;

class AnswerValidatorTest {

    String cardAnswer = "L'Égypte";
    String userAnswer = "egypte";
    AnswerValidator answerValidator;

    @BeforeEach
    void init() {
        answerValidator = new AnswerValidator(this.cardAnswer, this.userAnswer);
    }

    @Test
    void removeAccents() {
        assertEquals("l'egypte", this.answerValidator.removeAccents("l'égypte"));
    }

    @Test
    void removeDeterminers() {
        assertEquals("égypte", this.answerValidator.removeDeterminer("l'égypte"));
    }

    @Test
    void addTolerance() {
        assertEquals(this.userAnswer, this.answerValidator.addTolerance(cardAnswer));
    }

    @Test
    void isAnswerValid() {
        assertTrue(this.answerValidator.isAnswerValid());
    }
}