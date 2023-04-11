package ulb.infof307.g01.gui.controller;

import javafx.stage.Stage;
import ulb.infof307.g01.model.*;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.gui.view.playdeck.PlayDeckViewController;

import ulb.infof307.g01.gui.controller.exceptions.EmptyDeckException;

public class PlayDeckController implements PlayDeckViewController.Listener {

    private final CardExtractor cardExtractor;
    private Card currentCard;
    private boolean frontShown = true;

    private final Stage stage;

    private final MainWindowViewController mainWindowViewController;
    private final PlayDeckViewController playDeckViewController;
    private final ControllerListener controllerListener;

    /* ====================================================================== */
    /*                              Constructor                               */
    /* ====================================================================== */

    public PlayDeckController(Stage stage, Deck deck,
                              MainWindowViewController mainWindowViewController,
                              ControllerListener controllerListener) {

        this.stage = stage;
        this.cardExtractor = new CardExtractorRandom(deck);
        this.currentCard = cardExtractor.getNextCard();

        if (currentCard == null)
            throw new EmptyDeckException("Deck does not contain any cards.");

        this.controllerListener = controllerListener;
        this.mainWindowViewController = mainWindowViewController;
        mainWindowViewController.makeGoBackIconVisible();

        this.playDeckViewController = mainWindowViewController.getPlayDeckViewController();
        playDeckViewController.setListener(this);
        playDeckViewController.setCurrentCard(currentCard, cardExtractor.getCurrentCardIndex());
        playDeckViewController.setDeckName(deck.getName());
        playDeckViewController.setNumberOfCards(deck.cardCount());
    }


    /* ====================================================================== */
    /*                         Stage Manipulation                             */
    /* ====================================================================== */

    public void show() {
        mainWindowViewController.setPlayDeckViewVisible();
        mainWindowViewController.makeGoBackIconVisible();

        showCard();
        stage.show();
    }

    public void showCard(){
        if (currentCard instanceof FlashCard)
            playDeckViewController.showNormalCard();
        else if (currentCard instanceof MCQCard)
            playDeckViewController.showMCQCard();
    }

    /* ====================================================================== */
    /*                        View Listener Method                            */
    /* ====================================================================== */

    @Override
    public void cardClicked() {
        if (currentCard instanceof FlashCard){
            if (frontShown)
                playDeckViewController.flipToBackOfCard();
            else
                playDeckViewController.flipToFrontOfCard();
            frontShown = !frontShown;
        }
    }

    @Override
    public void nextCardClicked() {
        frontShown = true;
        currentCard = cardExtractor.getNextCard();

        if (currentCard != null)
            playDeckViewController.setCurrentCard(currentCard, cardExtractor.getCurrentCardIndex());

        else
            controllerListener.finishedPlayingDeck();

        showCard();
    }

    @Override
    public void previousCardClicked() {
        Card previousCard = cardExtractor.getPreviousCard();

        if (previousCard == null)
            return;

        currentCard = previousCard;
        playDeckViewController.setCurrentCard(currentCard, cardExtractor.getCurrentCardIndex());

        showCard();
    }


    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void finishedPlayingDeck();
    }
}
