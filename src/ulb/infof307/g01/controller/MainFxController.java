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

        mainWindowViewController = fxmlLoader.getController();
        mainWindowViewController.setListener(this);

        try {
            initDatabase();
            deckMenuController = new DeckMenuController(
                    stage,
                    this,
                    mainWindowViewController);

            deckMenuController.show();

        } catch (Exception e) {
            communicateError(e, "Please restart the application.");
        }
    }


    /* ====================================================================== */
    /*                      Error messages and handling                       */
    /* ====================================================================== */

    /**
     * Used to communicate errors that require the user to restart
     *  the application
     *
     * @param e Exception raised
     */
    private void communicateError(Exception e, String messageToUser) {
        mainWindowViewController.alertError(e.toString(), messageToUser);
    }

    private void restartApplicationError(Exception e) {
        communicateError(e, "Please restart the application.");
    }

    private void returnToMenuError(Exception e) {
        communicateError(e, "You will be returned to the main menu.");
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

        try {
            EditDeckController editDeckController
                    = new EditDeckController(stage, deck, mainWindowViewController);

            editDeckController.show();

        } catch (IOException e) {
            returnToMenuError(e);
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
            restartApplicationError(e);
        }
    }

    @Override
    public void goToHomeClicked() {
        try {
            deckMenuController.show();

        } catch (IOException e) {
            restartApplicationError(e);
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
            restartApplicationError(e);
        }
    }
}
