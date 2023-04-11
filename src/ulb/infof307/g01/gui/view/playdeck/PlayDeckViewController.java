package ulb.infof307.g01.gui.view.playdeck;

import javafx.animation.RotateTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.FlashCard;
import ulb.infof307.g01.model.MCQCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

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
    private Button correctChoiceButton;

    @FXML
    private WebView cardWebView;

    @FXML
    private Label currentCardIndexLabel;

    @FXML
    private Label cardNumberIndexLabel;


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

    public void setNumberOfCards(int value) {
        this.cardNumberIndexLabel.setText(String.valueOf(value));
    }

    public void setCurrentCard(Card currentCard, int index) {
        this.currentCard = currentCard;
        this.currentCardIndexLabel.setText(String.valueOf(index+1));
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
        FontIcon checkIcon = new FontIcon("mdi2c-check");
        checkIcon.setIconSize(20);
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
