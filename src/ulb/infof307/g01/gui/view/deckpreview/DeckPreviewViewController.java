package ulb.infof307.g01.gui.view.deckpreview;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.deck.Deck;

public class DeckPreviewViewController {

    @FXML
    private FontIcon playDeckIcon;

    @FXML
    private Button playDeck;

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

    public void setPlayDeckButtonDisabled(boolean disabled) {
        playDeck.setDisable(disabled);
    }



    public void handlePlayDeckClicked() {
        listener.onPlayDeckClicked();
    }


    /* ====================================================================== */
    /*                              Listener                                  */
    /* ====================================================================== */

    public interface Listener {
        void onPlayDeckClicked();
    }
}
