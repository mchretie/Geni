package ulb.infof307.g01.controller;

import javafx.stage.Stage;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.view.playdeck.PlayDeckViewController;

public class PlayDeckController implements PlayDeckViewController.Listener {

    private final Deck deck;
    private Card currentCard;

    private final Stage stage;

    private final MainWindowViewController mainWindowViewController;
    private final PlayDeckViewController playDeckViewController;

    public PlayDeckController(Stage stage, Deck deck, MainWindowViewController mainWindowViewController) {
        this.stage = stage;
        this.deck = deck;
        this.mainWindowViewController = mainWindowViewController;

        mainWindowViewController.makeGoBackIconVisible();

        this.playDeckViewController = mainWindowViewController.getPlayDeckViewController();
        playDeckViewController.setListener(this);
    }

    public void show() {
        mainWindowViewController.setPlayDeckViewVisible();
        mainWindowViewController.makeGoBackIconVisible();

        stage.show();
    }

    @Override
    public void cardClicked() {

    }

    @Override
    public void nextCardClicked() {

    }

    @Override
    public void previousCardClicked() {

    }
}
