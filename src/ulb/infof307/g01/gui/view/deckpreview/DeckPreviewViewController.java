package ulb.infof307.g01.gui.view.deckpreview;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ulb.infof307.g01.model.deck.Deck;

public class DeckPreviewViewController {

    @FXML
    private Label deckNameLabel;

    @FXML
    private Label cardCountLabel;

    @FXML
    private Label highestScoreLabel;

    private Deck deck;

    private Listener listener;

    /* ====================================================================== */
    /*                                 Setters                                */
    /* ====================================================================== */

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
        deckNameLabel.setText(deck.getName());
        setCardCountLabel();
    }

    public void setScore(int score) {
        highestScoreLabel.setText(score + " points");
    }

    private void setCardCountLabel() {
        int cardCount = deck.cardCount();
        cardCountLabel.setText(cardCount + " carte" + (cardCount > 1 ? "s" : ""));
    }


    public void handlePlayDeckClicked() {
        listener.onPlayDeckClicked(deck);
    }


    /* ====================================================================== */
    /*                              Listener                                  */
    /* ====================================================================== */

    public interface Listener {
        void onPlayDeckClicked(Deck deck);
    }
}
