package ulb.infof307.g01.gui.controller;

import com.google.gson.JsonSyntaxException;
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
            restartApplicationError(e);
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
        stage.setMinHeight(500);
        stage.setMinWidth(600);

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
     * Used to communicate errors that raised exceptions and require the user
     *  to restart the application.
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
     * For when changes to components (Decks, cards, etc.) fail to be saved in
     * the db
     */
    private void databaseModificationError(Exception e) {
        String message = "Vos modifications n’ont pas été enregistrées, "
                + "veuillez réessayer. Si le problème persiste, "
                + "redémarrez l’application";

        communicateError(e, message);
    }

    private void failedDeckExportError(Exception e) {
        String message = "L'exportation de votre deck a échoué "
                + "veuillez réessayer. Si le problème persiste, "
                + "redémarrez l’application";

        communicateError(e, message);
    }

    private void failedDeckImportError(JsonSyntaxException e) {
        String message = "L'importation du deck a échoué, " +
                "veuillez vérifier que le fichier est bien un fichier " +
                ".json.";

        communicateError(e, message);
    }

    private void failedFetchError(Exception e) {
        String message = "Le téléchargement depuis le serveur a échoué";

        communicateError(e, message);
    }

    /**
     * Used to communicate user errors that do not require the application to
     *  be restarted.
     *
     * @param title Title of the error
     * @param messageToUser Message to display to the user
     */
    private void communicateError(String title, String messageToUser) {
        mainWindowViewController.alertError(title, messageToUser);
    }

    private void invalidDeckNameError(char c) {
        String title = "Nom de paquet invalide.";
        String description = "Le nom de paquet que vous avez entré est invalide. "
                + "Veuillez entrer un nom de paquet qui ne contient pas le "
                + "caractère " + c + ".";

        communicateError(title, description);
    }

    private void emptyPacketError() {
        String title = "Paquet vide.";
        String description = "Le paquet que vous avez ouvert est vide.";
        communicateError(title, description);
    }

    private void severConnectionError() {
        String title = "Erreur avec le serveur";
        String description = "Le paquet n’a pu être téléchargé.";
        communicateError(title, description);
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
                    mainWindowViewController,
                    this,
                    deckDAO);

            editDeckController.show();
            viewStack.add(View.EDIT_DECK);

        } catch (InterruptedException | IOException e) {
            severConnectionError();
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
            emptyPacketError();

        } catch (InterruptedException | IOException e) {
            severConnectionError();
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
                                            mainWindowViewController,
                             this);

        viewStack.add(View.HTML_EDITOR);
        editCardController.show();
    }

    @Override
    public void fxmlLoadingError(IOException e) {
        restartApplicationError(e);
    }

    @Override
    public void savingError(Exception e) {
        databaseModificationError(e);
    }

    @Override
    public void failedExport(IOException e) {
        failedDeckExportError(e);
    }

    @Override
    public void failedFetch(InterruptedException e) {
        failedFetchError(e);
    }

    @Override
    public void failedImport(JsonSyntaxException e) {
        failedDeckImportError(e);
    }

    @Override
    public void invalidDeckName(String name, char c) {
        invalidDeckNameError(c);
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
            viewStack.remove(viewStack.size() - 1);
            deckMenuController.show();

        } catch (IOException | InterruptedException e) {
            restartApplicationError(e);
        }
    }
}
