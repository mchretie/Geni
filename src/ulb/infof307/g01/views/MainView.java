package ulb.infof307.g01.views;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class MainView implements View{

    Pane centerView;
    BorderPane view;

    public MainView() {
        view = new BorderPane();
    }

    public void setCenterView(View _centerView) {
        centerView = _centerView.getPane();
        this.view.setCenter(centerView);
    }

    public Pane getPane() {
        return view;
    }
}
