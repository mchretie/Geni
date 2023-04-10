package ulb.infof307.g01.gui.view.editdeck;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.scene.web.WebView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.FlashCard;
import ulb.infof307.g01.model.MCQCard;

import java.io.File;
import java.util.List;

public class EditDeckViewController {
    public GridPane choicesGrid;
    public BorderPane addChoiceField;
    public FontIcon addChoiceIcon;

    /* ====================================================================== */
    /*                              FXML Attributes                           */
    /* ====================================================================== */

    @FXML
    private VBox leftVbox;

    @FXML
    private VBox rightVbox;

    @FXML
    private HBox cardTypeBox;

    @FXML
    private HBox hbox;

    @FXML
    private BorderPane frontCard;

    @FXML
    private StackPane backCard;

    @FXML
    private HBox tagsBox;

    @FXML
    private ListView<String> cardsContainer;

    @FXML
    private TextField backCardText;

    @FXML
    private TextField deckNameText;

    @FXML
    private TextField tagsInput;

    @FXML
    private WebView frontCardWebView;

    @FXML
    private Button imageUploader;

    @FXML
    private FontIcon removeCardIcon;

    @FXML
    private FontIcon addCardIcon;

    @FXML
    public FontIcon frontCardEditIcon;

    @FXML
    private ColorPicker colorPicker;


    /* ====================================================================== */
    /*                           Card Editor Grid                             */
    /* ====================================================================== */

    private int currentCol = 0;
    private int currentRow = 0;


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

