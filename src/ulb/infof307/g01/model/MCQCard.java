package ulb.infof307.g01.model;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class MCQCard extends Card{
    private String[] answers;
    private int correctAnswer;

    public MCQCard(String front, String[] answers, int correctAnswer) {
        super(front);
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }

    public MCQCard(UUID uuid, UUID deckId, String front, String[] answers, int correctAnswer) {
        super(uuid, deckId, front);
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }

    public MCQCard(String front, String[] answers, int correctAnswer, KnowledgeLevel knowledge) {
        super(front, knowledge);
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }

    public String[] getAnswers() { return answers; }

    public void setAnswers(String[] answers) { this.answers = answers; }

    public int getCorrectAnswer() { return correctAnswer; }

    public void setCorrectAnswer(int correctAnswer) { this.correctAnswer = correctAnswer; }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), this.getDeckId(), this.getFront(), Arrays.hashCode(answers), correctAnswer);
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
                && Arrays.equals(answers, other.getAnswers())
                && correctAnswer == other.getCorrectAnswer();
    }

}
