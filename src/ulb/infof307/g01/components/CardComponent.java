package ulb.infof307.g01.components;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

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

    @Override public VBox getPane()
    {
        VBox view = new VBox();
        view.getChildren().add(new Label(question));
        view.getChildren().add(new Label(answer));
        return view;
    }
}
