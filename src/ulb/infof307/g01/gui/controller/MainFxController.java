package ulb.infof307.g01.gui.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.infof307.g01.gui.controller.errorhandler.ErrorHandler;
import ulb.infof307.g01.gui.controller.exceptions.EmptyDeckException;
import ulb.infof307.g01.gui.httpdao.dao.DeckDAO;
import ulb.infof307.g01.gui.httpdao.dao.LeaderboardDAO;
import ulb.infof307.g01.gui.httpdao.dao.UserSessionDAO;
import ulb.infof307.g01.gui.httpdao.dao.GameHistoryDAO;
import ulb.infof307.g01.gui.httpdao.exceptions.AuthenticationFailedException;
import ulb.infof307.g01.model.card.Card;
import ulb.infof307.g01.model.deck.Deck;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.model.deck.DeckMetadata;
import ulb.infof307.g01.model.card.FlashCard;
import ulb.infof307.g01.model.deck.Score;


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
        EditCardController.ControllerListener,
        ResultController.ControllerListener,
        UserAuthController.ControllerListener,
        ProfileController.ControllerListener {

    /* ====================================================================== */
    /*                          Attribute: Controllers                        */
    /* ====================================================================== */

    private DeckMenuController deckMenuController;
    private EditDeckController editDeckController;
    private PlayDeckController playDeckController;
    private EditCardController editCardController;
    private ResultController resultController;
    private UserAuthController userAuthController;
    private ProfileController profileController;
    private GlobalLeaderboardController leaderboardController;
    private StatisticsController statisticsController;

    private MainWindowViewController mainWindowViewController;

    /* ====================================================================== */
    /*                              DAO Attributes                            */
    /* ====================================================================== */

    private final UserSessionDAO userSessionDAO = new UserSessionDAO();
    private final DeckDAO deckDAO = new DeckDAO();
    private final LeaderboardDAO leaderboardDAO = new LeaderboardDAO();
    private final GameHistoryDAO gameHistoryDAO = new GameHistoryDAO();


    /* ====================================================================== */
    /*                            View Stack Attributes                       */
    /* ====================================================================== */

    private enum View {
        DECK_MENU,
        PLAY_DECK,
        EDIT_DECK,
        HTML_EDITOR,
        LOGIN_PROFILE,
        RESULT,
        LEADERBOARD,
        STATISTICS
    }

    List<View> viewStack = new ArrayList<>();


    /* ====================================================================== */
    /*                             Stage Attributes                           */
    /* ====================================================================== */

    Stage stage;


    /* ====================================================================== */
    /*                              Error Handler                             */
    /* ====================================================================== */

    private ErrorHandler errorHandler;


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

        configureStage(stage);
        initMainWindowView(stage);

        errorHandler = new ErrorHandler(mainWindowViewController);

        mainWindowViewController.setAllInvisible();
        stage.show();

        try {
            userSessionDAO.attemptAutologin();

        } catch (AuthenticationFailedException | InterruptedException e) {
            errorHandler.failedAutoLogin(e);
        }

        try {
            initControllers(stage);

            viewStack.add(View.DECK_MENU);
            (deckMenuController).show();

        } catch (IOException | InterruptedException e) {
            errorHandler.restartApplicationError(e);
        }
    }


    /* ====================================================================== */
    /*                         Config / init Methods                          */
    /* ====================================================================== */

    private void configureStage(Stage stage) {
        stage.setWidth(1300);
        stage.setHeight(800);
        stage.setMinHeight(500);
        stage.setMinWidth(800);
        stage.setTitle("Quinzelette");
    }

    private void initMainWindowView(Stage stage) throws IOException {
        URL resource = MainWindowViewController
                .class
                .getResource("MainWindowView.fxml");

        FXMLLoader fxmlLoader = new FXMLLoader(resource);

        Parent root = fxmlLoader.load();
        stage.setScene(new Scene(root));

        this.mainWindowViewController = fxmlLoader.getController();
        this.mainWindowViewController.setListener(this);
    }

    private void initControllers(Stage stage) throws IOException, InterruptedException {

        this.userAuthController
                = new UserAuthController(stage,
                errorHandler,
                mainWindowViewController,
                this,
                userSessionDAO);

        this.profileController
                = new ProfileController(stage,
                mainWindowViewController,
                this,
                userSessionDAO);

        this.deckMenuController
                = new DeckMenuController(
                stage,
                errorHandler,
                this,
                mainWindowViewController,
                deckDAO,
                userSessionDAO,
                leaderboardDAO);
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
                case LOGIN_PROFILE -> {
                    if (userSessionDAO.isLoggedIn())
                        profileController.show();
                    else
                        userAuthController.show();
                }
                case RESULT -> resultController.show();
                case LEADERBOARD -> leaderboardController.show();
                case STATISTICS -> statisticsController.show();
            }

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
                    this,
                    errorHandler,
                    leaderboardDAO,
                    userSessionDAO);

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

    @Override
    public void userLoggedIn() {
        try {
            deckMenuController.show();
            viewStack.add(View.DECK_MENU);

        } catch (IOException | InterruptedException e) {
            errorHandler.failedLoading(e);
        }
    }

    @Override
    public void userLoggedOut() {
        try {
            userSessionDAO.logout();
            resetViewStack(View.DECK_MENU);
            deckMenuController.show();

        } catch (IOException | InterruptedException e) {
            errorHandler.failedLoading(e);
        }
    }

    @Override
    public void finishedPlayingDeck(Score score) {
        viewStack.remove(viewStack.size() - 1);
        resultController = new ResultController(
                stage,
                mainWindowViewController,
                this,
                score
        );
        viewStack.add(View.RESULT);
        resultController.show();
    }

    @Override
    public void goBackToMenu() {
        try {
            viewStack.remove(viewStack.size() - 1);
            deckMenuController.show();
        } catch (IOException | InterruptedException e) {
            errorHandler.restartApplicationError(e);
        }
    }

    @Override
    public void statisticsClicked() {
        try {
            statisticsController = new StatisticsController(
                    stage,
                    errorHandler,
                    mainWindowViewController,
                    userSessionDAO,
                    deckDAO,
                    gameHistoryDAO
            );

            viewStack.add(View.STATISTICS);
            statisticsController.show();

        } catch (IOException e) {
            errorHandler.restartApplicationError(e);
        }
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
            resetViewStack(View.DECK_MENU);
            deckMenuController.show();

        } catch (IOException | InterruptedException e) {
            errorHandler.restartApplicationError(e);
        }
    }

    @Override
    public void goToCurrentPlayingDeck() {
        if (playDeckController == null || !userSessionDAO.isLoggedIn())
            return;

        playDeckController.show();
        viewStack.add(View.PLAY_DECK);
    }

    @Override
    public void goToLeaderboardClicked() {
        try {
            if (leaderboardController == null) {
                leaderboardController = new GlobalLeaderboardController(
                        stage,
                        mainWindowViewController,
                        errorHandler,
                        userSessionDAO,
                        deckDAO,
                        leaderboardDAO);
            }

            resetViewStack(View.LEADERBOARD);
            leaderboardController.show();


        } catch (InterruptedException | IOException e) {
            errorHandler.failedLoading(e);
        }
    }

    private void resetViewStack(View prevView) {
        viewStack.clear();
        viewStack.add(prevView);
    }

    @Override
    public void goToProfileClicked() {
        try {
            userSessionDAO.isLoggedIn();
            profileController.show();
            viewStack.add(View.LOGIN_PROFILE);

        } catch (IOException e) {
            errorHandler.failedLoading(e);
        }
    }
}
