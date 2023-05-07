package ulb.infof307.g01.gui.controller;

import javafx.stage.Stage;
import ulb.infof307.g01.gui.controller.errorhandler.ErrorHandler;
import ulb.infof307.g01.gui.controller.exceptions.EmptyDeckException;
import ulb.infof307.g01.gui.http.ServerCommunicator;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.gui.view.playdeck.PlayDeckViewController;
import ulb.infof307.g01.model.card.*;
import ulb.infof307.g01.model.card.extractor.CardExtractor;
import ulb.infof307.g01.model.card.extractor.CardExtractorRandom;
import ulb.infof307.g01.model.card.visitor.CardVisitor;
import ulb.infof307.g01.model.deck.Deck;
import ulb.infof307.g01.model.deck.Score;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;

public class PlayDeckController implements PlayDeckViewController.Listener,
                                            CardVisitor {

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
    private boolean progressbarTimedOut = true;
    private boolean[] answeredCards;

    public void setDeck(Deck deck) throws EmptyDeckException {
        if (deck == null || deck.cardCount() == 0)
            throw new EmptyDeckException("Deck does not contain any cards.");

        cardExtractor = new CardExtractorRandom(deck);
        currentCard = cardExtractor.getNextCard();
        this.score = new Score(serverCommunicator.getSessionUsername(), deck.getId());

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

    private void showCard() {
        currentCard.accept(this);
    }


    /* ====================================================================== */
    /*                         Card Visitor Methods                           */
    /* ====================================================================== */

    @Override
    public void visit(FlashCard flashCard) {
        playDeckViewController.showNormalCard();
        playDeckViewController.enableFrontCardClick();
        playDeckViewController.hideProgressBar();
    }

    @Override
    public void visit(MCQCard multipleChoiceCard) {
        playDeckViewController.showMCQCard();
        playDeckViewController.disableFrontCardClick();

        if (progressbarTimedOut) {
            playDeckViewController.startProgressBar();
            progressbarTimedOut = false;
        }

        else if (playDeckViewController.timerHasRunOut()) {
            playDeckViewController.showMCQAnswer();
            progressbarTimedOut = true;
        }

        else playDeckViewController.startProgressBar();
    }

    @Override
    public void visit(InputCard inputCard) {
        playDeckViewController.showInputCard();
        playDeckViewController.disableFrontCardClick();

        if (progressbarTimedOut) {
            playDeckViewController.startProgressBar();
            progressbarTimedOut = false;
        }

        else if (playDeckViewController.timerHasRunOut()) {
            playDeckViewController.showInputAnswer();
            progressbarTimedOut = true;
        }

        else playDeckViewController.startProgressBar();
    }

    /* ====================================================================== */
    /*                        View Listener Method                            */
    /* ====================================================================== */

    @Override
    public void cardClicked() {
        if (frontShown)
            playDeckViewController.flipToBackOfCard();
        else
            playDeckViewController.flipToFrontOfCard();
        frontShown = !frontShown;
    }

    @Override
    public void nextCardClicked() {
        frontShown = true;
        currentCard = cardExtractor.getNextCard();
        playDeckViewController.hideProgressBar();

        if (currentCard != null) {
            playDeckViewController.setCurrentCard(currentCard, cardExtractor.getCurrentCardIndex());
            showCard();
            return;
        }

        // TODO: This is a temporary fix.
        //  The 0 competitive cards case should be handled elsewhere.

        int divider = cardExtractor.getAmountCompetitiveCards() == 0 ? 1 : cardExtractor.getAmountCompetitiveCards();
        int totalScore = score.getScore() / divider;
        score.setScore(totalScore);

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
    public void onChoiceEntered(boolean isGoodChoice, double timeLeft) {
        int cardIndex = cardExtractor.getCurrentCardIndex();

        if (answeredCards[cardIndex]) {
            return;
        }

        answeredCards[cardIndex] = true;
        if (isGoodChoice) {
            double x = (timeLeft - 0.5) * 4;
            int scoreToAdd = (int) (1000 / (1 + Math.exp(-2 * x)));
            score.increment(scoreToAdd);
        }

        score.addTime(((TimedCard) currentCard).getCountdownTime()-timeLeft*10);
    }

    @Override
    public void timerRanOut() {
        showCard();
    }


    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void finishedPlayingDeck(Score score);
    }
}
