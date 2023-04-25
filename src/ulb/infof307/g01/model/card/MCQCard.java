package ulb.infof307.g01.model.card;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MCQCard extends Card {

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

    public MCQCard(UUID uuid, UUID deckId, String front, List<String> choices, int correctChoice) {
        super(uuid, deckId, front);
        this.choices = choices;
        this.correctChoice = correctChoice;
        this.cardType = "MCQCard";
    }

    public boolean canRemoveChoice() {
        return getChoicesCount() > MIN_CHOICES;
    }

    public boolean canAddChoice() {
        return getChoicesCount() < MAX_CHOICES;
    }

    public boolean isValidIndex(int index) {
        return index < getChoicesCount();
    }

    public int getChoicesCount() {
        return choices.size();
    }

    public String getChoice(int index) {
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
        if (isValidIndex(index))
            throw new IllegalArgumentException(
                    "The choice index must be among the choices");
        this.choices.set(index, choice);
    }

    public void setCorrectChoice(int correctChoice) throws IllegalArgumentException {
        if (isValidIndex(correctChoice))
            throw new IllegalArgumentException(
                    "The correct answer must be among the choices");
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

        MCQCard other = (MCQCard) o;
        return id.equals(other.getId())
                && (deckId == other.getDeckId() || deckId.equals(other.getDeckId()))
                && front.equals(other.getFront())
                && choices.equals(other.choices)
                && correctChoice == other.getCorrectChoiceIndex();
    }
}
