package ulb.infof307.g01.model.card;

import com.google.gson.annotations.Expose;
import ulb.infof307.g01.model.card.visitor.CardVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MCQCard extends TimedCard {

    @Expose
    private final List<String> choices;
    @Expose
    private int correctChoice;

    public final int MIN_CHOICES = 2;
    public final int MAX_CHOICES = 4;

    public MCQCard() {
        super();
        this.choices = new ArrayList<>();
        this.choices.add("Réponse 1");
        this.choices.add("Réponse 2");
        this.correctChoice = 0;
        this.cardType = "MCQCard";
    }

    public MCQCard(UUID uuid, UUID deckId, String front, List<String> choices, int correctChoice, Integer countdownTime) {
        super(uuid, deckId, front, countdownTime);
        this.choices = choices;
        this.correctChoice = correctChoice;
        this.cardType = "MCQCard";
    }

    @Override
    public <T extends Throwable> void accept(CardVisitor<T> visitor) throws T {
        visitor.visit(this);
    }

    private void checkIndexArg(int index) throws IllegalArgumentException {
        if (!isValidIndex(index))
            throw new IllegalArgumentException("Invalid index given");
    }

    public boolean canRemoveChoice() {
        return getChoicesCount() > MIN_CHOICES;
    }

    public boolean canAddChoice() {
        return getChoicesCount() < MAX_CHOICES;
    }

    public boolean isValidIndex(int index) {
        return index < getChoicesCount() && index >= 0;
    }

    public int getChoicesCount() {
        return choices.size();
    }

    public String getChoice(int index) throws IllegalArgumentException {
        checkIndexArg(index);
        return choices.get(index);
    }

    public void addChoice(String choice) throws IllegalStateException {
        if (!canAddChoice())
            throw new IllegalStateException(
                    "Cannot add more choices than %d".formatted(MAX_CHOICES));
        this.choices.add(choice);
    }

    public void removeChoice(int index) throws IllegalStateException {
        if (!canRemoveChoice())
            throw new IllegalStateException(
                    "Cannot have less choices than %d".formatted(MIN_CHOICES));

        this.choices.remove(index);
        if (this.correctChoice >= index)
            this.correctChoice = Math.max(correctChoice - 1, 0);
    }

    public void setChoice(int index, String choice) throws IllegalArgumentException {
        checkIndexArg(index);
        this.choices.set(index, choice);
    }

    public void setCorrectChoice(int correctChoice) throws IllegalArgumentException {
        checkIndexArg(correctChoice);
        this.correctChoice = correctChoice;
    }

    public int getCorrectChoiceIndex() {
        return correctChoice;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(),
                this.getDeckId(),
                this.getFront(),
                choices,
                correctChoice);
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

        if( !super.equals(o) ) return false;

        MCQCard other = (MCQCard) o;
        return id.equals(other.getId())
                && (deckId == other.getDeckId() || deckId.equals(other.getDeckId()))
                && front.equals(other.getFront())
                && choices.equals(other.choices)
                && correctChoice == other.getCorrectChoiceIndex();
    }
}
