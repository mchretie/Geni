package ulb.infof307.g01.gui.controller;

import javafx.stage.Stage;
import ulb.infof307.g01.gui.controller.errorhandler.ErrorHandler;
import ulb.infof307.g01.gui.httpdao.dao.LeaderboardDAO;
import ulb.infof307.g01.gui.httpdao.dao.UserSessionDAO;
import ulb.infof307.g01.model.*;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.gui.view.playdeck.PlayDeckViewController;

import ulb.infof307.g01.gui.controller.exceptions.EmptyDeckException;

import java.util.Arrays;

public class PlayDeckController implements PlayDeckViewController.Listener {

    private final Stage stage;
    private final MainWindowViewController mainWindowViewController;
    private final PlayDeckViewController playDeckViewController;
    private final ControllerListener controllerListener;
    private final ErrorHandler errorHandler;
    private final LeaderboardDAO leaderboardDAO;
    private final Score score;
    private final CardExtractor cardExtractor;
    private Card currentCard;
    private boolean frontShown = true;
    private boolean[] answeredCards;    // needed because user can go back to previous question


    /* ====================================================================== */
    /*                              Constructor                               */
    /* ====================================================================== */

    public PlayDeckController(Stage stage, Deck deck,
                              MainWindowViewController mainWindowViewController,
                              ControllerListener controllerListener,
                              ErrorHandler errorHandler,
                              LeaderboardDAO leaderboardDAO, UserSessionDAO userDAO) {

        this.stage = stage;
        this.cardExtractor = new CardExtractorRandom(deck);
        this.currentCard = cardExtractor.getNextCard();
        this.score = Score.createNewScore(userDAO.getUsername(), deck.getId());
        this.answeredCards = new boolean[deck.cardCount()];
        Arrays.fill(answeredCards, false);
        this.leaderboardDAO = leaderboardDAO;
        this.leaderboardDAO.setToken(userDAO.getToken());

        if (currentCard == null)
            throw new EmptyDeckException("Deck does not contain any cards.");

        this.controllerListener = controllerListener;
        this.errorHandler = errorHandler;
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
            leaderboardDAO.addScore(score);
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
