package ulb.infof307.g01.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.infof307.g01.database.Database;
import ulb.infof307.g01.database.DatabaseScheme;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.view.MainViewController;

import java.io.File;
import java.io.IOException;

/**
 * Main class of the application which initializes the main view using the main view handler and loads a menu view.
 */
public class MainFxController extends Application implements DeckMenuController.ControllerListener {

    private DeckMenuController deckMenuController;
    private PlayDeckController playDeckController;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        initDatabase();

        deckMenuController = new DeckMenuController(stage, this);
        deckMenuController.show();
    }

    private void initDatabase() {
        File dbfile = new File("demo.db");
        Database db = Database.singleton();
        try {
            db.open(dbfile);
            db.initTables(DatabaseScheme.CLIENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void editDeckClicked(Deck deck) {

    }

    @Override
    public void playDeckClicked(Deck deck) {
        playDeckController = new PlayDeckController();

    }
}
