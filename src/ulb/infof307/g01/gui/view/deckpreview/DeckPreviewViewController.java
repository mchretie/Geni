package ulb.infof307.g01.gui.view.deckpreview;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.deck.Deck;
import ulb.infof307.g01.model.deck.Score;

import java.util.List;

public class DeckPreviewViewController {

    @FXML
    private BorderPane borderPane;

    @FXML
    private ListView<Node> gameHistoryContainer;

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

    public void setGameHistory(List<Node> gameHistoryItem) {
        ObservableList<Node> items = FXCollections.observableArrayList(gameHistoryItem);
        gameHistoryContainer.setItems(items);

        borderPane.requestFocus();
        gameHistoryContainer.refresh();
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

    public void gameHistoryContainerClicked() {
    }


    /* ====================================================================== */
    /*                              Listener                                  */
    /* ====================================================================== */

    public interface Listener {
        void onPlayDeckClicked();
    }
}
