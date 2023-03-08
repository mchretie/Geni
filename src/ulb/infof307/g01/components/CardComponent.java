package ulb.infof307.g01.components;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * A component that displays a card upon getPane() call
 * @Param question The question of the card
 * @Param answer The answer of the card
 */
public class CardComponent implements Component {
    final String question;
    final String answer;

    public CardComponent(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public VBox getPane() {
        VBox view = new VBox(100);

        Label questionLabel = new Label(question);
        Label answerLabel = new Label(answer);

        view.getChildren().addAll(questionLabel, answerLabel);
        view.setAlignment(Pos.CENTER);
        return view;
    }
}
