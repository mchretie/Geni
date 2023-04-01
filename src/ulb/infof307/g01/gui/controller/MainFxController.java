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
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class of the application which initializes the main view using the main view handler and loads a menu view.
 */
public class MainFxController extends Application implements
        MainWindowViewController.NavigationListener,
        DeckMenuController.ControllerListener,
        PlayDeckController.ControllerListener,
        EditDeckController.ControllerListener,
        EditCardController.ControllerListener {

    /* ====================================================================== */
    /*                          Attribute: Controllers                        */
    /* ====================================================================== */

    private DeckMenuController deckMenuController;
    private EditDeckController editDeckController;
    private PlayDeckController playDeckController;
    private EditCardController editCardController;


    private MainWindowViewController mainWindowViewController;

    /* ====================================================================== */
    /*                              DAO Attributes                            */
    /* ====================================================================== */

    private final UserDAO userDAO = new UserDAO();
    private final DeckDAO deckDAO = new DeckDAO();


    /* ====================================================================== */
    /*                            View Stack Attributes                       */
    /* ====================================================================== */

    private enum View {
        DECK_MENU,
        PLAY_DECK,
        EDIT_DECK,
        HTML_EDITOR
    }

    List<View> viewStack = new ArrayList<>();


    /* ====================================================================== */
    /*                             Stage Attributes                           */
    /* ====================================================================== */

    Stage stage;

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
        stage.setResizable(false);

        mainWindowViewController = fxmlLoader.getController();
        mainWindowViewController.setListener(this);

        try {
            deckMenuController = new DeckMenuController(
                    stage,
                    this,
                    mainWindowViewController,
                    deckDAO,
                    userDAO);

            viewStack.add(View.DECK_MENU);
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
     * the application
     */
    private void communicateError(Exception e, String messageToUser) {
        mainWindowViewController.alertError(e.toString(), messageToUser);
    }

    /**
     * For exceptions that indicate that the app cannot continue to
     * function properly
     */
    private void restartApplicationError(Exception e) {
        communicateError(e, "Veuillez redémarrer l'application.");
        Platform.exit();
    }

    /**
     * For when windows other than the main window fail to launch
     */
    private void returnToMenuError(Exception e) {
        communicateError(e, "Vous reviendrez au menu principal.");
    }

    /**
     * For when changes to components (Decks, cards, etc.) fail to be saved in
     * the db
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

            viewStack.add(View.EDIT_DECK);
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

            viewStack.add(View.PLAY_DECK);
            playDeckController.show();
        } catch (EmptyDeckException e) {
            String title = "Paquet vide.";
            String description = "Le paquet que vous aviez ouvert est vide.";
            mainWindowViewController.alertInformation(title, description);
        }
    }

    @Override
    public void editCardClicked(Deck deck, Card card) {
        editCardController = new EditCardController(mainWindowViewController, this, stage, deck, card, deckDAO);
        editCardController.show();
        viewStack.add(View.HTML_EDITOR);
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
        // If there is no view to go back to, do nothing (shouldn't happen)
        if (viewStack.size() == 1)
            return;

        System.out.println("Going back to " + viewStack.get(viewStack.size() - 2));

        try {
            viewStack.remove(viewStack.size() - 1);
            switch (viewStack.get(0)) {
                case DECK_MENU -> deckMenuController.show();
                case PLAY_DECK -> playDeckController.show();
                case EDIT_DECK, HTML_EDITOR -> editDeckController.show();
            }
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
