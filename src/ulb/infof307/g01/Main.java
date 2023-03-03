package ulb.infof307.g01;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ulb.infof307.g01.views.MainView;
import ulb.infof307.g01.views.MainViewHandler;
import ulb.infof307.g01.views.MenuView;

/**
 * Main class of the application which initializes the main view using the main view handler and loads a menu view.
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) {
        MainViewHandler mainViewHandler = MainViewHandler.getInstance();

        StackPane root = new StackPane(mainViewHandler.getMainView().getPane());
        Scene scene = new Scene(root, 900, 600); //635f63
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
