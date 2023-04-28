package ulb.infof307.g01.gui.view.deckpreview;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.deck.Deck;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class DeckPreviewViewController {

    @FXML
    private FontIcon shareDeckIcon;

    @FXML
    private FontIcon deckVisibilityIcon;

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

        int cardCount = deck.cardCount();
        String text = cardCount + " carte" + (cardCount > 1 ? "s" : "");
        cardCountLabel.setText(text);
    }

    public void setScore(String score) {
        highestScoreLabel.setText(score + " points");
    }

    public void setScoreUnavailable() {
        highestScoreLabel.setText("Score indisponible");
        highestScoreLabel.setTextFill(Color.RED);
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

    public void setDeckVisibility(boolean visibleOnline) {
        if (visibleOnline) {
            deckVisibilityIcon.setIconLiteral("mdi2a-account-group");
            shareDeckIcon.setVisible(false);

        } else {
            deckVisibilityIcon.setIconLiteral("mdi2a-account-lock");
            shareDeckIcon.setVisible(true);
        }
    }


    /* ====================================================================== */
    /*                              Click Handlers                            */
    /* ====================================================================== */

    @FXML
    private void handlePlayDeckClicked() {
        listener.playDeckClicked();
    }

    @FXML
    private void gameHistoryContainerClicked() {
        gameHistoryContainer.getSelectionModel().clearSelection();
        borderPane.requestFocus();
    }

    @FXML
    private void handlePlayDeckEntered() {
        String style = "-fx-background-color: transparent;";
        playDeck.setStyle(style + "-fx-text-fill: white;");
        playDeckIcon.setIconColor(Color.WHITE);
    }

    @FXML
    private void handlePlayDeckExited() {
        String style = "-fx-background-color: transparent;";
        playDeck.setStyle(style + "-fx-text-fill: black;");
        playDeckIcon.setIconColor(Color.BLACK);
    }

    @FXML
    private void handleShareDeckClicked() {
        listener.deckShared();
    }


    /* ====================================================================== */
    /*                              Hover Handlers                            */
    /* ====================================================================== */

    @FXML
    private void handleShareDeckEntered() {
        shareDeckIcon.setIconColor(Color.WHITE);
    }

    @FXML
    private void handleShareDeckExited() {
        shareDeckIcon.setIconColor(Color.BLACK);
    }


    /* ====================================================================== */
    /*                              Listener                                  */
    /* ====================================================================== */

    public interface Listener {
        void playDeckClicked();
        void deckShared();
    }
}
