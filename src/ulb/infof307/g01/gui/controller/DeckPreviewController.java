package ulb.infof307.g01.gui.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;
import ulb.infof307.g01.gui.controller.errorhandler.ErrorHandler;
import ulb.infof307.g01.gui.httpdao.dao.*;
import ulb.infof307.g01.gui.view.deckpreview.DeckPreviewViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.gui.view.deckpreview.GameHistoryItemViewController;
import ulb.infof307.g01.model.deck.Deck;
import ulb.infof307.g01.model.deck.Score;
import ulb.infof307.g01.model.gamehistory.Game;
import ulb.infof307.g01.model.gamehistory.GameHistory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DeckPreviewController implements DeckPreviewViewController.Listener {

    private final DeckPreviewViewController deckPreviewViewController;
    private final ControllerListener controllerListener;
    private final MainWindowViewController mainWindowViewController;

    private final Stage stage;
    private final ErrorHandler errorHandler;
    private final ScoreDAO scoreDAO;
    private final GameHistoryDAO gameHistoryDAO;
    private final DeckDAO deckDAO;
    private final MarketplaceDAO marketplaceDAO;

    private Deck deck;


    public DeckPreviewController(Stage stage,
                                 MainWindowViewController mainWindowViewController,
                                 ControllerListener controllerListener,
                                 ErrorHandler errorHandler,
                                 ScoreDAO scoreDAO,
                                 DeckDAO deckDAO,
                                 MarketplaceDAO marketplaceDAO,
                                 GameHistoryDAO gameHistoryDAO) {

        this.stage = stage;
        this.mainWindowViewController = mainWindowViewController;
        this.controllerListener = controllerListener;
        this.errorHandler = errorHandler;
        this.scoreDAO = scoreDAO;
        this.gameHistoryDAO = gameHistoryDAO;
        this.deckDAO = deckDAO;
        this.marketplaceDAO = marketplaceDAO;

        this.deckPreviewViewController
                = mainWindowViewController.getDeckPreviewViewController();

        deckPreviewViewController.setListener(this);
    }


    public void setDeck(Deck deck) {
        try {
            this.deck = deck;
            deckPreviewViewController.setDeck(deck);

            Score score = scoreDAO.getBestScoreForDeck(deck.getId());
            String scoreString = score == null ? "0" : score.getScore() + "";
            deckPreviewViewController.setScore(scoreString);

            deckPreviewViewController.setPlayDeckButtonDisabled(deck.cardCount() == 0);
            deckPreviewViewController.setDeckVisibility(deck.isPublic());
            deckPreviewViewController.setGameHistory(loadGameHistory());

        } catch (IOException | InterruptedException e) {
            deckPreviewViewController.setScoreUnavailable();
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


    private List<Node> loadGameHistory(){
        try {
            List<Node> playersScoreItem = new ArrayList<>();

            GameHistory gameHistory = gameHistoryDAO.getGameHistory(deck.getId());

            for (Game game : gameHistory) {
                Node node = loadGameHistoryItem(game);
                playersScoreItem.add(node);
            }

            return playersScoreItem;

        } catch (IOException | InterruptedException e) {
            errorHandler.failedLoading(e);
            return new ArrayList<>();
        }
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
            marketplaceDAO.addDeckToMarketplace(deck);
            //deckDAO.emptyCache();

        } catch (IOException | InterruptedException e) {
            errorHandler.savingError(e);
        }
    }

    /* ====================================================================== */
    /*                        Controller Listener                             */
    /* ====================================================================== */

    public interface ControllerListener {
        void onPlayDeckClicked(Deck deck);
    }
}
