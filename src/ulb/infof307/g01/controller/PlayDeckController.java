package ulb.infof307.g01.controller;

import javafx.stage.Stage;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.CardExtractor;
import ulb.infof307.g01.model.CardExtractorRandom;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.view.playdeck.PlayDeckViewController;

public class PlayDeckController implements PlayDeckViewController.Listener {

    private final CardExtractor cardExtractor;
    private Card currentCard;

    private final Stage stage;

    private final MainWindowViewController mainWindowViewController;
    private final PlayDeckViewController playDeckViewController;
    private final ControllerListener controllerListener;

    private boolean frontIsShown = true;

    /* ====================================================================== */
    /*                              Constructor                               */
    /* ====================================================================== */

    public PlayDeckController(Stage stage, Deck deck,
                              MainWindowViewController mainWindowViewController,
                              ControllerListener controllerListener) {

        this.stage = stage;
        this.cardExtractor = new CardExtractorRandom(deck);
        this.currentCard = cardExtractor.getNextCard();
        this.controllerListener = controllerListener;

        this.mainWindowViewController = mainWindowViewController;
        mainWindowViewController.makeGoBackIconVisible();

        this.playDeckViewController = mainWindowViewController.getPlayDeckViewController();
        playDeckViewController.setListener(this);
        playDeckViewController.setDeckName(deck.getName());
    }


    /* ====================================================================== */
    /*                         Stage Manipulation                             */
    /* ====================================================================== */

    public void show() {
        mainWindowViewController.setPlayDeckViewVisible();
        mainWindowViewController.makeGoBackIconVisible();

        playDeckViewController.showFrontOfCard(currentCard);

        stage.show();
    }


    /* ====================================================================== */
    /*                        View Listener Method                            */
    /* ====================================================================== */

    @Override
    public void cardClicked() {

        if (frontIsShown) {
            playDeckViewController.showBackOfCard(currentCard);
            frontIsShown = false;
        }

        else {
            playDeckViewController.showFrontOfCard(currentCard);
            frontIsShown = true;
        }

    }

    @Override
    public void nextCardClicked() {
        currentCard = cardExtractor.getNextCard();
        if (currentCard == null) {
            controllerListener.finishedPlayingDeck();
            return;
        }

        frontIsShown = true;
        playDeckViewController.showFrontOfCard(currentCard);
    }

    @Override
    public void previousCardClicked() {
        Card previousCard = cardExtractor.getPreviousCard();
        if (previousCard == null)
            return;

        currentCard = previousCard;
        frontIsShown = true;
        playDeckViewController.showFrontOfCard(currentCard);
    }


    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void finishedPlayingDeck();
    }
}
