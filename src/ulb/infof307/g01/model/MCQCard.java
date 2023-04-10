package ulb.infof307.g01.model;

import com.google.gson.annotations.Expose;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MCQCard extends Card {

    @Expose
    private List<String> answers;
    @Expose
    private int correctAnswer;

    public MCQCard(String front, List<String> answers, int correctAnswer) {
        super(front);
        this.answers = answers;
        this.correctAnswer = correctAnswer;
        this.cardType = "MCQCard";
    }

    public MCQCard(UUID uuid, UUID deckId, String front, List<String> answers, int correctAnswer) {
        super(uuid, deckId, front);
        this.answers = answers;
        this.correctAnswer = correctAnswer;
        this.cardType = "MCQCard";
    }

    public MCQCard(String front, List<String> answers, int correctAnswer, KnowledgeLevel knowledge) {
        super(front, knowledge);
        this.answers = answers;
        this.correctAnswer = correctAnswer;
        this.cardType = "MCQCard";
    }

    public int getChoiceMax(){
        return 4;
    }

    public int getCardMin(){
        return 2;
    }

    public boolean isCardMin(){
        return this.answers.size() == 2;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public String getAnswer(int index) {
        return answers.get(index);
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public void addAnswer(String answer) {
        this.answers.add(answer);
    }

    public void removeAnswer(int index) {
        // limit answer to min two
        if (this.answers.size() <= 2)
            return;
        this.answers.remove(index);
        if (this.correctAnswer == index)
            this.correctAnswer = Math.max(index - 1, 0);

    }

    public void setAnswer(int index, String answer) {
        this.answers.set(index, answer);
    }

    public int getCorrectAnswer() {
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
                && correctAnswer == other.getCorrectAnswer();
    }

    public int getNbAnswers() {
        return answers.size();
    }
}
