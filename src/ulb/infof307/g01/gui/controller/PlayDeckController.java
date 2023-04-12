package ulb.infof307.g01.gui.controller;

import javafx.stage.Stage;
import ulb.infof307.g01.gui.httpdao.dao.LeaderboardDAO;
import ulb.infof307.g01.gui.httpdao.dao.UserDAO;
import ulb.infof307.g01.model.*;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.gui.view.playdeck.PlayDeckViewController;

import ulb.infof307.g01.gui.controller.exceptions.EmptyDeckException;

import java.io.IOException;
import java.util.UUID;

public class PlayDeckController implements PlayDeckViewController.Listener {

    private final Stage stage;
    private final MainWindowViewController mainWindowViewController;
    private final PlayDeckViewController playDeckViewController;
    private final ControllerListener controllerListener;

    private final LeaderboardDAO leaderboardDAO;
    private final Score score;
    private final CardExtractor cardExtractor;
    private Card currentCard;
    private boolean frontShown = true;


    /* ====================================================================== */
    /*                              Constructor                               */
    /* ====================================================================== */

    public PlayDeckController(Stage stage, Deck deck,
                              MainWindowViewController mainWindowViewController,
                              ControllerListener controllerListener,
                              LeaderboardDAO leaderboardDAO, UserDAO userDAO) {

        this.stage = stage;
        this.cardExtractor = new CardExtractorRandom(deck);
        this.currentCard = cardExtractor.getNextCard();
        // TODO change hardcoded value / get User instance
        this.score = Score.createNewScore(
                UUID.fromString("3d9f80f8-f923-46a3-8178-1fe3067e5d7e"),
                "guest",
                deck.getId());
        this.leaderboardDAO = leaderboardDAO;
        this.leaderboardDAO.setToken(userDAO.getToken());

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

        if (currentCard != null) {
            playDeckViewController.setCurrentCard(currentCard, cardExtractor.getCurrentCardIndex());
            showCard();
            return;
        }

        try {
            leaderboardDAO.addScore(score);
        } catch (Exception e) {
            controllerListener.addScoreFailed(e);
        } finally {
            controllerListener.finishedPlayingDeck();
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
    public void correctChoiceButtonClicked() {
        score.increment(1);
    }


    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void finishedPlayingDeck();
        void addScoreFailed(Exception e);
    }
}
