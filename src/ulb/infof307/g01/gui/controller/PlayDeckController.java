package ulb.infof307.g01.gui.controller;

import javafx.stage.Stage;
import ulb.infof307.g01.gui.controller.errorhandler.ErrorHandler;
import ulb.infof307.g01.gui.http.ServerCommunicator;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.gui.view.playdeck.PlayDeckViewController;

import ulb.infof307.g01.gui.controller.exceptions.EmptyDeckException;
import ulb.infof307.g01.model.card.*;
import ulb.infof307.g01.model.deck.Deck;
import ulb.infof307.g01.model.deck.Score;

import java.util.Arrays;

public class PlayDeckController implements PlayDeckViewController.Listener {

    private final Stage stage;
    private final MainWindowViewController mainWindowViewController;
    private final PlayDeckViewController playDeckViewController;
    private final ControllerListener controllerListener;
    private final ErrorHandler errorHandler;
    private final ServerCommunicator serverCommunicator;
    private Score score;
    private CardExtractor cardExtractor;

    private Card currentCard;
    private boolean frontShown = true;
    private boolean[] answeredCards;

    public void setDeck(Deck deck) throws EmptyDeckException {
        if (deck == null || deck.cardCount() == 0)
            throw new EmptyDeckException("Deck does not contain any cards.");

        cardExtractor = new CardExtractorRandom(deck);
        currentCard = cardExtractor.getNextCard();
        this.score = Score.createNewScore(serverCommunicator.getSessionUsername(), deck.getId());
        this.answeredCards = new boolean[deck.cardCount()];
        Arrays.fill(answeredCards, false);

        playDeckViewController.setCurrentCard(currentCard, cardExtractor.getCurrentCardIndex());
        playDeckViewController.setDeckName(deck.getName());
        playDeckViewController.setNumberOfCards(deck.cardCount());
    }

    public void removeDeck() {
        cardExtractor = null;
        currentCard = null;
        this.score = null;
        this.answeredCards = null;
    }

    /* ====================================================================== */
    /*                              Constructor                               */
    /* ====================================================================== */

    public PlayDeckController(Stage stage,
                              MainWindowViewController mainWindowViewController,
                              ControllerListener controllerListener,
                              ErrorHandler errorHandler,
                              ServerCommunicator serverCommunicator) {

        this.stage = stage;
        this.serverCommunicator = serverCommunicator;
        this.controllerListener = controllerListener;
        this.errorHandler = errorHandler;
        this.mainWindowViewController = mainWindowViewController;
        this.playDeckViewController = mainWindowViewController.getPlayDeckViewController();
        playDeckViewController.setListener(this);
    }


    /* ====================================================================== */
    /*                         Stage Manipulation                             */
    /* ====================================================================== */

    public void show() throws EmptyDeckException {

        if (cardExtractor == null)
            throw new EmptyDeckException("Deck has not been set.");

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
        else if (currentCard instanceof InputCard)
            playDeckViewController.showInputCard();
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

        if (currentCard != null) {
            playDeckViewController.setCurrentCard(currentCard, cardExtractor.getCurrentCardIndex());
            showCard();
            return;
        }

        try {
            serverCommunicator.addScore(score);

        } catch (Exception e) {
            errorHandler.failedAddScore(e);

        } finally {
            controllerListener.finishedPlayingDeck(score);
        }
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

    @Override
    public void onChoiceEntered(boolean isGoodChoice) {
        int cardIndex = cardExtractor.getCurrentCardIndex();
        if (answeredCards[cardIndex])
            return;

        answeredCards[cardIndex] = true;
        if (isGoodChoice)
            score.increment(1);
    }


    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void finishedPlayingDeck(Score score);
    }
}
