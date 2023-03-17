package ulb.infof307.g01.view.deckmenu;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.database.DeckManager;
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
    private final DeckManager dm = DeckManager.singleton();

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
    }

    private void updateDeckButtonName() {
        this.deckButton.setText(this.deck.getName());
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
