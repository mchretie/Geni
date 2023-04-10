package ulb.infof307.g01.gui.view.playdeck;

import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.FlashCard;
import ulb.infof307.g01.model.MCQCard;

import java.util.ArrayList;
import java.util.Collection;

public class PlayDeckViewController {

    /* ====================================================================== */
    /*                               FXML Attributes                          */
    /* ====================================================================== */

    @FXML
    private GridPane choicesGrid;
    @FXML
    private Label deckNameLabel;

    @FXML
    private Button cardButton;

    @FXML
    private Button correctAnswerButton;

    @FXML
    private WebView cardWebView;


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

    public void showNormalCard(){
        choicesGrid.setVisible(false);
    }

    public void showMCQCard(){
        choicesGrid.setVisible(true);
        loadAnswers();
    }

    public void loadAnswers() {
        MCQCard card = (MCQCard) currentCard;
        int correctAnswer = card.getCorrectAnswer();
        ArrayList<String> answers = new ArrayList<>(card.getAnswers());
        int row = 0;
        int col = 0;

        for (int i = 0; i < ((MCQCard) currentCard).getAnswers().size(); i++) {
            row = i / 2;
            col = i % 2;
            boolean isCorrectAnswer = (correctAnswer == i);

            BorderPane answer = addAnswer(answers.get(i), isCorrectAnswer);
            choicesGrid.add(answer, col, row);
        }
    }

    public BorderPane addAnswer(String answer, boolean isCorrectAnswer) {
        BorderPane answerPane = new BorderPane();
        TextField answerField = createAnswerField(answer);

        Button checkButton = createCorrectAnswerButton();
        FontIcon checkIcon = (FontIcon) checkButton.getGraphic();


        checkButton.setOnAction(actionEvent -> {
            checkIcon.setIconColor(Color.WHITE);
            if (isCorrectAnswer) {
                checkButton.setStyle("-fx-background-color: green");
            } else {
                checkButton.setStyle("-fx-background-color: red");
                //set Correct answer green
            }
        });
        answerPane.setLeft(answerField);
        answerPane.setRight(checkButton);
        return answerPane;
    }

    private TextField createAnswerField(String answer) {
        TextField answerField = new TextField();
        answerField.setMinHeight(30);
        answerField.setText(answer);
        answerField.setEditable(false);

        return answerField;
    }

    private Button createCorrectAnswerButton() {
        Button correctAnswerButton = new Button();
        FontIcon checkIcon = new FontIcon("mdi2c-check");
        correctAnswerButton.setGraphic(checkIcon);
        correctAnswerButton.setMinHeight(30);

        return correctAnswerButton;
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
