package ulb.infof307.g01.components;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class CardComponent implements Component {
    final String question;
    final String answer;

    public CardComponent(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public VBox getPane() {
        VBox view = new VBox();
        view.getChildren().add(new Label(question));
        view.getChildren().add(new Label(answer));
        return view;
    }
}
