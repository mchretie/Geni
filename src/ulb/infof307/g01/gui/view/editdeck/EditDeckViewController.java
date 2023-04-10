package ulb.infof307.g01.gui.view.editdeck;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;

import java.io.File;
import java.util.List;

public class EditDeckViewController {

    /* ====================================================================== */
    /*                            FXML Attributes                             */
    /* ====================================================================== */

    @FXML
    private HBox hbox;

    @FXML
    private VBox leftVbox;

    @FXML
    private VBox rightVbox;

    @FXML
    private HBox cardTypeBox;
    @FXML
    private StackPane cardEditor;
    @FXML
    private AnchorPane anchor;
    @FXML
    private ListView<String> cardsContainer;
    @FXML
    private HBox tagsBox;

    @FXML
    private TextField deckNameText;
    @FXML
    private TextField tagsInput;

    @FXML
    private Button imageUploader;

    @FXML
    private FontIcon removeCardIcon;
    @FXML
    private FontIcon addCardIcon;

    @FXML
    private ColorPicker colorPicker;

    /* ====================================================================== */
    /*                              Model Attributes                          */
    /* ====================================================================== */
    private Node QCMCardEditor;

    private Node flashCardEditor;

    private Deck deck;
    private Card selectedCard;


    /* ====================================================================== */
    /*                                Listener                                */
    /* ====================================================================== */
    private Listener listener;


    /* ====================================================================== */
    /*                              Initializer                               */
    /* ====================================================================== */

    /**
     * Initializes the controller class. Cannot happen during construction as
     *  the parents size is needed to set the width of the left and right components.
     */
    public void init() {
        deckNameText.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) handleUpdateDeckName();
        });

        double sceneWidth = leftVbox.getParent().getLayoutBounds().getWidth();
        leftVbox.setPrefWidth(sceneWidth * 0.4);
        rightVbox.setPrefWidth(sceneWidth * 0.6);
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

    public void setFlashCardEditor(Node node){
        cardEditor.getChildren().add(node);
        flashCardEditor = node;
    }

    public void setQCMCardEditor(Node node){
        cardEditor.getChildren().add(node);
        QCMCardEditor = node;
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
        selectedCard = deck.getCards().get(cardIndex);
        listener.cardPreviewClicked(selectedCard);
    }

    @FXML
    private void handleNormalSelected(){
        listener.newCard("flashCard");
        hideCardEditor();
        showFlashCardEditor();
    }

    @FXML
    private void handleQCMSelected() {
        listener.newCard("MCQCard");
        hideCardEditor();
        showQCMCardEditor();
    }


    private void showCardTypeButtons() {
        cardTypeBox.setVisible(true);
        cardTypeBox.toFront();
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
        cardTypeBox.setVisible(false);
    }

    @FXML
    private void handleUploadImageClicked() {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(hbox.getScene().getWindow());
        listener.uploadImage(file.toURI().toString());
    }

    @FXML
    public void handleColorButtonClicked() {
        listener.deckColorModified(deck, colorPicker.getValue());
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
    private void handleTextFieldKeyPressed(KeyEvent keyEvent) {
        if (!keyEvent.getCode().equals(KeyCode.ENTER))
            return;

        anchor.requestFocus();
    }


    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface Listener {
        void deckNameModified(String newName);
        void tagAddedToDeck(Deck deck, String tagName, String color);
        void deckColorModified(Deck deck, Color color);
        void newCard(String type);
        void removeCard(Card selectedCard);
        void cardPreviewClicked(Card card);
        void uploadImage(String filePath);
    }
}
