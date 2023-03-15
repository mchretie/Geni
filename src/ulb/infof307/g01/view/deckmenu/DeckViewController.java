package ulb.infof307.g01.view.deckmenu;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.database.DeckDAO;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;

import java.util.List;

public class DeckViewController {

    @FXML
    public Rectangle optionsOverlayRect;

    @FXML
    public Button deckButton;
    public Button shareDeckButton;
    public Button deleteDeckButton;
    public Button editDeck;

    @FXML
    public FontIcon editDeckIcon;
    public FontIcon removeDeckIcon;
    public FontIcon shareDeckIcon;

    @FXML
    public StackPane homeDeckPane;
    public FlowPane deckTags;

    private Deck deck;
    private final DeckDAO dm = DeckDAO.singleton();

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    /* ============================================================================================================== */
    /*                                                 Updating Deck                                                  */
    /* ============================================================================================================== */

    public void setDeck(Deck deck) {
        this.deck = deck;
        this.updateDeckButtonName();
        this.updateDeckTags();
    }

    private void updateDeckButtonName() {
        this.deckButton.setText(this.deck.getName());
    }

    private void updateDeckTags() {
        this.deckTags.getChildren().clear();
        List<Tag> tags = this.deck.getTags();
        int tagCount = 4;
        if (tags.size() < tagCount) {
            tagCount = this.deck.getTags().size();
        }
        for (int i = 0; i < tagCount; i++) {
            Tag tag = tags.get(i);
            StackPane tagPane = new StackPane();
            tagPane.setPrefWidth(80);
            tagPane.setPrefHeight(30);
            tagPane.setStyle("-fx-background-color: #%s; -fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #000000; -fx-border-width: 1;".formatted(tag.getColor()));
            Label tagLabel = new Label(tag.getName());
            tagLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 12;");
            tagPane.getChildren().add(tagLabel);
            this.deckTags.getChildren().add(tagPane);
        }
    }

    /* ============================================================================================================== */
    /*                                                  Click handlers                                                */
    /* ============================================================================================================== */

    @FXML
    public void handleEditDeckClicked() {
        listener.editDeckClicked(deck);
    }

    @FXML
    public void handleDoubleDeckClicked() {
        listener.deckDoubleClicked(deck);
    }

    @FXML
    public void handleRemoveDeckClicked() {
        listener.deckRemoved(deck);
    }


    /* ============================================================================================================== */
    /*                                                  Hover handlers                                                */
    /* ============================================================================================================== */

    @FXML
    public void handleHoverEditDeck() {
        editDeckIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    public void handleHoverEditDeckExit() {
        editDeckIcon.setIconColor(Color.web("#000000"));
    }

    @FXML
    public void handleHoverRemoveDeck() {
        removeDeckIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    public void handleHoverRemoveDeckExit() {
        removeDeckIcon.setIconColor(Color.web("#000000"));
    }

    @FXML
    public void handleHoverShareDeck() {
        shareDeckIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    public void handleHoverShareDeckExit() {
        shareDeckIcon.setIconColor(Color.web("#000000"));
    }


    /* ============================================================================================================== */
    /*                                                Listener interface                                              */
    /* ============================================================================================================== */

    public interface Listener {
        void deckRemoved(Deck deck);
        void deckDoubleClicked(Deck deck);
        void editDeckClicked(Deck deck);
    }
}
