package ulb.infof307.g01.views;

import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ulb.infof307.g01.components.CardComponent;

public class GameView implements View{

    public Pane getPane() {
        VBox view = new VBox(new CardComponent("Question", "Answer").getPane());
        view.setAlignment(Pos.CENTER);
        return view;
    }
}
