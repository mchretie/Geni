package ulb.infof307.g01.gui.controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.infof307.g01.gui.controller.exceptions.EmptyDeckException;
import ulb.infof307.g01.gui.httpdao.dao.DeckDAO;
import ulb.infof307.g01.gui.httpdao.dao.UserDAO;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.model.User;

import java.io.IOException;
import java.net.URL;

/**
 * Main class of the application which initializes the main view using the main view handler and loads a menu view.
 */
public class MainFxController extends Application implements MainWindowViewController.NavigationListener,
                                                                DeckMenuController.ControllerListener,
                                                                PlayDeckController.ControllerListener,
                                                                EditDeckController.ControllerListener {

    private DeckMenuController deckMenuController;
    private MainWindowViewController mainWindowViewController;
    private PlayDeckController playDeckController;

    private final UserDAO userDAO = new UserDAO();
    private final DeckDAO deckDAO = new DeckDAO();

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
    public void start(Stage stage) throws IOException, InterruptedException {

        this.stage = stage;

        // TODO: Title and login.
        stage.setTitle("Pokémon TCG Deck Builder");
        userDAO.register("guest", "guest");
        userDAO.login("guest", "guest");

        URL resource = MainWindowViewController
                            .class
                            .getResource("MainWindowView.fxml");

        FXMLLoader fxmlLoader = new FXMLLoader(resource);

        Parent root = fxmlLoader.load();

        stage.setScene(new Scene(root));
        stage.setResizable(true);
        stage.setMinHeight(400);
        stage.setMinWidth(550);


        mainWindowViewController = fxmlLoader.getController();
        mainWindowViewController.setListener(this);

        try {
            deckMenuController = new DeckMenuController(
                    stage,
                    this,
                    mainWindowViewController,
                    deckDAO,
                    userDAO);

            deckMenuController.show();

        } catch (IOException | InterruptedException e) {
            restartApplicationError(e);
        }
    }


    /* ====================================================================== */
    /*                      Error messages and handling                       */
    /* ====================================================================== */

    /**
     * Used to communicate errors that require the user to restart
     *  the application
     *
     */
    private void communicateError(Exception e, String messageToUser) {
        mainWindowViewController.alertError(e.toString(), messageToUser);
    }

    /**
     * For exceptions that indicate that the app cannot continue to
     *  function properly
     *
     */
    private void restartApplicationError(Exception e) {
        communicateError(e, "Veuillez redémarrer l'application.");
        Platform.exit();
    }

    /**
     * For when windows other than the main window fail to launch
     *
     */
    private void returnToMenuError(Exception e) {
        communicateError(e, "Vous reviendrez au menu principal.");
    }

    /**
     * For when changes to components (Decks, cards, etc.) fail to be saved in
     *  the db
     *
     */
    private void databaseModificationError(Exception e) {
        String message = "Vos modifications n’ont pas été enregistrées, "
                            + "veuillez réessayer. Si le problème persiste, "
                            + "redémarrez l’application";

        communicateError(e, message);
    }


    /* ====================================================================== */
    /*                     Controller Listener Methods                        */
    /* ====================================================================== */

    @Override
    public void editDeckClicked(Deck deck) {

        try {
            EditDeckController editDeckController
                    = new EditDeckController(stage,
                                                deck,
                                                mainWindowViewController,
                                                this,
                                                deckDAO);

            editDeckController.show();

        } catch (IOException e) {
            returnToMenuError(e);
        }
    }

    @Override
    public void playDeckClicked(Deck deck) {
        try {
            playDeckController = new PlayDeckController(
                    stage,
                    deck,
                    mainWindowViewController,
                    this);

            playDeckController.show();
        }

        catch (EmptyDeckException e) {
            String title = "Paquet vide.";
            String description = "Le paquet que vous aviez ouvert est vide.";
            mainWindowViewController.alertInformation(title, description);
        }
    }

    @Override
    public void fxmlLoadingError(IOException e) {
        restartApplicationError(e);
    }

    @Override
    public void savingError(Exception e) {
        databaseModificationError(e);
    }


    /* ====================================================================== */
    /*                   Navigation Listener Methods                          */
    /* ====================================================================== */

    @Override
    public void goBackClicked() {
        try {
            deckMenuController.show();

        } catch (IOException | InterruptedException e) {
            restartApplicationError(e);
        }
    }

    @Override
    public void goToHomeClicked() {
        try {
            deckMenuController.show();

        } catch (IOException | InterruptedException e) {
            restartApplicationError(e);
        }
    }

    @Override
    public void goToCurrentDeckClicked() {
        if (playDeckController == null)
            return;

        playDeckController.show();
    }

    @Override
    public void goToAboutClicked() {

    }

    @Override
    public void finishedPlayingDeck() {
        try {
            deckMenuController.show();

        } catch (IOException | InterruptedException e) {
            restartApplicationError(e);
        }
    }
}
