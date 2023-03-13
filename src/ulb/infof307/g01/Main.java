package ulb.infof307.g01;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.infof307.g01.database.Database;
import ulb.infof307.g01.database.DatabaseScheme;
import ulb.infof307.g01.view.MainViewController;

import java.io.File;
import java.io.IOException;

/**
 * Main class of the application which initializes the main view using the main view handler and loads a menu view.
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        initDatabase();
        FXMLLoader fxmlLoader = new FXMLLoader(MainViewController.class.getResource("MainView.fxml"));
        Parent root = fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void initDatabase() {
        File dbfile = new File("demo.db");
        Database db = Database.singleton();
        try {
            db.open(dbfile);
            db.initTables(DatabaseScheme.CLIENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
