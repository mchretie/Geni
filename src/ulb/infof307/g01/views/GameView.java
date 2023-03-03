package ulb.infof307.g01.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import ulb.infof307.g01.components.CardComponent;
import ulb.infof307.g01.components.Component;

import java.awt.event.MouseEvent;
import java.beans.EventHandler;

public class GameView implements View {

    public GameView() {
    }

    public Pane getPane() {
        VBox view = new VBox(new Label("GameView"));
        Component card = new CardComponent("how long is my dick?", "43 cm ;)");
        return card.getPane();
    }
}
