package ulb.infof307.g01.view.editdeck;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;

import java.io.IOException;
import java.util.List;

public class EditDeckViewController {

    @FXML
    public StackPane frontCard;
    public StackPane backCard;

    @FXML
    public TextField frontCardText;
    public TextField backCardText;
    public TextField deckName;

    @FXML
    private ListView<String> cardsContainer;

    private Deck deck;
    private Card selectedCard;

    private Listener listener;

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setSelectedCard(Card selectedCard) {
        this.selectedCard = selectedCard;
    }

    /* ============================================================================================================== */
    /*                                               Card and deck loading                                            */
    /* ============================================================================================================== */

    public void loadCardsFromDeck() {

        ObservableList<String> list = FXCollections.observableArrayList();
        cardsContainer.setItems(list);

        cardsContainer.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        for (Card card : deck)
            list.add(card.getFront());

        cardsContainer.refresh();
    }

    public void loadCardEditor(Card card) {
        frontCardText.setText(card.getFront());
        backCardText.setText(card.getBack());
        frontCard.setVisible(true);
        backCard.setVisible(true);
    }


    /* ============================================================================================================== */
    /*                                                  Mouse clicked                                                 */
    /* ============================================================================================================== */

    @FXML
    public void handleAddCardClicked() {
        listener.newCard();
    }

    @FXML
    public void handleCardPreviewClicked() {
        int cardIndex = cardsContainer.getSelectionModel().getSelectedIndex();
        selectedCard = deck.getCards().get(cardIndex);
        listener.cardPreviewClicked(selectedCard);
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
        if (!keyEvent.getCode().equals(KeyCode.ENTER))
            return;

        listener.frontOfCardModified(selectedCard, frontCardText.getText());
    }

    @FXML
    public void handleBackEdit(KeyEvent keyEvent) {
        if (!keyEvent.getCode().equals(KeyCode.ENTER))
            return;

        listener.backOfCardModified(selectedCard, backCardText.getText());
    }


    /* ============================================================================================================== */
    /*                                              Listener interface                                                */
    /* ============================================================================================================== */

    public interface Listener {
        void deckNameModified(String newName);
        void tagAddedToDeck(Deck deck, String tagName);
        void frontOfCardModified(Card card, String newFront);
        void backOfCardModified(Card card, String newBack);
        void newCard();
        void cardPreviewClicked(Card card);
    }
}
