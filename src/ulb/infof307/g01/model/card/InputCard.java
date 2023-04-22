package ulb.infof307.g01.model.card;

import com.google.gson.annotations.Expose;
import ulb.infof307.g01.model.IndulgentValidator;

import java.util.Objects;
import java.util.UUID;

public class InputCard extends Card {

    @Expose
    private String answer;

    public InputCard() {
        super();
        this.answer = "";
        this.cardType = "InputCard";
    }

    public InputCard(UUID uuid, UUID deckId, String front, String answer) {
        super(uuid, deckId, front);
        this.answer = answer;
        this.cardType = "InputCard";
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isInputCorrect(String input) {
        IndulgentValidator validator = new IndulgentValidator();
        return validator.isEquals(this.answer, input);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), this.getDeckId(), this.getFront(), this.answer);
    }

    @Override
    public boolean equals(Object o) {
        UUID id = this.getId();
        UUID deckId = this.getDeckId();
        String front = this.getFront();

        if (o == this)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;

        InputCard other = (InputCard) o;
        return id.equals(other.getId())
                && (deckId == other.getDeckId() || deckId.equals(other.getDeckId()))
                && front.equals(other.getFront()) && this.answer.equals(other.getAnswer());
    }
}
