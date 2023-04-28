package ulb.infof307.g01.model.card;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestMCQCard {

    @Test
    void isValidIndex_IndexNotValid_ReturnsFalse() {
        MCQCard card = new MCQCard();
        int choicesCount = card.getChoicesCount();

        for (int i = 0; i < choicesCount; ++i)
            assertTrue(card.isValidIndex(i));

        assertFalse(card.isValidIndex(-1));
        assertFalse(card.isValidIndex(choicesCount));
    }

    @Test
    void canRemoveChoice_MinChoices_ReturnsFalse() {
        MCQCard card = new MCQCard();

        while(card.canRemoveChoice())
            card.removeChoice(0);

        assertEquals(card.MIN_CHOICES, card.getChoicesCount());
        assertFalse(card.canRemoveChoice());
    }

    @Test
    void canAddChoice_MaxChoices_ReturnsFalse() {
        MCQCard card = new MCQCard();

        while(card.canAddChoice())
            card.addChoice("dummy");

        assertEquals(card.MAX_CHOICES, card.getChoicesCount());
        assertFalse(card.canAddChoice());
    }

    @Test
    void removeChoice_MinReached_Throws() {
        MCQCard card = new MCQCard();

        while(card.canRemoveChoice())
            card.removeChoice(0);

        assertThrows(IllegalStateException.class, () -> card.removeChoice(0));
    }

    @Test
    void addChoice_MaxReached_Throws() {
        MCQCard card = new MCQCard();

        while(card.canAddChoice())
            card.addChoice("dummy");

        assertThrows(IllegalStateException.class, () -> card.addChoice("dummy"));
    }

    @Test
    void getChoice_InvalidIndex_Throws() {
        MCQCard card = new MCQCard();
        assertThrows(IllegalArgumentException.class, () ->card.getChoice(-1));
    }

    @Test
    void setChoice_InvalidIndex_Throws() {
        MCQCard card = new MCQCard();
        assertThrows(IllegalArgumentException.class, () ->card.setChoice(-1, "dummy"));
    }

    @Test
    void setCorrectChoice_InvalidIndex_Throws() {
        MCQCard card = new MCQCard();
        assertThrows(IllegalArgumentException.class, () ->card.setCorrectChoice(-1));
    }

    @Test
    void setCorrectChoice_Set_IsUpdated() {
        MCQCard card = new MCQCard();
        card.setCorrectChoice(0);
        assertEquals(0, card.getCorrectChoiceIndex());
    }

    @Test
    void removeChoice_NotTheCorrectChoice_CorrectChoiceIsSame() {
        MCQCard card = new MCQCard();

        int idx = 1;
        String answer = "expect";

        card.addChoice("dummy");
        card.setChoice(idx, answer);
        card.setCorrectChoice(idx);
        card.removeChoice(0);

        assertEquals(answer, card.getChoice(card.getCorrectChoiceIndex()));
    }

    @Test
    void removeChoice_IsCorrectChoiceAndLast_CorrectChoiceIndexStillValid() {
        MCQCard card = new MCQCard();

        card.addChoice("dummy");
        card.setCorrectChoice(card.getChoicesCount()-1);
        card.removeChoice(card.getChoicesCount()-1);

        assertTrue(card.isValidIndex(card.getCorrectChoiceIndex()));
    }
}