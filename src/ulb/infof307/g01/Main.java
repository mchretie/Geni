package ulb.infof307.g01;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ulb.infof307.g01.views.CartView;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        StackPane root = new StackPane();
        root.getChildren().add(new CartView("Why is Alec so hot?", "He's a nazi.").getView());
        Scene scene = new Scene(root, 640, 480, Color.web("#635f63"));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
