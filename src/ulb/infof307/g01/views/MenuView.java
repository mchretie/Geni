package ulb.infof307.g01.views;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * The view of the menu
 */
public class MenuView implements View{
    MainViewHandler mainViewHandler = MainViewHandler.getInstance();

    public Pane getPane() {
        VBox view = new VBox();
        Button testButton = new Button("Change view");
        testButton.setOnAction(e -> {
            mainViewHandler.setCenterView(new GameView());
            System.out.println("Changed view");
        });
        view.getChildren().add(testButton);
        return view;
    }
}
