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
    private ColorPicker colorPickerBackground;

    @FXML
    private ColorPicker colorPickerTitle;

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
        colorPickerBackground.setValue(Color.web(deck.getColor()));
        colorPickerTitle.setValue(Color.web(deck.getColorName()));

        deckNameText.setText(deck.getName());

        String color = hexToRgb(deck.getColorName());
        deckNameText.setStyle("-fx-text-inner-color: " + color + ";");
    }

    private String hexToRgb(String hex) {
        Color color = Color.web(hex);
        int r = (int) (color.getRed() * 255);
        int g = (int) (color.getGreen() * 255);
        int b = (int) (color.getBlue() * 255);
        return String.format("#%02x%02x%02x", r, g, b);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setSelectedCard(int cardIndex) {
        if (cardIndex >= 0) {
            cardsContainer.getSelectionModel().select(cardIndex);
        }
    }


    /* ====================================================================== */
    /*                              Card Editor                               */
    /* ====================================================================== */

    public void showCardsFromDeck(Deck deck) {
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

    private void loadFront(Card card) {
        frontCardWebView.getEngine().loadContent(card.getFront());
        frontCard.setVisible(true);
    }

    /**
     * Loads the card editor with the given card.
     *
     * @param flashCard the card to load
     */
    public void loadFlashCardEditor(FlashCard flashCard) {
        loadFront(flashCard);
        backCardWebView.getEngine().loadContent(flashCard.getBack());
        backCard.setVisible(true);
        timerChangerComponent.setVisible(false);
        answerOfInputCard.setVisible(false);
        choicesGrid.setVisible(false);
    }

    public void loadInputCardEditor(InputCard inputCard) {
        loadFront(inputCard);
        answerOfInputCard.setText(inputCard.getAnswer());
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
    public void loadMCQCardEditor(MCQCard mcqCard) {
        loadFront(mcqCard);
        choicesGrid.getChildren().clear();
        timerValue.setText(String.valueOf(mcqCard.getCountdownTime()));

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

        addChoiceButton.setOnAction(e -> listener.mcqCardChoiceAdded());

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
                case ENTER -> listener.mcqChoiceModified(textField.getText(), index);

                case TAB -> focusNextNode(index);
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
     * @param index the index of the choice field
     */
    private void focusNextNode(int index) {
        int nextIndex = index + 1;
        if (nextIndex < choicesGrid.getChildren().size()) {
            focusNextChoiceField(nextIndex);

        } else {
            focusNextChoiceField(0);
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

        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                listener.mcqChoiceModified(textField.getText(), index);
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

        selectCorrectChoiceButton.setOnAction(e -> listener.mcqAnswerChanged(index));

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
        removeChoiceButton.setOnAction(e -> listener.mcqCardChoiceRemoved(index));

        return removeChoiceButton;
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

        Color color
                = Color.color(Math.random(), Math.random(), Math.random());

        String colorString
                = color.toString().replace("0x", "#");

        listener.tagAddedToDeck(tagText, colorString);
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
        listener.selectedCardRemoved();
    }

    @FXML
    private void handleCardPreviewClicked() {
        int cardIndex = cardsContainer.getSelectionModel().getSelectedIndex();

        if (cardIndex < 0)
            return;

        listener.cardPreviewClicked(cardIndex);
    }

    @FXML
    public void handleColorButtonClickedBackground() {
        listener.deckColorModified(colorPickerBackground.getValue());
    }

    @FXML
    public void handleColorButtonClickedTitle() {
        Color color = colorPickerTitle.getValue();
        String RGBColor = hexToRgb(color.toString());
        deckNameText.setStyle("-fx-text-inner-color: " + RGBColor + ";");

        listener.deckTitleColorModified(color);
    }

    @FXML
    private void handleUploadImageClicked() {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(mainHbox.getScene().getWindow());
        if (file != null) {
            listener.deckImageModified(file);
        }
    }

    @FXML
    private void handleFrontEditClicked() {
        listener.editFrontOfCardClicked();
    }

    @FXML
    private void handleBackEditClicked() {
        listener.editBackOfCardClicked();
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
    private void handleColorPickerBackgroundHover() {
        colorPickerBackground.setStyle("-fx-background-color: #B1B7E1");
    }

    @FXML
    private void handleColorPickerBackgroundExit() {
        colorPickerBackground.setStyle("-fx-background-color: #C3B1E1");
    }

    @FXML
    private void handleUploadImageHover() {
        imageUploader.setStyle("-fx-background-color: #B1B7E1");
    }

    @FXML
    private void handleUploadImageExit() {
        imageUploader.setStyle("-fx-background-color: #C3B1E1");
    }

    @FXML
    private void handleColorPickerHoverTitle() { colorPickerTitle.setStyle("-fx-background-color: #B1B7E1"); }

    @FXML
    private void handleColorPickerExitTitle() { colorPickerTitle.setStyle("-fx-background-color: #C3B1E1"); }

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
        listener.inputAnswerModified(answerOfInputCard.getText());
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
            }
        });


        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            mainHbox.requestFocus();
        }
    }

    @FXML
    private void handleTimerValueSave() {
        if (!timerValue.getText().isEmpty())
            return;

        listener.timerValueChanged(Integer.parseInt(timerValue.getText()));
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

    public void setRemoveChoiceButtonEnabled(boolean canRemoveChoice) {
        for (Node node : choicesGrid.getChildren()) {

            if (!(node instanceof HBox))
                continue;

            for (Node node1 : ((HBox) node).getChildren()) {
                if (node1 instanceof Button) {
                    node1.setDisable(!canRemoveChoice);
                }
            }
        }
    }


    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ==================================================================== */

    public interface Listener {
        /* Deck */
        void deckNameModified(String newName);
        void tagAddedToDeck(String tagName, String color);
        void deckColorModified(Color color);
        void deckTitleColorModified(Color color);
        void deckImageModified(File image);

        /* Card */
        void cardPreviewClicked(int index);
        void editFrontOfCardClicked();
        void selectedCardRemoved();

        /* MCQ Card */
        void newMCQCard();
        void mcqChoiceModified(String text, int index);
        void mcqAnswerChanged(int i);
        void mcqCardChoiceRemoved(int index);
        void mcqCardChoiceAdded();

        /* Flash Card */
        void newFlashCard();
        void editBackOfCardClicked();

        /* Input Card */
        void newInputCard();
        void inputAnswerModified(String answer);
        void timerValueChanged(int value);
    }
}
