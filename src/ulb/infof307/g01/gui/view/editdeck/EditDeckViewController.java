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
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.gui.util.GridPosIterator;
import ulb.infof307.g01.gui.util.Pos2D;
import ulb.infof307.g01.model.card.*;
import ulb.infof307.g01.model.deck.Deck;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class EditDeckViewController {

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

    @FXML
    private HBox timerChangerComponent;

    @FXML
    private TextField timerValue;


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
            listener.setSelectedCardIndex(cardIndex);
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
            loadMCQCardEditor(mcqCard);

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
        timerChangerComponent.setVisible(false);
        answerOfInputCard.setVisible(false);
        choicesGrid.setVisible(false);

    }

    private void loadInputCardEditor(InputCard inputCard) {
        answerOfInputCard.setText(inputCard.getAnswer());
        System.out.println(inputCard.getCountdownTime()); // TODO : delete this
        timerValue.setText(String.valueOf(inputCard.getCountdownTime()));
        answerOfInputCard.setVisible(true);
        timerChangerComponent.setVisible(true);
        choicesGrid.setVisible(false);
        backCard.setVisible(false);
    }


    /**
     * Loads the card editor with the given MCQ card.
     *
     * @param mcqCard the MCQ card to load
     */
    private void loadMCQCardEditor(MCQCard mcqCard) {
        choicesGrid.getChildren().clear();

        int correctChoiceIndex = mcqCard.getCorrectChoiceIndex();
        Iterator<Pos2D> positions = new GridPosIterator(2, 2);

        for (int i = 0; i < mcqCard.MAX_CHOICES; i++) {
            Pos2D nextPos = positions.next();
            currentCol = nextPos.col;
            currentRow = nextPos.row;

            if (i >= mcqCard.getChoicesCount()) {
                addChoiceFieldButton();
                break;
            }

            addChoiceField(mcqCard.getChoice(i), i, correctChoiceIndex == i);
        }

        backCard.setVisible(false);
        timerChangerComponent.setVisible(true);
        answerOfInputCard.setVisible(false);
        choicesGrid.setVisible(true);
    }


    /**
     * Adds a button that allows the user to add a choice field to the grid
     * when clicked.
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
     * @param choice          the text of the choice
     * @param index           the index of the choice in the grid
     * @param isCorrectChoice true if the choice is the correct choice
     */
    private void addChoiceField(String choice, int index, boolean isCorrectChoice) {
        TextField textField = getChoiceFieldTextField(choice, index);

        textField.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER -> {
                    if (choiceFieldEmpty(textField, index))
                        return;

                    focusNextNode(index, true, false);
                }

                case TAB -> {
                    choiceFieldEmpty(textField, index);
                    focusNextNode(index, false, true);
                }
            }
        });

        Button correctChoiceSelectionButton
                = createCorrectChoiceSelectionButton(isCorrectChoice, index);

        Button removeChoiceButton = createRemoveChoiceButton(index);

        HBox hBox = new HBox(2,
                textField,
                correctChoiceSelectionButton,
                removeChoiceButton);

        HBox.setHgrow(hBox, Priority.ALWAYS);
        hBox.setAlignment(Pos.CENTER);

        choicesGrid.add(hBox, currentCol, currentRow);
    }


    /**
     * Checks if the choice field is empty and removes it if it is.
     * Returns true if the choice field was removed.
     *
     * @param textField the text field of the choice field
     * @param index     the index of the choice field
     * @return true if the choice field was removed or if the number of choice
     * fields is less than 3 and the field is empty.
     */
    private boolean choiceFieldEmpty(TextField textField, int index) {
        if (textField.getText().isEmpty()
                && ((MCQCard) selectedCard).getChoicesCount() < 3)

            return true;

        if (!textField.getText().isEmpty())
            return false;

        listener.choiceRemoved((MCQCard) selectedCard, index);

        loadSelectedCardEditor();
        focusPreviousChoiceField(index);
        return true;
    }

    /**
     * Focuses the previous choice field of the given index.
     *
     * @param index the index of the choice field
     */
    private void focusPreviousChoiceField(int index) {
        HBox hBox = (HBox) choicesGrid.getChildren().get(index - 1);
        TextField textField = (TextField) hBox.getChildren().get(0);
        textField.requestFocus();
        textField.selectAll();
    }

    /**
     * Focuses the next choice field of the given index.
     *
     * @param nextIndex the index of the choice field
     */
    private void focusNextChoiceField(int nextIndex) {
        HBox hBox = (HBox) choicesGrid.getChildren().get(nextIndex);
        TextField textField = (TextField) hBox.getChildren().get(0);
        textField.requestFocus();
        textField.selectAll();
    }

    /**
     * Focuses the next eligible node after the choice field at the given index.
     *
     * @param index          the index of the choice field
     * @param createNextNode true if the next node should be created if it doesn't exist
     * @param cycle          true if the focus should cycle back to the first choice field
     */
    private void focusNextNode(int index, boolean createNextNode, boolean cycle) {
        int nextIndex = index + 1;
        if (nextIndex < ((MCQCard) selectedCard).getChoicesCount()) {
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

    /**
     * Gets the text field for a choice field
     *
     * @param choice the text of the choice
     * @param index  the index of the choice field
     * @return the text field
     */
    private TextField getChoiceFieldTextField(String choice, int index) {
        TextField textField = new TextField(choice);
        textField.setPromptText("Réponse");
        textField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(textField, Priority.ALWAYS);

        // When the text field loses focus, the choice is updated
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && index < ((MCQCard) selectedCard).getChoicesCount()) {
                listener.choiceModified((MCQCard) selectedCard, textField.getText(), index);
                loadSelectedCardEditor();
            }
        });

        return textField;
    }

    /**
     * Gets the button to set the correct choice for a choice field
     *
     * @param isCorrectChoice true if the choice is the correct one
     * @param index           the index of the choice field
     * @return the button
     */
    private Button createCorrectChoiceSelectionButton(boolean isCorrectChoice, int index) {
        Button selectCorrectChoiceButton = new Button();
        FontIcon checkIcon = new FontIcon("mdi2c-check");

        if (isCorrectChoice) {
            checkIcon.setIconColor(Color.WHITE);
            selectCorrectChoiceButton.setStyle("-fx-background-color: green;");
        }

        selectCorrectChoiceButton.setGraphic(checkIcon);

        selectCorrectChoiceButton.setOnAction(event -> {
            listener.correctChoiceChanged((MCQCard) selectedCard, index);
            loadSelectedCardEditor();
        });

        return selectCorrectChoiceButton;
    }


    /**
     * Gets the remove button for a choice field
     *
     * @param index the index of the choice field to remove
     * @return the button
     */
    private Button createRemoveChoiceButton(int index) {
        Button removeChoiceButton = new Button();
        removeChoiceButton.setStyle("-fx-background-color: red;");
        FontIcon trashIcon = new FontIcon("mdi2t-trash-can-outline");
        trashIcon.setIconColor(Color.WHITE);
        removeChoiceButton.setGraphic(trashIcon);

        if (!((MCQCard) selectedCard).canRemoveChoice())
            removeChoiceButton.setDisable(true);

        removeChoiceButton.setOnAction(event -> {
            listener.choiceRemoved((MCQCard) selectedCard, index);
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
        timerChangerComponent.setVisible(false);
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
    public void handleColorButtonClicked() {
        listener.deckColorModified(deck, colorPicker.getValue());
    }


    @FXML
    private void handleUploadImageClicked() {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(mainHbox.getScene().getWindow());
        if (file != null) {
            listener.deckImageModified(deck, file, "/backgrounds/" + deck.getId().toString() + ".jpg");
        }
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
    private void handleTimerValueEdit(KeyEvent keyEvent) {
        timerValue.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("[0-9]")) {
                event.consume();
                System.out.println("not a number"); // TODO : delete
            }
        });

        System.out.println("key pressed : " + keyEvent.getText()); // TODO : delete

        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            mainHbox.requestFocus();
        }
    }

    @FXML
    private void handleTimerValueSave() {
        if (!timerValue.getText().isEmpty()) {
            System.out.println("timer value: " + Integer.parseInt(timerValue.getText())); // TODO : delete
            listener.timerValueChanged((TimedCard) selectedCard, Integer.parseInt(timerValue.getText()));
        }
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
        timerChangerComponent.setVisible(true);
    }

    @FXML
    private void handleInputCardSelected() {
        choicesGrid.setVisible(false);
        backCard.setVisible(false);
        listener.newInputCard();
        cardTypeSelected();
        answerOfInputCard.setVisible(true);
        timerChangerComponent.setVisible(true);
    }

    @FXML
    private void handleFlashCardSelected() {
        choicesGrid.setVisible(false);
        answerOfInputCard.setVisible(false);
        listener.newFlashCard();
        cardTypeSelected();
        backCard.setVisible(true);
        timerChangerComponent.setVisible(false);
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
        /* Deck */
        void deckNameModified(String newName);

        void tagAddedToDeck(Deck deck, String tagName, String color);

        void deckColorModified(Deck deck, Color color);

        void deckImageModified(Deck deck, File image, String filename);

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

        void timerValueChanged(TimedCard selectedCard, int value);

        void setSelectedCardIndex(int cardIndex);
    }
}
