package ulb.infof307.g01.views;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class GameView implements View{

    public Pane getPane() {
        VBox view = new VBox(new Label("GameView"));
        return view;
    }
}
