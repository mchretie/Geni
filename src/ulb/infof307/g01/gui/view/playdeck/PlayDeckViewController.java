package ulb.infof307.g01.gui.view.playdeck;

import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.FlashCard;
import ulb.infof307.g01.model.InputCard;
import ulb.infof307.g01.model.MCQCard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class PlayDeckViewController {
    ArrayList<String> colors = new ArrayList<>(Arrays.asList("#cb9172", "#b8b662", "#7b8bc9", "#c078be"));

    /* ====================================================================== */
    /*                               FXML Attributes                          */
    /* ====================================================================== */

    @FXML
    private VBox cardBox;

    @FXML
    private Label deckNameLabel;

    @FXML
    private Button cardButton;

    @FXML
    private WebView cardWebView;

    @FXML
    private GridPane choicesGrid;

    @FXML
    private Button correctChoiceButton;

    @FXML
    private BorderPane inputPane;

    @FXML
    private VBox inputBox;

    @FXML
    private TextField inputTextField;

    /* ====================================================================== */
    /*                              Model Attributes                          */
    /* ====================================================================== */

    private Card currentCard;


    /* ====================================================================== */
    /*                                Listener                                */
    /* ====================================================================== */

    private Listener listener;


    /* ====================================================================== */
    /*                                 Setters                                */
    /* ====================================================================== */

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setDeckName(String deckName) { this.deckNameLabel.setText(deckName); }

    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
        showFrontOfCard();
    }


    /* ====================================================================== */
    /*                     Card displaying and animation                      */
    /* ====================================================================== */

    private void showFrontOfCard() {
        String htmlContent = currentCard.getFront();
        cardWebView.getEngine().loadContent(htmlContent);
    }

    /*---------------------Normal Card ---------------------- */

    public void showNormalCard(){
        choicesGrid.setVisible(false);
        inputBox.setVisible(false);
    }

    public void flipToFrontOfCard() {
        flipCard(currentCard.getFront());
    }

    public void flipToBackOfCard() {
        flipCard(((FlashCard) currentCard).getBack());
    }

    private void flipCard(String newContent) {
        RotateTransition rotateTransition
                = new RotateTransition(Duration.millis(300), cardButton);

        rotateTransition.setAxis(Rotate.Y_AXIS);
        rotateTransition.setFromAngle(0);
        rotateTransition.setToAngle(90);

        rotateTransition.setOnFinished(e -> {
            cardWebView.getEngine().loadContent(newContent);

            rotateTransition.setFromAngle(90);
            rotateTransition.setToAngle(0);
            rotateTransition.setOnFinished(_e -> {});
            rotateTransition.play();
        });

        rotateTransition.play();
    }

    /*---------------------- MCQ Card ---------------------- */

    public void showMCQCard(){
        choicesGrid.setVisible(true);
        inputBox.setVisible(false);
        loadAnswers();
    }

    public void loadAnswers() {
        choicesGrid.getChildren().clear();

        MCQCard card = (MCQCard) currentCard;
        int correctChoiceIndex = card.getCorrectChoiceIndex();

        for (int i = 0; i < card.getNbOfChoices(); i++) {
            int row = i / 2;
            int col = i % 2;

            BorderPane choicePane
                    = addChoice(card.getChoice(i),
                    i == correctChoiceIndex,
                    colors.get(i));

            choicesGrid.add(choicePane, col, row);
        }
    }


    public BorderPane addChoice(String answer, boolean isCorrectChoice, String color) {
        BorderPane choicePane = new BorderPane();
        choicePane.setStyle("-fx-background-color: " + color + ";");

        TextField choiceField = createChoiceField(answer);
        Button choiceSelectionButton = createChoiceSelectionButton();

        if (isCorrectChoice)
            correctChoiceButton = choiceSelectionButton;

        choiceSelectionButton.setOnAction(actionEvent -> {
            FontIcon buttonIcon = (FontIcon) choiceSelectionButton.getGraphic();
            buttonIcon.setIconColor(Color.WHITE);
            showCorrectChoice();
        });

        choicePane.setLeft(choiceField);
        choicePane.setRight(choiceSelectionButton);

        return choicePane;
    }


    private TextField createChoiceField(String choiceText) {
        TextField choiceTextField = new TextField(choiceText);
        choiceTextField.setMinHeight(30);
        choiceTextField.setStyle("-fx-background-color: transparent;" +
                                "-fx-border-color: transparent;");
        choiceTextField.setEditable(false);

        return choiceTextField;
    }

    private Button createChoiceSelectionButton() {
        Button choiceSelectionButton = new Button();
        FontIcon checkIcon = setIcon("mdi2c-check", Color.BLACK);
        choiceSelectionButton.setGraphic(checkIcon);
        choiceSelectionButton.setMinHeight(30);
        choiceSelectionButton.setStyle("-fx-background-color:transparent");

        return choiceSelectionButton;
    }


    private void showCorrectChoice(){
        for (Node choiceNode : choicesGrid.lookupAll("BorderPane")) {
            BorderPane choice = (BorderPane) choiceNode;

            for (Node selectionNode : choice.lookupAll("Button")) {
                Button selectionButton = (Button) selectionNode;

                selectionButton.setDisable(true);

                if (selectionButton == correctChoiceButton) {
                    choice.setStyle("-fx-background-color: #659e40;");
                } else {
                    choice.setStyle("-fx-background-color: #c45151;");
                }
            }
        }
    }

    /*---------------------Input card ---------------------- */

    @FXML
    public void showInputCard(){
        inputBox.setVisible(true);
        choicesGrid.setVisible(false);

        inputTextField.setText("");
        inputTextField.setStyle("");
        inputPane.setStyle("");
        inputPane.setRight(null);

        if (inputBox.getChildren().size() > 1) inputBox.getChildren().remove(1);
    }

    @FXML
    private void handleTextFieldKeyPressed(KeyEvent keyEvent) {
        inputTextField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (" ".equals(event.getCharacter()))
                event.consume(); // Consume the space character
        });

        if (! keyEvent.getCode().equals(KeyCode.ENTER)) {
            return;
        }
        handleInputText();
        cardBox.requestFocus();
    }

    private void handleInputText(){
        InputCard card = (InputCard) currentCard;

        inputTextField.setStyle("-fx-background-color: transparent");
        if ((card.isInputCorrect(inputTextField.getText()))){
            inputPane.setRight(setIcon("mdi2c-check", Color.WHITE));
            inputPane.setStyle("-fx-background-color: #659e40;");
        }

        else {
            inputPane.setStyle("-fx-background-color: #c45151;");
            inputPane.setRight(setIcon("mdi2c-close", Color.BLACK));
            showCorrectInput();
        }
    }

    private void showCorrectInput(){
        InputCard card = (InputCard) currentCard;
        String string = card.getAnswer();

        BorderPane correctInputPane = new BorderPane();
        TextField correctInputField = new TextField(string);
        correctInputField.setEditable(false);
        correctInputField.setStyle("-fx-background-color: transparent ");

        correctInputPane.setStyle("-fx-background-color: #6bb862; -fx-border-color:#aad4a5");
        correctInputPane.setCenter(correctInputField);
        correctInputPane.setRight(setIcon("mdi2c-check", Color.WHITE));

        inputBox.getChildren().add(correctInputPane);
    }

    private FontIcon setIcon(String iconLiteral, Color color){
        FontIcon icon = new FontIcon();
        icon.setIconLiteral(iconLiteral);
        icon.setIconSize(20);
        icon.setIconColor(color);
        return icon;
    }

    /* ====================================================================== */
    /*                              Click handlers                            */
    /* ====================================================================== */

    @FXML
    private void handlePreviousCardClicked() {
        listener.previousCardClicked();
    }

    @FXML
    private void handleNextCardClicked() {
        listener.nextCardClicked();
    }

    @FXML
    private void onCardClicked() {
        listener.cardClicked();
    }

    /* ====================================================================== */
    /*                          Listener interface                            */
    /* ====================================================================== */

    public interface Listener {
        void cardClicked();
        void nextCardClicked();
        void previousCardClicked();
    }
}
