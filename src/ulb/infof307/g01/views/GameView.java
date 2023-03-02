package ulb.infof307.g01.views;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ulb.infof307.g01.components.CardComponent;
import ulb.infof307.g01.components.Component;

public class GameView implements View{

    public Pane getPane() {
        //VBox view = new VBox(new Label("GameView"));
        Component card = new CardComponent("how long is my dick?", "43 cm ;)");
        return card.getPane();
    }
}
