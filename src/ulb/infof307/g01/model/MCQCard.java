package ulb.infof307.g01.model;

import com.google.gson.annotations.Expose;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MCQCard extends Card{
    @Expose
    private List<String> answers;
    @Expose
    private Integer correctAnswer;

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

    public List<String> getAnswers() { return answers; }

    public void setAnswers(List<String> answers) { this.answers = answers; }

    public int getCorrectAnswer() { return correctAnswer; }

    public void setCorrectAnswer(int correctAnswer) { this.correctAnswer = correctAnswer; }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), this.getDeckId(), this.getFront(), answers, correctAnswer);
    }

    @Override
    public boolean equals(Object o)
    {
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

}
