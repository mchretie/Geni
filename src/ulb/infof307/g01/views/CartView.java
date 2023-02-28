package ulb.infof307.g01.views;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class CartView {
    final String question;
    final String answer;

    public CartView(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public VBox getView() {
        VBox view = new VBox();
        view.getChildren().add(new Label(question));
        view.getChildren().add(new Label(answer));
        return view;
    }
}
