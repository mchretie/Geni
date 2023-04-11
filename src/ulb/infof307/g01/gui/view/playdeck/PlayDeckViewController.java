package ulb.infof307.g01.gui.view.playdeck;

import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
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
import java.util.Arrays;

public class PlayDeckViewController {
    ArrayList<String> colors = new ArrayList<>(Arrays.asList("#cb9172", "#b8b662", "#7b8bc9", "#c078be"));

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

    public void setDeckName(String deckName) {
        this.deckNameLabel.setText(deckName);
    }

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
            rotateTransition.setOnFinished(_e -> {
            });
            rotateTransition.play();
        });

        rotateTransition.play();
    }

    public void showNormalCard() {
        choicesGrid.setVisible(false);
    }

    public void showMCQCard() {
        choicesGrid.setVisible(true);
        loadAnswers();
    }

    public void loadAnswers() {
        choicesGrid.getChildren().clear();
        MCQCard card = (MCQCard) currentCard;
        int correctAnswer = card.getCorrectAnswer();
        ArrayList<String> answers = new ArrayList<>(card.getAnswers());

        for (int i = 0; i < ((MCQCard) currentCard).getAnswers().size(); i++) {
            int row = i / 2;
            int col = i % 2;
            boolean isCorrectAnswer = (correctAnswer == i);
            Button answer = addAnswer(answers.get(i), isCorrectAnswer, colors.get(i));
            choicesGrid.add(answer, col, row);
        }
    }

    public Button addAnswer(String answer, boolean isCorrectAnswer, String color) {
        Button checkButton = new Button(answer);
        FontIcon checkIcon = new FontIcon("mdi2c-check");
        checkIcon.setIconSize(20);
        checkButton.setGraphic(checkIcon);
        checkButton.setMinHeight(30);
        checkButton.setMinWidth(200);
        checkButton.setContentDisplay(ContentDisplay.RIGHT);
        checkButton.setStyle("-fx-background-color: " + color + "; -fx-border-radius: 5; -fx-background-radius: 5;");

        if (isCorrectAnswer) correctAnswerButton = checkButton;

        checkButton.setOnAction(actionEvent -> {
            checkIcon.setIconColor(Color.WHITE);
            showCorrectAnswers();
        });

        return checkButton;
    }

    private void showCorrectAnswers() {
        for (int i = 0; i < choicesGrid.getChildren().size(); i++) {
            Button answer = (Button) choicesGrid.getChildren().get(i);
            answer.setDisable(true);
            answer.setTextFill(Color.WHITE);
            if (answer == correctAnswerButton) {
                answer.setOpacity(1);
                answer.setStyle("-fx-background-color: #659e40;");
            } else {
                answer.setStyle("-fx-background-color: #c45151;");
            }
        }
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
