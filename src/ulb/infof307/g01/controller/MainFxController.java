package ulb.infof307.g01.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.infof307.g01.database.Database;
import ulb.infof307.g01.database.DatabaseScheme;
import ulb.infof307.g01.database.exceptions.DatabaseException;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.view.mainwindow.MainWindowViewController;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

/**
 * Main class of the application which initializes the main view using the main view handler and loads a menu view.
 */
public class MainFxController extends Application implements MainWindowViewController.NavigationListener,
                                                                DeckMenuController.ControllerListener,
                                                                PlayDeckController.ControllerListener {

    DeckMenuController deckMenuController;
    MainWindowViewController mainWindowViewController;
    PlayDeckController playDeckController;

    private Stage stage;


    /* ====================================================================== */
    /*                                  Main                                  */
    /* ====================================================================== */

    public static void main(String[] args) {
        launch();
    }


    /* ====================================================================== */
    /*                           Application Methods                          */
    /* ====================================================================== */

    @Override
    public void start(Stage stage) throws IOException {

        this.stage = stage;

        URL resource = MainWindowViewController
                            .class
                            .getResource("MainWindowView.fxml");

        FXMLLoader fxmlLoader = new FXMLLoader(resource);

        Parent root = fxmlLoader.load();

        stage.setScene(new Scene(root));
        stage.setResizable(false);

        try {
            initDatabase();

        } catch (SQLException e) {
            e.printStackTrace();

        } catch (DatabaseException e) {
            // display error message
        }

        mainWindowViewController = fxmlLoader.getController();
        mainWindowViewController.setListener(this);

        deckMenuController = new DeckMenuController(
                                        stage,
                                        this,
                                        mainWindowViewController);

        deckMenuController.show();
    }


    /* ====================================================================== */
    /*                       Database Access Methods                          */
    /* ====================================================================== */

    private void initDatabase() throws SQLException, DatabaseException {
        File dbfile = new File("demo.db");
        Database db = Database.singleton();
        db.open(dbfile);
        db.initTables(DatabaseScheme.CLIENT);
    }


    /* ====================================================================== */
    /*                     Controller Listener Methods                        */
    /* ====================================================================== */

    @Override
    public void editDeckClicked(Deck deck) {
        EditDeckController editDeckController
                = new EditDeckController(stage, deck, mainWindowViewController);

        try {
            editDeckController.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void playDeckClicked(Deck deck) {
        playDeckController = new PlayDeckController(
                                        stage,
                                        deck,
                                        mainWindowViewController,
                         this);

        playDeckController.show();
    }


    /* ====================================================================== */
    /*                   Navigation Listener Methods                          */
    /* ====================================================================== */

    @Override
    public void goBackClicked() {
        try {
            deckMenuController.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Display error
        }
    }

    @Override
    public void goToHomeClicked() {
        try {
            deckMenuController.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Display error
        }
    }

    @Override
    public void goToCurrentDeckClicked() {
        playDeckController.show();
    }

    @Override
    public void goToAboutClicked() {

    }

    @Override
    public void finishedPlayingDeck() {
        try {
            deckMenuController.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Display error
        }
    }
}
