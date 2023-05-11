package ulb.infof307.g01.gui.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;
import ulb.infof307.g01.gui.controller.errorhandler.ErrorHandler;
import ulb.infof307.g01.gui.http.ServerCommunicator;
import ulb.infof307.g01.gui.http.exceptions.ServerCommunicationFailedException;
import ulb.infof307.g01.gui.view.deckpreview.DeckPreviewViewController;
import ulb.infof307.g01.gui.view.deckpreview.GameHistoryItemViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.model.deck.Deck;
import ulb.infof307.g01.model.deck.Score;
import ulb.infof307.g01.model.gamehistory.Game;
import ulb.infof307.g01.model.gamehistory.GameHistory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DeckPreviewController implements DeckPreviewViewController.Listener {

    /* ====================================================================== */
    /*                             Controllers                                */
    /* ====================================================================== */

    private final DeckPreviewViewController deckPreviewViewController;
    private final ControllerListener controllerListener;
    private final MainWindowViewController mainWindowViewController;
    private final ErrorHandler errorHandler;

    /* ====================================================================== */
    /*                            Server Comm                                 */
    /* ====================================================================== */

    private final ServerCommunicator serverCommunicator;

    /* ====================================================================== */
    /*                             Stage                                       */
    /* ====================================================================== */

    private final Stage stage;

    /* ====================================================================== */
    /*                            Model Objects                               */
    /* ====================================================================== */

    private Deck deck;

    public DeckPreviewController(Stage stage,
                                 MainWindowViewController mainWindowViewController,
                                 ControllerListener controllerListener,
                                 ErrorHandler errorHandler,
                                 ServerCommunicator serverCommunicator) {

        this.stage = stage;
        this.mainWindowViewController = mainWindowViewController;
        this.controllerListener = controllerListener;
        this.errorHandler = errorHandler;

        this.serverCommunicator = serverCommunicator;

        this.deckPreviewViewController
                = mainWindowViewController.getDeckPreviewViewController();

        deckPreviewViewController.setListener(this);
    }

    public void setDeck(Deck deck) {
        try {
            this.deck = deck;
            deckPreviewViewController.setDeck(deck);

            Score score = serverCommunicator.getBestScoreForDeck(deck.getId());
            String scoreString = score == null ? "0" : String.valueOf(score.getScoreValue());
            deckPreviewViewController.setScore(scoreString);

            deckPreviewViewController.setPlayDeckButtonDisabled(deck.cardCount() == 0);
            deckPreviewViewController.setDeckVisibility(deck.isPublic());
            deckPreviewViewController.setGameHistory(loadGameHistory());

        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
        }
    }

    public void show() throws IllegalStateException {

        if (deck == null) {
            throw new IllegalStateException("Deck must be set before showing the deck preview");
        }

        mainWindowViewController.setDeckPreviewViewVisible();
        mainWindowViewController.makeGoBackIconVisible();

        stage.show();
    }

    private List<Node> loadGameHistory() {
        try {
            List<Node> playersScoreItem = new ArrayList<>();

            GameHistory gameHistory = serverCommunicator.getGameHistory(deck.getId());

            for (Game game : gameHistory) {
                Node node = loadGameHistoryItem(game);
                playersScoreItem.add(node);
            }

            return playersScoreItem;

        } catch (IOException e) {
            errorHandler.savingError(e);

        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
        }

        return new ArrayList<>();
    }

    private Node loadGameHistoryItem(Game game) throws IOException {
        URL url = GameHistoryItemViewController
                .class.getResource("GameHistoryItemView.fxml");

        FXMLLoader loader = new FXMLLoader(url);
        Node node = loader.load();

        GameHistoryItemViewController gameHistoryItemViewController
                = loader.getController();

        gameHistoryItemViewController.setGame(game);

        return node;
    }


    /* ====================================================================== */
    /*                          Listener methods                              */
    /* ====================================================================== */

    @Override
    public void playDeckClicked() {
        controllerListener.onPlayDeckClicked(deck);
    }

    @Override
    public void deckShared() {
        try {
            deck.switchOnlineVisibility();
            deckPreviewViewController.setDeckVisibility(deck.isPublic());
            serverCommunicator.addDeckToMarketplace(deck);

        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
        }
    }

    /* ====================================================================== */
    /*                        Controller Listener                             */
    /* ====================================================================== */

    public interface ControllerListener {
        void onPlayDeckClicked(Deck deck);
    }
}
