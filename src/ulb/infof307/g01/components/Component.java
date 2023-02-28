package ulb.infof307.g01.components;

import javafx.scene.layout.Pane;

/**
 * A component is an object that can return a view which can then easily be shown in a Pane.
 */
public interface Component {
    Pane getPane();
}
