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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.scene.web.WebView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;

import java.io.File;
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

    @FXML
    private Button imageUploader;

    private Node QCMCardEditor;

    private Node flashCardEditor;

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
        if (cardEditor.getChildren().size() == 3) {
            flashCardEditor = cardEditor.getChildren().get(1);
            QCMCardEditor = cardEditor.getChildren().get(2);
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


    /* ====================================================================== */
    /*                                 Tags                                   */
    /* ====================================================================== */

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

    public void showTags() {
        tagsBox.getChildren().clear();
        deck.getTags()
                .forEach(tag -> addTagToView(tag.getName(), tag.getColor()));
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
        hideCardEditor();
        showCardTypeButtons();
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

        hideCardEditor();
        //TODO check type of card to show the good card editor
        showFlashCardEditor();
        selectedCard = deck.getCards().get(cardIndex);
        listener.cardPreviewClicked(selectedCard);
    }

    @FXML
    private void handleUploadImageClicked() {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(anchor.getScene().getWindow());
        listener.uploadImage(file.toURI().toString());
    }

    @FXML
    private void handleFlashCardEdit(){
        listener.newCard(); //TODO newCard avec para flashCardEditor
        hideCardEditor();
        showFlashCardEditor();
    }

    @FXML
    private void handleQCMCardEdit() {
        listener.newCard(); //TODO newCard avec para QCMCardEditor
        hideCardEditor();
        showQCMCardEditor();
    }


    private void showCardTypeButtons() {
        cardTypeButtons.setVisible(true);
        cardTypeButtons.toFront();
    }

    public void showFlashCardEditor() {
        flashCardEditor.setVisible(true);
        flashCardEditor.toFront();
    }

    public void showQCMCardEditor() {
        QCMCardEditor.setVisible(true);
        QCMCardEditor.toFront();
    }

    public void hideCardEditor(){
        flashCardEditor.setVisible(false);
        QCMCardEditor.setVisible(false);
        cardTypeButtons.setVisible(false);
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
    public void handleColorButtonClicked() {
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
        void cardPreviewClicked(Card card);
        void uploadImage(String filePath);
    }
}
