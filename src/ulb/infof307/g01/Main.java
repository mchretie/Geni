package ulb.infof307.g01;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ulb.infof307.g01.views.MainView;
import ulb.infof307.g01.views.MainViewHandler;
import ulb.infof307.g01.views.MenuView;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        StackPane root = new StackPane();
        MainViewHandler mainViewHandler = MainViewHandler.getInstance();
        MainView mainView = mainViewHandler.getMainView();
        mainView.setCenterView(new MenuView());
        root.getChildren().add(mainView.getPane());
        Scene scene = new Scene(root, 640, 480, Color.web("#635f63"));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
