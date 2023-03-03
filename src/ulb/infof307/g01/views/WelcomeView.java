package ulb.infof307.g01.views;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class WelcomeView implements View{
    public Pane getPane() {
        return new VBox(new Label("Welcome to this amazing software"));
    }
}