    /**
     * Initializes the controller class. Cannot happen during construction as
     * the parents size is needed to set the width of the left and right components.
     */
    public void init() {
        backCardText.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) handleBackEdit();
        });

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

        for (Card card : deck) {
            Document doc = Jsoup.parse(card.getFront());
            Element body = doc.body();
            list.add(body.text());
        }

        cardsContainer.refresh();
    }

    private void loadCardEditor(Card card) {
        frontCardWebView.getEngine().loadContent(card.getFront());
        frontCard.setVisible(true);
        if (card instanceof FlashCard flashCard) {
            backCardText.setText(flashCard.getBack());
            backCard.setVisible(true);
            choicesGrid.setVisible(false);
        } else if (card instanceof MCQCard mcqCard) {
            choicesGrid.getChildren().clear();
            currentCol = 0;
            currentRow = 0;
            int correctAnswerIndex = mcqCard.getCorrectAnswer();
            for (int i = 0; i < mcqCard.getCardMax(); i++) {
                if (i >= mcqCard.getAnswers().size()) {
                    addChoiceFieldButton();
                    break;
                }
                String choice = mcqCard.getAnswers().get(i);
                addChoiceField(choice, i, correctAnswerIndex == i);
                currentCol++;
                if (currentCol == 2) {
                    currentCol = 0;
                    currentRow++;
                }
            }
            backCard.setVisible(false);
            choicesGrid.setVisible(true);
        }
    }

    private void addChoiceFieldButton() {
        Button addChoiceButton = new Button();
        addChoiceButton.setGraphic(new FontIcon("mdi2p-plus"));
        addChoiceButton.setOnAction(event -> {
            listener.mcqAnswerAdded((MCQCard) selectedCard);
            loadSelectedCardEditor();
        });
        choicesGrid.add(addChoiceButton, currentCol, currentRow);
    }

    private void addChoiceField(String choice, int index, boolean correctAnswer) {
        TextField textField = getChoiceFieldTextField(choice, index);

        Button setCorrectAnswerButton = getChoiceFieldCorrectAnswerButton(correctAnswer, index);

        Button removeChoiceButton = getChoiceFieldRemoveButton(index);

        HBox hBox = new HBox();
        HBox.setHgrow(hBox, Priority.ALWAYS);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(textField, setCorrectAnswerButton, removeChoiceButton);
        hBox.setSpacing(2);
        choicesGrid.add(hBox, currentCol, currentRow);
    }

    private TextField getChoiceFieldTextField(String choice, int index) {
        TextField textField = new TextField(choice);
        textField.setPromptText("Réponse");
        textField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(textField, Priority.ALWAYS);
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) listener.mcqAnswerEdit((MCQCard) selectedCard, textField.getText(), index);
        });
        return textField;
    }

    private Button getChoiceFieldCorrectAnswerButton(boolean correctAnswer, int index) {
        Button setCorrectAnswerButton = new Button();
        FontIcon checkIcon = new FontIcon("mdi2c-check");
        if (correctAnswer) {
            checkIcon.setIconColor(Color.WHITE);
            setCorrectAnswerButton.setStyle("-fx-background-color: green;");
        }
        setCorrectAnswerButton.setGraphic(checkIcon);

        setCorrectAnswerButton.setOnAction(event -> {
            listener.mcqCorrectAnswerEdit((MCQCard) selectedCard, index);
            loadSelectedCardEditor();
        });
        return setCorrectAnswerButton;
    }

    private Button getChoiceFieldRemoveButton(int index) {
        Button removeChoiceButton = new Button();
        removeChoiceButton.setStyle("-fx-background-color: red;");
        FontIcon trashIcon = new FontIcon("mdi2t-trash-can-outline");
        trashIcon.setIconColor(Color.WHITE);
        removeChoiceButton.setGraphic(trashIcon);
        if (((MCQCard) selectedCard).isCardMin())
            removeChoiceButton.setDisable(true);

        removeChoiceButton.setOnAction(event -> {
            listener.mcqAnswerRemove((MCQCard) selectedCard, index);
            loadSelectedCardEditor();
        });
        return removeChoiceButton;
    }

    public void loadSelectedCardEditor() {
        loadCardEditor(selectedCard);
    }

    public void hideSelectedCardEditor() {
        frontCard.setVisible(false);
        backCard.setVisible(false);
        choicesGrid.setVisible(false);
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
        setCardTypeButtonVisibility(true);
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

    @FXML
    private void handleFrontEditClicked() {
        listener.editCardClicked(selectedCard);
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

    @FXML
    private void handleFrontCardEditHover() {
        frontCardEditIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    private void handleFrontCardEditHoverExit() {
        frontCardEditIcon.setIconColor(Color.web("#000000"));
    }

    @FXML
    private void handleAddChoiceHover() {
        addChoiceIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    private void handleAddChoiceHoverExit() {
        addChoiceIcon.setIconColor(Color.web("#000000"));
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
        listener.backOfFlashCardModified((FlashCard) selectedCard, backCardText.getText());
    }

    @FXML
    private void handleTextFieldKeyPressed(KeyEvent keyEvent) {
        if (!keyEvent.getCode().equals(KeyCode.ENTER))
            return;

        hbox.requestFocus();
    }

    @FXML
    private void handleMCQCardSelected() {
        backCard.setVisible(false);
        listener.newMCQCard();
        cardTypeSelected();

        choicesGrid.setVisible(true);
    }

    @FXML
    private void handleFlashCardSelected() {
        choicesGrid.setVisible(false);
        listener.newFlashCard();
        cardTypeSelected();
        backCard.setVisible(true);
    }

    private void cardTypeSelected() {
        setCardTypeButtonVisibility(false);
        frontCard.setVisible(true);
    }

    private void setCardTypeButtonVisibility(boolean visibility) {
        cardTypeBox.setVisible(visibility);
    }


    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ==================================================================== */

    public interface Listener {
        void deckNameModified(String newName);

        void tagAddedToDeck(Deck deck, String tagName, String color);

        void backOfFlashCardModified(FlashCard selectedCard, String newBack);

        void mcqAnswerEdit(MCQCard selectedCard, String text, int index);

        void mcqCorrectAnswerEdit(MCQCard selectedCard, int i);
        void mcqAnswerRemove(MCQCard selectedCard, int index);

        void mcqAnswerAdded(MCQCard selectedCard);

        void deckColorModified(Deck deck, Color color);

        void newFlashCard();

        void newMCQCard();


        void removeCard(Card selectedCard);

        void cardPreviewClicked(Card card);

        void editCardClicked(Card selectedCard);

        void uploadImage(String filePath);


    }
}
