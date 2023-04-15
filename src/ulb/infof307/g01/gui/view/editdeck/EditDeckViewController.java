package ulb.infof307.g01.gui.view.editdeck;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EditDeckViewController implements Initializable {

    /* ====================================================================== */
    /*                         FXML: Panes & Containers                       */
    /* ====================================================================== */

    @FXML
    private StackPane frontCard;

    @FXML
    private StackPane backCard;

    @FXML
    private AnchorPane anchor;

    @FXML
    private HBox tagsBox;

    @FXML
    private ListView<String> cardsContainer;


    /* ====================================================================== */
    /*                            FXML: Text Fields                           */
    /* ====================================================================== */

    @FXML
    private TextField backCardText;

    @FXML
    private TextField deckNameText;

    @FXML
    private TextField tagsInput;

    @FXML
    private WebView frontCardWebView;


    /* ====================================================================== */
    /*                              FXML: Buttons                             */
    /* ====================================================================== */

    @FXML
    private Button imageUploader;


    /* ====================================================================== */
    /*                               FXML: Icons                              */
    /* ====================================================================== */

    @FXML
    private FontIcon removeCardIcon;

    @FXML
    private FontIcon addCardIcon;

    @FXML
    public FontIcon frontCardEditIcon;


    /* ====================================================================== */
    /*                           FXML: Color picker                           */
    /* ====================================================================== */

    @FXML
    private ColorPicker colorPicker;


    /* ====================================================================== */
    /*                              Model Attributes                          */
    /* ====================================================================== */

    private Deck deck;

    private Card selectedCard;


    /* ====================================================================== */
    /*                                Listener                                */
    /* ====================================================================== */

    private Listener listener;

    /* ====================================================================== */
    /*                              Initializer                               */
    /* ====================================================================== */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backCardText.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) handleBackEdit();
        });

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


    /* ====================================================================== */
    /*                              Card Editor                               */
    /* ====================================================================== */

    public void showCards() {
        ObservableList<String> list = FXCollections.observableArrayList();
        cardsContainer.setItems(list);

        cardsContainer.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        for (Card card : deck){
            Document doc = Jsoup.parse(card.getFront());
            Element body = doc.body();
            list.add(body.text());
        }

        cardsContainer.refresh();
    }

    private void loadCardEditor(Card card) {
        frontCardWebView.getEngine().loadContent(card.getFront());
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


    /* ====================================================================== */
    /*                                 Tags                                   */
    /* ====================================================================== */

    public void setTags(List<Node> tagViews) {
        tagsBox.getChildren().clear();
        tagsBox.getChildren().addAll(tagViews);
    }

    @FXML
    private void handleTagAdded(KeyEvent event) {
        if (event.getCode() != KeyCode.ENTER
                || tagsInput.getText().trim().isEmpty())

            return;

        String tagText = tagsInput.getText().trim();
        tagsInput.clear();

        // TODO: Ability to choose a color
        Color color
                = Color.color(Math.random(), Math.random(), Math.random());

        String colorString
                = color.toString().replace("0x", "#");

        listener.tagAddedToDeck(deck, tagText, colorString);
    }


    /* ====================================================================== */
    /*                             Click handlers                             */
    /* ====================================================================== */

    @FXML
    private void handleAddCardClicked() {
        listener.newCard();
    }

    @FXML
    private void handleRemoveCardClicked() {
        listener.removeCard(selectedCard);
    }

    @FXML
    private void handleCardPreviewClicked() {
        int cardIndex = cardsContainer.getSelectionModel().getSelectedIndex();

        if (cardIndex < 0)
            return;

        selectedCard = deck.getCards().get(cardIndex);
        listener.cardPreviewClicked(selectedCard);
    }


    /* ====================================================================== */
    /*                             Hover handlers                             */
    /* ====================================================================== */

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

    @FXML
    private void handleColorPickerHover() {
        colorPicker.setStyle("-fx-background-color: #aad0b3");
    }

    @FXML
    private void handleColorPickerExit() {
        colorPicker.setStyle("-fx-background-color: #5ab970");
    }

    @FXML
    private void handleUploadImageHover() {
        imageUploader.setStyle("-fx-background-color: #aad0b3");
    }

    @FXML
    private void handleUploadImageExit() {
        imageUploader.setStyle("-fx-background-color: #5ab970");
    }


    /* ====================================================================== */
    /*                            Modified text                               */
    /* ====================================================================== */

    @FXML
    private void handleUpdateDeckName() {
        listener.deckNameModified(deckNameText.getText());
    }

    @FXML
    private void handleBackEdit() {
        listener.backOfCardModified(selectedCard, backCardText.getText());
    }

    @FXML
    private void handleTextFieldKeyPressed(KeyEvent keyEvent) {
        if (!keyEvent.getCode().equals(KeyCode.ENTER))
            return;

        anchor.requestFocus();
    }

    @FXML
    public void handleColorButtonClicked() {
        listener.deckColorModified(deck, colorPicker.getValue());
    }

    @FXML
    private void handleUploadImageClicked() throws IOException {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(anchor.getScene().getWindow());
        listener.deckImageModified(deck, file, "/img/" + deck.getId().toString() + ".jpg");
    }

    @FXML
    private void handleFrontCardEditHover() {
        frontCardEditIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    private void handleFrontCardEditHoverExit() {
        frontCardEditIcon.setIconColor(Color.web("#000000"));
    }

    @FXML
    private void handleFrontEditClicked() {
        listener.editCardClicked(selectedCard);
    }


    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface Listener {
        void deckNameModified(String newName);
        void tagAddedToDeck(Deck deck, String tagName, String color);
        void backOfCardModified(Card card, String newBack);
        void deckImageModified(Deck deck, File image, String filename);
        void deckColorModified(Deck deck, Color color);
        void newCard();
        void removeCard(Card selectedCard);
        void cardPreviewClicked(Card card);
        void editCardClicked(Card selectedCard);
    }
}
