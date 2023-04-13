package ulb.infof307.g01.gui.view.editdeck;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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
import ulb.infof307.g01.gui.view.editdeck.subcomponents.ChoiceView;
import ulb.infof307.g01.model.*;

import java.io.File;
import java.util.List;

public class EditDeckViewController implements ChoiceView.Listener
{

    /* ====================================================================== */
    /*                              FXML Attributes                           */
    /* ====================================================================== */

    @FXML
    private GridPane choicesGrid;

    @FXML
    private VBox leftVbox;

    @FXML
    private VBox rightVbox;

    @FXML
    private HBox cardTypeBox;

    @FXML
    private HBox mainHbox;

    @FXML
    private BorderPane frontCard;

    @FXML
    private BorderPane backCard;

    @FXML
    private HBox tagsBox;

    @FXML
    private ListView<String> cardsContainer;

    @FXML
    private WebView backCardWebView;

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
    private FontIcon backCardEditIcon;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private TextField answerOfInputCard;


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

    /**
     * Loads the card editor with the given card.
     *
     * @param card the card to load
     */
    private void loadCardEditor(Card card) {
        frontCardWebView.getEngine().loadContent(card.getFront());
        frontCard.setVisible(true);

        if (card instanceof FlashCard flashCard)
            loadFlashCardEditor(flashCard);

        else if (card instanceof MCQCard mcqCard)
            loadQCMCardEditor(mcqCard);

        else if (card instanceof InputCard inputCard)
            loadInputCardEditor(inputCard);
    }

    /**
     * Loads the card editor with the given card.
     *
     * @param flashCard the card to load
     */
    private void loadFlashCardEditor(FlashCard flashCard) {
        backCardWebView.getEngine().loadContent(flashCard.getBack());
        backCard.setVisible(true);
        answerOfInputCard.setVisible(false);
        choicesGrid.setVisible(false);
    }

    private void loadInputCardEditor(InputCard inputCard) {
        answerOfInputCard.setText(inputCard.getAnswer());
        answerOfInputCard.setVisible(true);
        choicesGrid.setVisible(false);
        backCard.setVisible(false);
    }


    /**
     * Loads the card editor with the given MCQ card.
     *
     * @param mcqCard the MCQ card to load
     */
    private void loadQCMCardEditor(MCQCard mcqCard) {
        choicesGrid.getChildren().clear();

        currentCol = 0;
        currentRow = 0;

        int correctChoiceIndex = mcqCard.getCorrectChoiceIndex();
        for (int i = 0; i < mcqCard.getChoiceMax(); i++) {
            if (i >= mcqCard.getNbOfChoices()) {
                addChoiceFieldButton();
                break;
            }

            addChoiceField(mcqCard.getChoice(i), i, correctChoiceIndex == i);
            nextPosition();
        }

        backCard.setVisible(false);
        answerOfInputCard.setVisible(false);
        choicesGrid.setVisible(true);
    }

    private void nextPosition() {
        currentCol++;
        if (currentCol == 2) {
            currentCol = 0;
            currentRow++;
        }
    }


    /**
     * Adds a button that allows the user to add a choice field to the grid
     *  when clicked.
     */
    private void addChoiceFieldButton() {
        Button addChoiceButton = new Button();
        addChoiceButton.setGraphic(new FontIcon("mdi2p-plus"));

        addChoiceButton.setOnAction(event -> {
            listener.choiceAdded((MCQCard) selectedCard);
            loadSelectedCardEditor();
        });

        choicesGrid.add(addChoiceButton, currentCol, currentRow);
    }


    /**
     * Adds a choice field to the grid
     *
     * @param choice        the text of the choice
     * @param index         the index of the choice in the grid
     * @param isCorrectChoice true if the choice is the correct choice
     */
    private void addChoiceField(String choice, int index, boolean isCorrectChoice) {
        ChoiceView choiceView
                = new ChoiceView(choice, index, isCorrectChoice, (MCQCard) selectedCard, this);

        choicesGrid.add(choiceView, currentCol, currentRow);
    }

    private void removeChoiceAtIndex(int index) {
        listener.choiceRemoved((MCQCard) selectedCard, index);
        loadSelectedCardEditor();
        focusPreviousChoiceField(index);
    }

    /**
     * Focuses the previous choice field of the given index.
     *
     * @param index the index of the choice field
     */
    private void focusPreviousChoiceField(int index) {
        ChoiceView choiceView
                = (ChoiceView) choicesGrid.getChildren().get(index - 1);

        choiceView.requestFocus();
    }

    /**
     * Focuses the next choice field of the given index.
     *
     * @param nextIndex the index of the choice field
     */
    private void focusNextChoiceField(int nextIndex) {
        ChoiceView choiceView = (ChoiceView) choicesGrid.getChildren().get(nextIndex);
        choiceView.requestFocus();
    }

