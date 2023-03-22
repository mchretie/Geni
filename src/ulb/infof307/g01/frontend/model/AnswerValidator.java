package ulb.infof307.g01.frontend.model;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;


public class AnswerValidator {

    private final String cardAnswer;
    private final String userAnswer;
    public final List<String> DETERMINERS = List.of("le", "la", "l'", "les", "de", "du", "des");


    AnswerValidator(String cardAnswer, String userAnswer) {
        this.cardAnswer = cardAnswer;
        this.userAnswer = userAnswer;
    }

    String removeAccents(String text) {
        return StringUtils.stripAccents(text);
    }

    String removeDeterminer(String text) {
        for (String determiner : this.DETERMINERS) {
            text = StringUtils.remove(text, determiner);
        }
        return text;
    }

    String addTolerance(String text) {
        text = text.toLowerCase();
        text = this.removeAccents(text);
        text = this.removeDeterminer(text);
        return text;
    }

    boolean isAnswerValid() {
        String cardAnswerWithTol = this.addTolerance(this.cardAnswer);
        String userAnswerWithTol = this.addTolerance(this.userAnswer);

        return Objects.equals(cardAnswerWithTol, userAnswerWithTol);
    }

}
