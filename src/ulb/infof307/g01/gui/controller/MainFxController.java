package ulb.infof307.g01.gui.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.infof307.g01.gui.controller.errorhandler.ErrorHandler;
import ulb.infof307.g01.gui.controller.exceptions.EmptyDeckException;
import ulb.infof307.g01.gui.http.ServerCommunicator;
import ulb.infof307.g01.gui.http.exceptions.AuthenticationFailedException;
import ulb.infof307.g01.gui.http.exceptions.ServerCommunicationFailedException;
import ulb.infof307.g01.gui.util.ImageLoader;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.model.card.Card;
import ulb.infof307.g01.model.card.FlashCard;
import ulb.infof307.g01.model.deck.Deck;
import ulb.infof307.g01.model.deck.DeckMetadata;
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
        ProfileController.ControllerListener,
        DeckPreviewController.ControllerListener {

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
    private DeckPreviewController deckPreviewController;
    private MarketplaceController marketplaceController;

    private MainWindowViewController mainWindowViewController;

    private final ImageLoader imageLoader = new ImageLoader();

    /* ====================================================================== */
    /*                              DAO Attributes                            */
    /* ====================================================================== */

    private final ServerCommunicator serverCommunicator = new ServerCommunicator();


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
        STATISTICS,
        PREVIEW_DECK,
        MARKETPLACE
    }

    final List<View> viewStack = new ArrayList<>();


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

        mainWindowViewController.setAllInvisible();
        stage.show();

        errorHandler = new ErrorHandler(mainWindowViewController);

        try {
            initControllers(stage);
            serverCommunicator.attemptAutoLogin();

            viewStack.add(View.DECK_MENU);
            deckMenuController.show();

        } catch (AuthenticationFailedException e) {
            userAuthController.show();

        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);

        } catch (IOException | InterruptedException e) {
            errorHandler.restartApplicationError(e);
        }
    }


    /* ====================================================================== */
    /*                         Config / init Methods                          */
    /* ====================================================================== */

    private void configureStage(Stage stage) {
        stage.setWidth(1000);
        stage.setHeight(800);
        stage.setMinHeight(500);
        stage.setMinWidth(600);
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

    private void initControllers(Stage stage) {

        this.userAuthController
                = new UserAuthController(stage,
                                         errorHandler,
                                         mainWindowViewController,
                                         this,
                                         serverCommunicator);

        this.profileController
                = new ProfileController(stage,
                                        mainWindowViewController,
                                        this,
                                        serverCommunicator);

        this.deckMenuController
                = new DeckMenuController(
                stage,
                errorHandler,
                this,
                mainWindowViewController,
                serverCommunicator);

        this.deckMenuController.setImageLoader(imageLoader);

        this.playDeckController
                = new PlayDeckController(
                stage,
                mainWindowViewController,
                this,
                errorHandler,
                serverCommunicator);

        this.deckPreviewController
                = new DeckPreviewController(
                stage,
                mainWindowViewController,
                this,
                errorHandler,
                serverCommunicator);
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
                case DECK_MENU, PREVIEW_DECK -> deckMenuController.show();
                case PLAY_DECK -> playDeckController.show();
                case EDIT_DECK -> editDeckController.show();
                case HTML_EDITOR -> editCardController.show();
                case LOGIN_PROFILE -> {
                    if (serverCommunicator.isUserLoggedIn())
                        profileController.show();
                    else
                        userAuthController.show();
                }
                case RESULT -> resultController.show();
                case LEADERBOARD -> leaderboardController.show();
                case STATISTICS -> statisticsController.show();
                case MARKETPLACE -> marketplaceController.show();
            }

        } catch (IOException | InterruptedException e) {
            errorHandler.restartApplicationError(e);

        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
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
                                             serverCommunicator.getDeck(deckMetadata).orElse(null),
                                             errorHandler,
                                             mainWindowViewController,
                                             this,
                                             serverCommunicator);

            editDeckController.show();
            viewStack.add(View.EDIT_DECK);

        } catch (IOException e) {
            errorHandler.failedLoading(e);

        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
        }
    }

    @Override
    public void deckClicked(DeckMetadata deckMetadata) {
        try {
            deckPreviewController
                    .setDeck(serverCommunicator.getDeck(deckMetadata).orElse(null));

            deckPreviewController.show();
            viewStack.add(View.PREVIEW_DECK);

        } catch (EmptyDeckException e) {
            errorHandler.emptyDeckError();

        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
        }
    }

    @Override
    public void editFrontOfCardClicked(Deck deck, Card selectedCard) {
        editCardController
                = new EditCardController(stage,
                                         deck,
                                         selectedCard,
                                         true,
                                         serverCommunicator,
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
                                         serverCommunicator,
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
            mainWindowViewController.makebottomNavigationBarVisible();
            mainWindowViewController.makeTopNavigationBarVisible();
            deckMenuController.show();
            resetViewStack(View.DECK_MENU);

        } catch (IOException | InterruptedException e) {
            errorHandler.failedLoading(e);
        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
        }
    }

    @Override
    public void userLoggedOut() {
        userAuthController.show();
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

        playDeckController.removeDeck();
        resultController.show();
    }

    @Override
    public void goBackToMenu() {
        try {
            viewStack.remove(viewStack.size() - 1);
            deckMenuController.show();

        } catch (IOException | InterruptedException e) {
            errorHandler.restartApplicationError(e);
        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
        }
    }

    @Override
    public void statisticsClicked() {
        statisticsController = new StatisticsController(
                stage,
                errorHandler,
                mainWindowViewController,
                serverCommunicator
        );

        viewStack.add(View.STATISTICS);
        statisticsController.show();
    }

    @Override
    public void onPlayDeckClicked(Deck deck) {
        try {
            playDeckController.setDeck(deck);
            playDeckController.show();

        } catch (EmptyDeckException | IllegalStateException e) {
            errorHandler.emptyDeckError();
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
        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
        }
    }

    @Override
    public void goToMarketplaceClicked() {
        try {
            marketplaceController = new MarketplaceController(
                    stage,
                    errorHandler,
                    mainWindowViewController,
                    serverCommunicator);

            marketplaceController.setImageLoader(imageLoader);

            resetViewStack(View.MARKETPLACE);
            marketplaceController.show();

        } catch (IOException e) {
            errorHandler.failedLoading(e);
        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
        }
    }

    @Override
    public void goToLeaderboardClicked() {
        if (leaderboardController == null) {
            leaderboardController = new GlobalLeaderboardController(
                    stage,
                    mainWindowViewController,
                    errorHandler,
                    serverCommunicator);
        }

        resetViewStack(View.LEADERBOARD);
        leaderboardController.show();
    }

    private void resetViewStack(View prevView) {
        viewStack.clear();
        viewStack.add(prevView);
    }

    @Override
    public void goToProfileClicked() {
        try {
            profileController.show();
            viewStack.add(View.LOGIN_PROFILE);

        } catch (IOException e) {
            errorHandler.failedLoading(e);
        }
    }

    @Override
    public void deckPreviewClosed() {
        try {
            deckMenuController.show();

        } catch (IOException | InterruptedException e) {
            errorHandler.failedLoading(e);
        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
        }
    }
}
