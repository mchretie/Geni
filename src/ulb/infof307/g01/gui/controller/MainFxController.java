package ulb.infof307.g01.gui.controller;

import com.google.gson.JsonSyntaxException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.infof307.g01.gui.controller.errorhandler.ErrorHandler;
import ulb.infof307.g01.gui.controller.exceptions.EmptyDeckException;
import ulb.infof307.g01.gui.httpdao.dao.DeckDAO;
import ulb.infof307.g01.gui.httpdao.dao.UserDAO;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.model.DeckMetadata;
import ulb.infof307.g01.model.FlashCard;


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

    private ErrorHandler errorHandler;

    /* ====================================================================== */
    /*                                  Main                                  */
    /* ====================================================================== */

    public static void main(String[] args) {
        launch();
    }


    /* ====================================================================== */
    /*                             View stack methods                         */
    /* ====================================================================== */

    private void showPreviousView() {

        if (viewStack.size() == 1)
            return;

        try {
            viewStack.remove(viewStack.size() - 1);
            switch (viewStack.get(viewStack.size() - 1)) {
                case DECK_MENU -> deckMenuController.show();
                case PLAY_DECK -> playDeckController.show();
                case EDIT_DECK -> editDeckController.show();
                case HTML_EDITOR -> editCardController.show();
            }

        } catch (IOException | InterruptedException e) {
            errorHandler.restartApplicationError(e);
        }
    }


    /* ====================================================================== */
    /*                           Application Methods                          */
    /* ====================================================================== */

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {

        this.stage = stage;
        stage.setWidth(1000);
        stage.setHeight(800);
        stage.setMinHeight(500);
        stage.setMinWidth(600);

        stage.setTitle("Pokémon TCG Deck Builder");
        userDAO.register("guest", "guest");
        userDAO.login("guest", "guest");

        URL resource = MainWindowViewController
                .class
                .getResource("MainWindowView.fxml");

        FXMLLoader fxmlLoader = new FXMLLoader(resource);

        Parent root = fxmlLoader.load();
        stage.setScene(new Scene(root));

        mainWindowViewController = fxmlLoader.getController();
        mainWindowViewController.setListener(this);
        errorHandler = new ErrorHandler(mainWindowViewController);

        try {
            deckMenuController = new DeckMenuController(
                    stage,
                    errorHandler,
                    this,
                    mainWindowViewController,
                    deckDAO,
                    userDAO);

            viewStack.add(View.DECK_MENU);
            deckMenuController.show();

        } catch (IOException | InterruptedException e) {
            errorHandler.restartApplicationError(e);
        }

    }

    /* ====================================================================== */
    /*                     Controller Listener Methods                        */
    /* ====================================================================== */

    @Override
    public void editDeckClicked(DeckMetadata deckMetadata) {

        try {
            editDeckController
                    = new EditDeckController(stage,
                    deckDAO.getDeck(deckMetadata).orElse(null),
                    errorHandler,
                    mainWindowViewController,
                    this,
                    deckDAO);

            editDeckController.show();
            viewStack.add(View.EDIT_DECK);

        } catch (InterruptedException | IOException e) {
            errorHandler.severConnectionError();
        }
    }

    @Override
    public void playDeckClicked(DeckMetadata deckMetadata) {
        try {
            playDeckController = new PlayDeckController(
                    stage,
                    deckDAO.getDeck(deckMetadata).orElse(null),
                    mainWindowViewController,
                    this);

            playDeckController.show();
            viewStack.add(View.PLAY_DECK);

        } catch (EmptyDeckException e) {
            errorHandler.emptyPacketError();

        } catch (InterruptedException | IOException e) {
            errorHandler.severConnectionError();
        }
    }

    @Override
    public void editFrontOfCardClicked(Deck deck, Card selectedCard) {
        editCardController
                = new EditCardController(stage,
                deck,
                selectedCard,
                true,
                deckDAO,
                errorHandler,
                mainWindowViewController,
                this);

        viewStack.add(View.HTML_EDITOR);
        editCardController.show();
    }

    @Override
    public void editBackOfCardClicked(Deck deck, FlashCard selectedCard) {
        editCardController
                = new EditCardController(stage,
                                            deck,
                                            selectedCard,
                                       false,
                                            deckDAO,
                                            errorHandler,
                                            mainWindowViewController,
                             this);

        viewStack.add(View.HTML_EDITOR);
        editCardController.show();
    }

    @Override
    public void savedChanges() {
        showPreviousView();
    }


    /* ====================================================================== */
    /*                   Navigation Listener Methods                          */
    /* ====================================================================== */

    @Override
    public void goBackClicked() {
        showPreviousView();
    }

    @Override
    public void goToHomeClicked() {
        try {
            deckMenuController.show();

        } catch (IOException | InterruptedException e) {
            errorHandler.restartApplicationError(e);
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
            viewStack.remove(viewStack.size() - 1);
            deckMenuController.show();

        } catch (IOException | InterruptedException e) {
            errorHandler.restartApplicationError(e);
        }
    }
}
