package ulb.infof307.g01.gui.controller;

import javafx.stage.Stage;
import ulb.infof307.g01.gui.httpdao.dao.ScoreDAO;
import ulb.infof307.g01.gui.view.deckpreview.DeckPreviewViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.model.deck.Deck;

import java.io.IOException;

public class DeckPreviewController implements DeckPreviewViewController.Listener {

    private final DeckPreviewViewController deckPreviewViewController;
    private final ControllerListener controllerListener;
    private final MainWindowViewController mainWindowViewController;

    private final Stage stage;
    private final ScoreDAO scoreDAO;

    private Deck deck;


    public DeckPreviewController(Stage stage,
                                 MainWindowViewController mainWindowViewController,
                                 ControllerListener controllerListener,
                                 ScoreDAO scoreDAO) {

        this.stage = stage;
        this.mainWindowViewController = mainWindowViewController;
        this.controllerListener = controllerListener;
        this.scoreDAO = scoreDAO;

        this.deckPreviewViewController
                = mainWindowViewController.getDeckPreviewViewController();

        deckPreviewViewController.setListener(this);
    }


    public void setDeck(Deck deck) {
        try {
            this.deck = deck;
            deckPreviewViewController.setDeck(deck);
            deckPreviewViewController.setScore(scoreDAO.getBestScoreForDeck(deck.getId()));
            deckPreviewViewController.setPlayDeckButtonDisabled(deck.cardCount() == 0);

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


    /* ====================================================================== */
    /*                          Listener methods                              */
    /* ====================================================================== */

    @Override
    public void onPlayDeckClicked() {
        controllerListener.onPlayDeckClicked(deck);
    }

    /* ====================================================================== */
    /*                        Controller Listener                             */
    /* ====================================================================== */

    public interface ControllerListener {
        void onPlayDeckClicked(Deck deck);
    }
}
