package ulb.infof307.g01.model;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;


public class IndulgentValidator {
    final Iterable<String> determiners = List.of("le",
                                                 "la",
                                                 "l'", 
                                                 "les",
                                                 "de",
                                                 "du",
                                                 "des");

    private String removeAccents(String text) {
        return StringUtils.stripAccents(text);
    }

    private String removeDeterminers(String text) {
        for (String determiner : determiners) {
            text = StringUtils.remove(text, determiner).strip();
        }
        return text;
    }

    public String addTolerance(String text) {
        text = text.toLowerCase();
        text = this.removeAccents(text);
        text = this.removeDeterminers(text);
        return text;
    }

    public boolean areEqual(String text1, String text2) {
        String cardAnswerWithTol = this.addTolerance(text1);
        String userAnswerWithTol = this.addTolerance(text2);

        return Objects.equals(cardAnswerWithTol, userAnswerWithTol);
    }
}
