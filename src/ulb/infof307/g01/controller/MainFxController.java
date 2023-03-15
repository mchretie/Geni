package ulb.infof307.g01.controller;

import javafx.application.Application;
import javafx.stage.Stage;
import ulb.infof307.g01.database.Database;
import ulb.infof307.g01.database.DatabaseScheme;
import ulb.infof307.g01.model.Deck;

import java.io.File;
import java.io.IOException;

/**
 * Main class of the application which initializes the main view using the main view handler and loads a menu view.
 */
public class MainFxController extends Application implements DeckMenuController.ControllerListener, EditDeckController.ControllerListener {

    private Stage stage;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        initDatabase();

        this.stage = stage;

        DeckMenuController deckMenuController = new DeckMenuController(stage, this);
        deckMenuController.show();
    }

    private void initDatabase() {
        try {
            File dbfile = new File("demo.db");
            Database db = Database.singleton();
            db.open(dbfile);
            db.initTables(DatabaseScheme.CLIENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void editDeckClicked(Deck deck) {
        EditDeckController editDeckController = new EditDeckController(stage, deck, this);

        try {
            editDeckController.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void playDeckClicked(Deck deck) {
        PlayDeckController playDeckController = new PlayDeckController();
    }
}
