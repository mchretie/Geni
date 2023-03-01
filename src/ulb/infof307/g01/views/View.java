package ulb.infof307.g01.views;

import javafx.scene.layout.Pane;

/**
 * A view is an object that can return a Pane which can then easily be shown in MainView.
 */
public interface View {
    /**
     * Returns the Pane of the view
     * @return the Pane of the view
     */
    public Pane getPane();
}
