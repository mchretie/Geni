package ulb.infof307.g01.view.editdeck;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;

import java.net.URL;
import java.util.ResourceBundle;

public class EditDeckViewController implements Initializable {

    @FXML
    public StackPane frontCard;
    public StackPane backCard;
    public AnchorPane anchor;

    @FXML
    public TextField frontCardText;
    public TextField backCardText;
    public TextField deckNameText;

    @FXML
    public FontIcon removeCardIcon;
    public FontIcon addCardIcon;

    @FXML
    private ListView<String> cardsContainer;

    private Deck deck;
    private Card selectedCard;

    private Listener listener;

    /* ============================================================================================================== */
    /*                                                   Initializer                                                  */
    /* ============================================================================================================== */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        frontCardText.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) handleFrontEdit();
        });

        backCardText.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) handleBackEdit();
        });

        deckNameText.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) handleUpdateDeckName();
        });
    }


    /* ============================================================================================================== */
    /*                                                  Setters                                                       */
    /* ============================================================================================================== */

    public void setDeck(Deck deck) {
        this.deck = deck;
        deckNameText.setText(deck.getName());
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setSelectedCard(Card selectedCard) { this.selectedCard = selectedCard; }


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

    private void loadCardEditor(Card card) {
        frontCardText.setText(card.getFront());
        backCardText.setText(card.getBack());
        frontCard.setVisible(true);
        backCard.setVisible(true);
    }

    public void loadSelectedCardEditor() {
        loadCardEditor(selectedCard);
    }

    public void hideSelectedCardEditor() {
        frontCard.setVisible(false);
        backCard.setVisible(false);
    }


    /* ============================================================================================================== */
    /*                                                  Mouse clicked                                                 */
    /* ============================================================================================================== */

    @FXML
    public void handleAddCardClicked() {
        listener.newCard();
    }

    @FXML
    public void handleRemoveCardClicked() { listener.removeCard(selectedCard);}

    @FXML
    public void handleCardPreviewClicked() {
        int cardIndex = cardsContainer.getSelectionModel().getSelectedIndex();

        if (cardIndex < 0)
            return;

        selectedCard = deck.getCards().get(cardIndex);
        listener.cardPreviewClicked(selectedCard);
    }

    @FXML
    public void handleRemoveCardHover() {
        removeCardIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    public void handleRemoveCardHoverExit() {
        removeCardIcon.setIconColor(Color.web("#000000"));
    }

    @FXML
    public void handleAddCardHover() {
        addCardIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    public void handleAddHoverExit() {
        addCardIcon.setIconColor(Color.web("#000000"));
    }



    /* ============================================================================================================== */
    /*                                                  Modified text                                                 */
    /* ============================================================================================================== */

    @FXML
    public void handleUpdateDeckName() {
        listener.deckNameModified(deckNameText.getText());
    }

    @FXML
    public void handleTagAdded() {
        listener.tagAddedToDeck(deck, "Tag");
    }

    @FXML
    public void handleFrontEdit() {
        listener.frontOfCardModified(selectedCard, frontCardText.getText());
    }

    @FXML
    public void handleBackEdit() {
        listener.backOfCardModified(selectedCard, backCardText.getText());
    }

    @FXML
    public void handleTextFieldKeyPressed(KeyEvent keyEvent) {
        if (!keyEvent.getCode().equals(KeyCode.ENTER))
            return;

        anchor.requestFocus();
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
        void removeCard(Card selectedCard);
        void cardPreviewClicked(Card card);
    }
}
