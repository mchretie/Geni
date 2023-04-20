package ulb.infof307.g01.model.card;

import com.google.gson.annotations.Expose;
import edu.emory.mathcs.backport.java.util.Arrays;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.eclipse.jetty.util.TypeUtil.asList;

public class MCQCard extends Card {

    @Expose
    private final List<String> answers;
    @Expose
    private int correctAnswer;

    public MCQCard() {
        super();
        this.answers = new ArrayList<>();
        this.answers.add("Réponse 1");
        this.answers.add("Réponse 2");

        this.correctAnswer = 0;
        this.cardType = "MCQCard";
    }

    public MCQCard(UUID uuid, UUID deckId, String front, List<String> answers, int correctAnswer) {
        super(uuid, deckId, front);
        this.answers = answers;
        this.correctAnswer = correctAnswer;
        this.cardType = "MCQCard";
    }

    public int getChoiceMax(){
        return 4;
    }

    public boolean isCardMin(){
        return this.answers.size() == 2;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public String getChoice(int index) {
        return answers.get(index);
    }

    public void addAnswer(String answer) {
        this.answers.add(answer);
    }

    public void removeAnswer(int index) {
        if (this.answers.size() <= 2)
            return;

        this.answers.remove(index);

        if (this.correctAnswer == index)
            this.correctAnswer = Math.max(index - 1, 0);
    }

    public void setAnswer(int index, String answer) {
        this.answers.set(index, answer);
    }

    public int getCorrectChoiceIndex() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), this.getDeckId(), this.getFront(), answers, correctAnswer);
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
                && answers.equals(other.getAnswers())
                && correctAnswer == other.getCorrectChoiceIndex();
    }

    public int getNbOfChoices() {
        return answers.size();
    }
}
