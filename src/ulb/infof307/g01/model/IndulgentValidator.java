package ulb.infof307.g01.model;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;


public class IndulgentValidator {

    private final String answer;
    private final String userAnswer;
    public final List<String> DETERMINERS = List.of("le", "la", "l'", "les", "de", "du", "des");

    public IndulgentValidator(String answer, String userAnswer) {
        this.answer = answer;
        this.userAnswer = userAnswer;
    }

    String removeAccents(String text) {
        return StringUtils.stripAccents(text);
    }

    String removeDeterminers(String text) {
        for (String determiner : this.DETERMINERS) {
            text = StringUtils.remove(text, determiner);
        }
        return text;
    }

    String addTolerance(String text) {
        text = text.toLowerCase();
        text = this.removeAccents(text);
        text = this.removeDeterminers(text);
        return text;
    }

    boolean isAnswerValid() {
        String cardAnswerWithTol = this.addTolerance(this.answer);
        String userAnswerWithTol = this.addTolerance(this.userAnswer);

        return Objects.equals(cardAnswerWithTol, userAnswerWithTol);
    }

}
