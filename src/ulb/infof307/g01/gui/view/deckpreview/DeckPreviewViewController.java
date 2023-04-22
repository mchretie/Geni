package ulb.infof307.g01.gui.view.deckpreview;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.deck.Deck;
import ulb.infof307.g01.model.deck.Score;

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

    public void setScore(Score score) {
        highestScoreLabel.setText("Highscore: " + score.getScore() + " points");
    }

    private void setCardCountLabel() {
        int cardCount = deck.cardCount();
        String text = "Nombre de cartes: " + cardCount + " carte" + (cardCount > 1 ? "s" : "");
        cardCountLabel.setText(text);
    }

    public void setPlayDeckButtonDisabled(boolean disabled) {
        playDeck.setDisable(disabled);
    }

    public void handlePlayDeckClicked() {
        listener.onPlayDeckClicked();
    }

    public void setScoreUnavailable() {
        highestScoreLabel.setText("Score indisponible");
        highestScoreLabel.setTextFill(Color.RED);
    }


    /* ====================================================================== */
    /*                              Listener                                  */
    /* ====================================================================== */

    public interface Listener {
        void onPlayDeckClicked();
    }
}