    /**
     * Focuses the next eligible node after the choice field at the given index.
     *
     * @param index the index of the choice field
     * @param createNextNode true if the next node should be created if it doesn't exist
     * @param cycle true if the focus should cycle back to the first choice field
     */
    private void focusNextNode(int index, boolean createNextNode, boolean cycle) {
        int nextIndex = index + 1;
        if (nextIndex < ((MCQCard) selectedCard).getNbOfChoices()) {
            focusNextChoiceField(nextIndex);

        } else if (nextIndex < 4 && createNextNode) {
            listener.choiceAdded((MCQCard) selectedCard);
            loadSelectedCardEditor();
            focusNextChoiceField(nextIndex);

        } else if (cycle) {
            focusNextChoiceField(0);

        } else {
            mainHbox.requestFocus();
        }
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
        File file = fileChooser.showOpenDialog(mainHbox.getScene().getWindow());
        listener.uploadImage(file.toURI().toString());
    }

    @FXML
    public void handleColorButtonClicked() {
        listener.deckColorModified(deck, colorPicker.getValue());
    }

    @FXML
    private void handleFrontEditClicked() {
        listener.editFrontOfCardClicked(selectedCard);
    }

    @FXML
    private void handleBackEditClicked() {
        listener.editBackOfCardClicked((FlashCard) selectedCard);
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
    private void handleBackCardEditHoverExit() {
        backCardEditIcon.setIconColor(Color.web("#000000"));
    }

    @FXML
    private void handleBackCardEditHover() {
        backCardEditIcon.setIconColor(Color.web("#FFFFFF"));
    }


    /* ====================================================================== */
    /*                            Modified text                               */
    /* ====================================================================== */

    @FXML
    private void handleUpdateDeckName() {
        listener.deckNameModified(deckNameText.getText());
    }

    @FXML
    private void handleAnswerOfInputEdit() {
        listener.inputAnswerModified((InputCard) selectedCard, answerOfInputCard.getText());
    }

    @FXML
    private void handleInputTextFieldKeyPressed(KeyEvent keyEvent) {
        answerOfInputCard.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (" ".equals(event.getCharacter()))
                event.consume(); // Consume the space character
        });
        if (!keyEvent.getCode().equals(KeyCode.ENTER))
            return;
        mainHbox.requestFocus();
    }

    @FXML
    private void handleTextFieldKeyPressed(KeyEvent keyEvent) {
        if (!keyEvent.getCode().equals(KeyCode.ENTER))
            return;

        mainHbox.requestFocus();
    }

    @FXML
    private void handleMCQCardSelected() {
        backCard.setVisible(false);
        answerOfInputCard.setVisible(false);
        listener.newMCQCard();
        cardTypeSelected();

        choicesGrid.setVisible(true);
    }

    @FXML
    private void handleInputCardSelected() {
        choicesGrid.setVisible(false);
        backCard.setVisible(false);
        listener.newInputCard();
        cardTypeSelected();
        answerOfInputCard.setVisible(true);
    }

    @FXML
    private void handleFlashCardSelected() {
        choicesGrid.setVisible(false);
        answerOfInputCard.setVisible(false);
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
    /*                       Sub-component listener methods                   */
    /* ====================================================================== */

    @Override
    public void choiceModified(MCQCard selectedCard, String text, int index) {
        listener.choiceModified(selectedCard, text, index);
        loadSelectedCardEditor();
    }

    @Override
    public void correctChoiceChanged(MCQCard selectedCard, int index) {
        listener.correctChoiceChanged(selectedCard, index);
        loadSelectedCardEditor();
    }

    @Override
    public void choiceRemoved(MCQCard selectedCard, int index) {
        removeChoiceAtIndex(index);
        loadSelectedCardEditor();
    }

    @Override
    public void focusNextNode(int index, KeyCode keyCode) {
        focusNextNode(index, keyCode == KeyCode.ENTER, keyCode == KeyCode.TAB);
    }


    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ==================================================================== */

    public interface Listener {
        /* Deck */
        void deckNameModified(String newName);
        void tagAddedToDeck(Deck deck, String tagName, String color);
        void deckColorModified(Deck deck, Color color);
        void uploadImage(String filePath);
        void removeCard(Card selectedCard);

        /* Card */
        void cardPreviewClicked(Card card);
        void editFrontOfCardClicked(Card selectedCard);

        /* MCQ Card */
        void newMCQCard();
        void choiceModified(MCQCard selectedCard, String text, int index);
        void correctChoiceChanged(MCQCard selectedCard, int i);
        void choiceRemoved(MCQCard selectedCard, int index);
        void choiceAdded(MCQCard selectedCard);

        /* Flash Card */
        void newFlashCard();
        void editBackOfCardClicked(FlashCard selectedCard);

        /* Input Card */
        void newInputCard();
        void inputAnswerModified(InputCard selectedCard, String answer);
    }
}
