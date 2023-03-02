package ulb.infof307.g01.views;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class EditView implements View{
    public Pane getPane() {
        return new VBox(new Label("This is the edit view"));
    }
}
