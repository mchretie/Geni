package ulb.infof307.g01.gui.view.editdeck;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditDeckViewController implements Initializable {
    @FXML
    private HBox cardTypeButtons;

    @FXML
    private AnchorPane cardEditor;

    @FXML
    private AnchorPane anchor;

    @FXML
    private TextField deckNameText;

    @FXML
    private FontIcon removeCardIcon;
    @FXML
    private FontIcon addCardIcon;

    @FXML
    private ListView<String> cardsContainer;

    @FXML
    private HBox tagsBox;

    @FXML
    private TextField tagsInput;

    @FXML
    private ColorPicker colorPicker;

    private Deck deck;
    private Card selectedCard;

    private Listener listener;

    /* ====================================================================== */
    /*                              Initializer                               */
    /* ====================================================================== */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deckNameText.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) handleUpdateDeckName();
        });
    }


    /* ====================================================================== */
    /*                                Setters                                 */
    /* ====================================================================== */

    public void setDeck(Deck deck) {
        this.deck = deck;
        deckNameText.setText(deck.getName());
        colorPicker.setValue(Color.web(deck.getColor()));
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setSelectedCard(Card selectedCard) {
        this.selectedCard = selectedCard;

        int cardIndex = deck.getCards().indexOf(selectedCard);
        if (cardIndex >= 0) {
            cardsContainer.getSelectionModel().select(cardIndex);
        }
    }

    public void setCardEditors(Node node){
        cardEditor.getChildren().add(node);
        hideFlashCardEditor();
        hideQCMCardEditor();
    }

    /* ====================================================================== */
    /*                         Tags, Card, deck loading                       */
    /* ====================================================================== */

    public void loadCardsFromDeck() {
        ObservableList<String> list = FXCollections.observableArrayList();
        cardsContainer.setItems(list);

        cardsContainer.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        for (Card card : deck)
            list.add(card.getFront());

        cardsContainer.refresh();
    }

    public void loadTagsFromDeck(){
        tagsBox.getChildren().clear();
        for (Tag tag: deck.getTags()) { addTagToView(tag.getName(), tag.getColor()); }
    }

    private void addTagToView(String text, String color) {
        StackPane tagPane = new StackPane();
        tagPane.setMaxHeight(20);
        tagPane.setStyle("-fx-border-color: #000000; " +
                "-fx-padding: 6px 10px; " +
                "-fx-border-insets: 3px 5px; " +
                "-fx-background-insets: 3px 5px; " +
                "-fx-border-radius: 15px; " +
                "-fx-background-radius: 15px; " +
                "-fx-background-color: " + color);

        Text tagText = new Text(text.trim());
        tagPane.getChildren().add(tagText);
        tagsBox.getChildren().add(tagPane);
    }


    /* ====================================================================== */
    /*                             Click handlers                             */
    /* ====================================================================== */

    @FXML
    private void handleAddCardClicked() {
        hideFlashCardEditor();
        showCardTypeButtons();
    }

    @FXML
    private void handleRemoveCardClicked() { listener.removeCard(selectedCard);}

    @FXML
    private void handleCardPreviewClicked() throws IOException {

        int cardIndex = cardsContainer.getSelectionModel().getSelectedIndex();
        if (cardIndex < 0)
            return;

        showFlashCardEditor();
        selectedCard = deck.getCards().get(cardIndex);

        listener.cardPreviewClicked(selectedCard);
    }

    @FXML
    private void handleFlashCardEdit(){
        listener.newCard(); //TODO newCard avec para flashCardEditor
        hideCardTypeButtons();
        showFlashCardEditor();
    }

    @FXML
    private void handleQCMCardEdit() {
        listener.newCard(); //TODO newCard avec para QCMCardEditor
        hideCardTypeButtons();
        showQCMCardEditor();
    }

    @FXML
    private void handleRemoveCardHover() {
        removeCardIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    private void handleRemoveCardHoverExit() {
        removeCardIcon.setIconColor(Color.web("#000000"));
    }

    @FXML
    private void handleAddCardHover() {
        addCardIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    private void handleAddHoverExit() {
        addCardIcon.setIconColor(Color.web("#000000"));
    }

    private void hideCardTypeButtons() {cardTypeButtons.setVisible(false);}

    private void showCardTypeButtons() {cardTypeButtons.setVisible(true);}

    public void hideFlashCardEditor() {
        cardEditor.getChildren().get(1).setVisible(false);
    }
    public void showFlashCardEditor() {
        cardEditor.getChildren().get(1).setVisible(true);
    }

    public void hideQCMCardEditor() {
        return;
//        cardEditor.getChildren().get(2).setVisible(false);
    }
    public void showQCMCardEditor() {
        return;
//        cardEditor.getChildren().get(2).setVisible(true);
    }


    /* ====================================================================== */
    /*                            Modified text                               */
    /* ====================================================================== */

    @FXML
    private void handleUpdateDeckName() {
        listener.deckNameModified(deckNameText.getText());
    }

    @FXML
    private void handleTagAdded(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            Color color = Color.color(Math.random(), Math.random(), Math.random());
            String colorStr = color.toString().replace("0x", "#");

            String tagText = tagsInput.getText();

            if (!deck.tagExists(tagText)) {
                addTagToView(tagText, colorStr);
                tagsInput.setText("");

                listener.tagAddedToDeck(deck, tagText, colorStr);
            }
        }
    }

    @FXML
    public void handleColorButtonClicked(ActionEvent actionEvent) {
        listener.deckColorModified(deck, colorPicker.getValue());
    }

    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface Listener {
        void deckNameModified(String newName);
        void tagAddedToDeck(Deck deck, String tagName, String color);
        void deckColorModified(Deck deck, Color color);
        void newCard();
        void removeCard(Card selectedCard);
        void cardPreviewClicked(Card card) throws IOException;
    }
}
