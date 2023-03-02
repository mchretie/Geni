package ulb.infof307.g01.views;

import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * The main view of the application
 */
public class MainView implements View{

    Pane centerView;
    BorderPane view;

    public MainView() {
        view = new BorderPane();
        ToolBar toolbar = new ToolBar(new Label("Toolbar"));
        view.setTop(toolbar);
    }

    public void setCenterView(View _centerView) {
        centerView = _centerView.getPane();
        this.view.setCenter(centerView);
    }

    public Pane getPane() {
        return view;
    }
}
