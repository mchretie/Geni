package ulb.infof307.g01.gui.view.deckpreview;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.deck.Deck;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DeckPreviewViewController implements Initializable {

    @FXML
    private BorderPane borderPane;

    @FXML
    private ListView<Node> gameHistoryContainer;
    
    @FXML
    private FontIcon playDeckIcon;

    @FXML
    private Button playDeck;

    @FXML
    private Label cardCountLabel;

    @FXML
    private Label highestScoreLabel;

    private HBox starContainer;
    private FontIcon shareDeckIcon;
    private Label deckNameLabel;
    private FontIcon deckVisibilityIcon;

    private Listener listener;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        starHBox();

        HBox topHbox = new HBox(starContainer, new Region(), deckNameHbox(), new Region(), deckShareHBox());
        topHbox.setStyle("-fx-background-radius: 10 10 0 0; " +
                            "-fx-border-radius: 10 10 0 0; " +
                            "-fx-border-style: hidden hidden solid hidden; " +
                            "-fx-border-color: lightgrey; " +
                            "-fx-background-color: #C3B1E1;");

        borderPane.setTop(topHbox);
    }

    private void starHBox() {
        starContainer = new HBox();
        starContainer.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(starContainer, Priority.ALWAYS);

        for (int starIndex = 0; starIndex < 5; starIndex++) {
            Button starButton = new Button();
            starButton.setStyle("-fx-background-color: transparent; " +
                                    "-fx-border-color: transparent;");

            FontIcon starIcon = new FontIcon("lsf-starempty");
            starIcon.setIconSize(20);

            int finalStarIndex = starIndex;
            starButton.setOnMouseClicked(event -> listener.starClicked(finalStarIndex));
            starButton.setOnMouseEntered(event -> listener.starEntered(finalStarIndex));
            starButton.setOnMouseExited(event -> listener.starExited());

            starButton.setGraphic(starIcon);
            starContainer.getChildren().add(starButton);
        }
    }

    private HBox deckShareHBox() {
        Button shareDeckButton = new Button();
        shareDeckIcon = new FontIcon("mdi2s-share");
        shareDeckIcon.setIconSize(20);

        shareDeckButton.setGraphic(shareDeckIcon);
        shareDeckButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        shareDeckButton.setOnMouseEntered(event -> shareDeckIcon.setIconColor(Color.WHITE));
        shareDeckButton.setOnMouseExited(event -> shareDeckIcon.setIconColor(Color.BLACK));

        shareDeckButton.setOnMouseClicked(event -> listener.deckSharedClicked());

        HBox hBox = new HBox(shareDeckButton);
        HBox.setHgrow(hBox, Priority.ALWAYS);
        hBox.setAlignment(Pos.CENTER_RIGHT);

        return hBox;
    }

    private HBox deckNameHbox() {
        deckNameLabel = new Label();
        deckNameLabel.setFont(Font.font(20));

        deckVisibilityIcon = new FontIcon("mdi2s-share");
        deckVisibilityIcon.setIconSize(20);

        HBox hBox = new HBox(deckNameLabel, deckVisibilityIcon);
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(hBox, Priority.ALWAYS);
        HBox.setMargin(deckNameLabel, new Insets(10, 0, 10, 0));

        return hBox;
    }

    /* ====================================================================== */
    /*                                 Setters                                */
    /* ====================================================================== */

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setDeck(Deck deck) {
        deckNameLabel.setText(deck.getName());
        int cardCount = deck.cardCount();
        String text = cardCount + " carte" + (cardCount > 1 ? "s" : "");
        cardCountLabel.setText(text);
    }

    public void setScore(String score) {
        highestScoreLabel.setText(score + " points");
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
        shareDeckIcon.setVisible(!visibleOnline);
        starContainer.setVisible(visibleOnline);

        if (visibleOnline) {
            deckVisibilityIcon.setIconLiteral("mdi2a-account-group");

        } else {
            deckVisibilityIcon.setIconLiteral("mdi2a-account-lock");
        }
    }

    public void setStars(int startIndex) {
        for (int i = 0; i < 5; i++) {
            Button starButton = (Button) starContainer.getChildren().get(i);
            FontIcon starIcon = (FontIcon) starButton.getGraphic();

            if (i <= startIndex) {
                starIcon.setIconLiteral("lsf-star");
            }
            else {
                starIcon.setIconLiteral("lsf-starempty");
            }
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


    /* ====================================================================== */
    /*                              Listener                                  */
    /* ====================================================================== */

    public interface Listener {
        void playDeckClicked();
        void deckSharedClicked();

        void starClicked(int startIndex);
        void starEntered(int starIndex);
        void starExited();
    }
}
