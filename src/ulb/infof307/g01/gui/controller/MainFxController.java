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

import java.util.prefs.Preferences;


/**
 * Main class of the application which initializes the main view using the main
 * view handler and loads a menu view.
 */

public class MainFxController
        extends Application implements MainWindowViewController.NavigationListener,
        DeckMenuController.ControllerListener,
        PlayDeckController.ControllerListener,
        EditDeckController.ControllerListener,
        EditCardController.ControllerListener,
        LoginController.ControllerListener,
        ProfileController.ControllerListener {

    /* ====================================================================== */
    /*                          Attribute: Controllers                        */
    /* ====================================================================== */

    private DeckMenuController deckMenuController;
    private EditDeckController editDeckController;
    private PlayDeckController playDeckController;
    private EditCardController editCardController;
    private LoginController loginController;
    private ProfileController profileController;

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
        HTML_EDITOR,
        LOGIN,
        PROFILE
    }

    List<View> viewStack = new ArrayList<>();

    /* ====================================================================== */
    /*                             Stage Attributes                           */
    /* ====================================================================== */

    Stage stage;

    /* ====================================================================== */
    /*                             Stage Attributes                           */
    /* ====================================================================== */

    private final Preferences prefs = Preferences.userNodeForPackage(UserDAO.class);
    private String username;
    private String password;

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

        URL resource =
                MainWindowViewController.class.getResource("MainWindowView.fxml");

        FXMLLoader fxmlLoader = new FXMLLoader(resource);

        Parent root = fxmlLoader.load();

        stage.setScene(new Scene(root));
        stage.setResizable(false);

        mainWindowViewController = fxmlLoader.getController();
        mainWindowViewController.setListener(this);

        loginController =
                new LoginController(stage, mainWindowViewController, this);
        profileController =
                new ProfileController(stage, mainWindowViewController, this);

        // Todo : handle failed auto login. example:
        // This next line is IMPORTANT to reset the registery when deleting demo.db
        // removeCredentials();

        if (userCredentialsExist()) {
            loginWithCredentials();
            profileController.setUserNameInProfile(username);
            profileController.setLoggedIn(true);
        }
        try {
            deckMenuController = new DeckMenuController(
                    stage, this, mainWindowViewController, deckDAO, userDAO);
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

    @Override
    public void playDeckClicked(Deck deck) {
        try {
            playDeckController =
                    new PlayDeckController(stage, deck, mainWindowViewController, this);

            viewStack.add(View.PLAY_DECK);
            playDeckController.show();
        } catch (EmptyDeckException e) {
            String title = "Paquet vide.";
            String description = "Le paquet que vous aviez ouvert est vide.";
            mainWindowViewController.alertInformation(title, description);
        }
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
            editDeckController = new EditDeckController(
                    stage, deck, mainWindowViewController, this, deckDAO);
            viewStack.add(View.EDIT_DECK);
            editDeckController.show();
        } catch (IOException e) {
            returnToMenuError(e);
        }
    }

    @Override
    public void editCardClicked(Deck deck, Card card) {
        editCardController = new EditCardController(stage, deck, card, deckDAO,
                mainWindowViewController, this);
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

        try {
            viewStack.remove(viewStack.size() - 1);
            switch (viewStack.get(viewStack.size() - 1)) {
                case DECK_MENU -> deckMenuController.show();
                case PLAY_DECK -> playDeckController.show();
                case EDIT_DECK -> editDeckController.show();
                case HTML_EDITOR -> editCardController.show();
                case LOGIN -> loginController.show();
                case PROFILE -> profileController.show();
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

    @Override
    public void handleProfileClicked() {
        try {
            if (profileController.isLoggedIn()) {
                viewStack.add(View.PROFILE);
                profileController.show();
            } else {
                viewStack.add(View.LOGIN);
                loginController.show();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void handleLogout() {
        try {
            removeCredentials();
            userDAO.removeToken();
            profileController.setLoggedIn(false);
            viewStack.remove(View.PROFILE);
            loginController.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleLogin(String username, String password) {
        profileController.setLoggedIn(true);
        viewStack.remove(View.LOGIN);
        saveCredentials(username, password);
        profileController.setUserNameInProfile(username);
        try {
            profileController.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Checks if the user has already logged in and if so saves the credentials
    public boolean userCredentialsExist() {
        this.username = this.prefs.get("localUsername", null);
        this.password = this.prefs.get("localPassword", null);
        return username != null && password != null;
    }

    public void loginWithCredentials() {
        try {
            userDAO.login(this.username, this.password);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeCredentials() {
        this.prefs.remove("localUsername");
        this.prefs.remove("localPassword");
    }

    public void saveCredentials(String username, String password) {
        this.prefs.put("localUsername", username);
        this.prefs.put("localPassword", password);
    }

    // Caution : Guest == NOT logged in
    @Override
    public boolean isGuestSession() {
        return !profileController.isLoggedIn();
    }


}
