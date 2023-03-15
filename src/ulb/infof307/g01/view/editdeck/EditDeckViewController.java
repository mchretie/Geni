package ulb.infof307.g01.view.editdeck;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;

public class EditDeckViewController {

    @FXML
    public VBox cardsContainer;
    public StackPane frontCard;
    public StackPane backCard;

    @FXML
    public TextField frontCardText;
    public TextField backCardText;
    public TextField deckName;

    private Deck deck;
    private Card selectedCard;

    private Listener listener;

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setSelectedCard(Card selectedCard) {
        this.selectedCard = selectedCard;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }


    /* ============================================================================================================== */
    /*                                                  Mouse clicked                                                 */
    /* ============================================================================================================== */

    @FXML
    public void handleAddCardClicked(ActionEvent actionEvent) {
    }

    /* ============================================================================================================== */
    /*                                                  Modified text                                                 */
    /* ============================================================================================================== */

    @FXML
    public void handleUpdateDeckName(KeyEvent keyEvent) {
        listener.deckNameModified(keyEvent.getText());
    }

    @FXML
    public void handleTagAdded(KeyEvent keyEvent) {
        listener.tagAddedToDeck(deck, keyEvent.getText());
    }

    @FXML
    public void handleFrontEdit(KeyEvent keyEvent) {
        listener.frontOfCardModified(selectedCard, keyEvent.getText());
    }

    @FXML
    public void handleBackEdit(KeyEvent keyEvent) {
        listener.backOfCardModified(selectedCard, keyEvent.getText());
    }

    /* ============================================================================================================== */
    /*                                              Listener interface                                                */
    /* ============================================================================================================== */

    public interface Listener {
        void deckNameModified(String newName);
        void tagAddedToDeck(Deck deck, String tagName);
        void frontOfCardModified(Card card, String newFront);
        void backOfCardModified(Card card, String newBack);
    }
}
