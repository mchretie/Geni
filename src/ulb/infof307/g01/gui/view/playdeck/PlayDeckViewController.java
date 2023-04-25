package ulb.infof307.g01.gui.view.playdeck;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.gui.util.GridPosIterator;
import ulb.infof307.g01.gui.util.Pos2D;
import ulb.infof307.g01.model.card.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class PlayDeckViewController {

    ArrayList<String> colors = new ArrayList<>(Arrays.asList("#cb9172", "#b8b662", "#7b8bc9", "#c078be"));

    /* ====================================================================== */
    /*                               FXML Attributes                          */
    /* ====================================================================== */

    @FXML
    private GridPane choicesGrid;

    @FXML
    private VBox cardBox;

    @FXML
    private Label deckNameLabel;

    @FXML
    private Button cardButton;

    @FXML
    private WebView cardWebView;

    private Button correctChoiceButton;

    @FXML
    private VBox inputBox;

    private TextField inputTextField;
    @FXML
    private Label currentCardIndexLabel;

    @FXML
    private Label cardNumberIndexLabel;

    @FXML
    private ProgressBar countdown;


    /* ====================================================================== */
    /*                              Model Attributes                          */
    /* ====================================================================== */

    private Card currentCard;
    private boolean hasAnswered = false;
    private Timeline timeline;


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

    public void setDeckName(String deckName) {
        this.deckNameLabel.setText(deckName);
    }

    public void setNumberOfCards(int value) {
        this.cardNumberIndexLabel.setText(String.valueOf(value));
    }

    public void setCurrentCard(Card currentCard, int index) {
        this.currentCard = currentCard;
        this.currentCardIndexLabel.setText(String.valueOf(index + 1));
        showFrontOfCard();
    }

    public void setTimer() {
        if (currentCard instanceof FlashCard) {
            this.countdown.setVisible(false);
            return;
        }

        this.countdown.setVisible(true);
        Integer seconds = ((TimedCard) currentCard).getCountdownTime();

        countdown.setStyle("-fx-accent: GREEN");

        timeline = new Timeline(
                // begin
                new KeyFrame(Duration.ZERO, new KeyValue(countdown.progressProperty(), 1)),

                // 1/3 time
                new KeyFrame(Duration.seconds((double) seconds/3), e -> {
                    countdown.setStyle("-fx-accent: ORANGE");
                }, new KeyValue(countdown.progressProperty(), 0.66)),

                // 2/3 time
                new KeyFrame(Duration.seconds((double) 2*seconds/3), e -> {
                    countdown.setStyle("-fx-accent: RED");
                }, new KeyValue(countdown.progressProperty(), 0.33)),

                // end time
                new KeyFrame(Duration.seconds(seconds), e -> {
                    if (!hasAnswered) {
                        if (this.currentCard instanceof MCQCard) {
                            stopCountdown();
                            showCorrectAnswers();
                        } else if (this.currentCard instanceof InputCard) {
                            handleInputText();
                        }
                    }
                }, new KeyValue(countdown.progressProperty(), 0))
        );
        timeline.play();
    }


    /* ====================================================================== */
    /*                     Card displaying and animation                      */
    /* ====================================================================== */

    private void showFrontOfCard() {
        String htmlContent = currentCard.getFront();
        cardWebView.getEngine().loadContent(htmlContent);
    }

    private void stopCountdown() {
        this.countdown.setVisible(false);
        this.hasAnswered = true;
    }

    /*---------------------Normal Card ---------------------- */

    public void showNormalCard() {
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
            rotateTransition.setOnFinished(_e -> {
            });
            rotateTransition.play();
        });

        rotateTransition.play();
    }

    /*---------------------- MCQ Card ---------------------- */

    public void showMCQCard() {
        choicesGrid.setVisible(true);
        inputBox.setVisible(false);
        loadAnswers();
    }

    public void loadAnswers() {
        choicesGrid.getChildren().clear();

        MCQCard card = (MCQCard) currentCard;
        int correctChoiceIndex = card.getCorrectChoiceIndex();
        Iterator<Pos2D> positions = new GridPosIterator(2, 2);

        for (int i = 0; i < card.getChoicesCount(); i++) {
            Pos2D pos = positions.next();
            boolean isCorrectAnswer = (correctChoiceIndex == i);
            Button answer = addAnswer(card.getChoice(i), isCorrectAnswer, colors.get(i));
            choicesGrid.add(answer, pos.col, pos.row);
        }
    }

    public Button addAnswer(String answer, boolean isCorrectChoice, String color) {
        Button checkButton = new Button(answer);
        FontIcon checkIcon = new FontIcon("mdi2c-check");
        checkIcon.setIconSize(20);
        checkButton.setGraphic(checkIcon);
        checkButton.setMinHeight(30);
        checkButton.setMinWidth(200);
        checkButton.setContentDisplay(ContentDisplay.RIGHT);
        checkButton.setStyle("-fx-background-color: " + color + "; -fx-border-radius: 5; -fx-background-radius: 5;");

        if (isCorrectChoice) correctChoiceButton = checkButton;

        checkButton.setOnAction(actionEvent -> {
            checkIcon.setIconColor(Color.WHITE);
            stopCountdown();
            showCorrectAnswers();
            listener.onChoiceEntered(isCorrectChoice, countdown.getProgress());
        });

        return checkButton;
    }

    private void showCorrectAnswers() {
        for (int i = 0; i < choicesGrid.getChildren().size(); i++) {
            Button answer = (Button) choicesGrid.getChildren().get(i);
            answer.setDisable(true);
            answer.setTextFill(Color.WHITE);
            if (answer == correctChoiceButton) {
                answer.setOpacity(1);
                answer.setStyle("-fx-background-color: #659e40;");
            } else {
                answer.setStyle("-fx-background-color: #c45151;");
            }
        }
    }

    /*---------------------Input card ---------------------- */

    @FXML
    public void showInputCard() {
        inputBox.setVisible(true);
        choicesGrid.setVisible(false);

        inputBox.getChildren().clear();

        inputTextField = new TextField();
        inputTextField.onKeyPressedProperty().set(this::handleTextFieldKeyPressed);

        Button approveAnswer = new Button();
        FontIcon checkIcon = setIcon("mdi2c-check", Color.BLACK);
        approveAnswer.setGraphic(checkIcon);
        approveAnswer.onMouseClickedProperty().set(mouseEvent -> handleInputText());

        HBox inputHBox = new HBox(2);
        inputHBox.setAlignment(Pos.BASELINE_CENTER);
        inputHBox.getChildren().addAll(inputTextField, approveAnswer);

        inputBox.getChildren().add(inputHBox);

        if (inputBox.getChildren().size() > 1) inputBox.getChildren().remove(1);
    }

    @FXML
    private void handleTextFieldKeyPressed(KeyEvent keyEvent) {
        inputTextField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (" ".equals(event.getCharacter()))
                event.consume(); // Consume the space character
        });

        if (!keyEvent.getCode().equals(KeyCode.ENTER)) {
            return;
        }
        handleInputText();
        cardBox.requestFocus();
    }

    @FXML
    private void handleInputText() {
        InputCard card = (InputCard) currentCard;

        inputTextField.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        inputTextField.setDisable(true);

        String input = inputTextField.getText();

        inputBox.getChildren().clear();
        inputBox.setSpacing(2);

        boolean correct = card.isInputCorrect(input);

        stopCountdown();
        listener.onChoiceEntered(correct, countdown.getProgress());

        if (!correct) showInput(input, false);

        showInput(card.getAnswer(), true);
    }

    private void showInput(String input, Boolean correct) {
        Text inputText = new Text(input);
        inputText.setStyle("-fx-background-color: transparent;");
        if (correct) inputText.setFill(Color.WHITE);

        HBox inputHbox = new HBox(2);
        String color = correct ? "#659e40" : "#c45151";
        inputHbox.setStyle("-fx-background-color: " + color + "; -fx-border-radius: 5; -fx-background-radius: 5;");

        inputHbox.setAlignment(Pos.BASELINE_CENTER);
        inputHbox.setPadding(new Insets(5, 5, 5, 5));

        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        FontIcon checkIcon = correct ? setIcon("mdi2c-check", Color.WHITE) : setIcon("mdi2c-close", Color.BLACK);

        inputHbox.getChildren().add(inputText);
        inputHbox.getChildren().add(region);
        inputHbox.getChildren().add(checkIcon);

        inputBox.getChildren().add(inputHbox);
    }


    private FontIcon setIcon(String iconLiteral, Color color) {
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
        hasAnswered = false;
        if (timeline != null)
            timeline.stop();
        listener.previousCardClicked();
    }

    @FXML
    private void handleNextCardClicked() {
        hasAnswered = false;
        if (timeline != null)
            timeline.stop();
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

        void onChoiceEntered(boolean isGoodChoice, double timeLeft);
    }
}
