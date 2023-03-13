package ulb.infof307.g01.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;

import java.util.List;

public class PlayDeckViewController {
    @FXML
    public Button neverSeen;
    public Button veryBad;
    public Button bad;
    public Button average;
    public Button good;
    public Button veryGood;
    public Button cardButton;

    @FXML
    public TextFlow cardText;
    @FXML
    public HBox cardRatingBox;
    public FontIcon previousCardIcon;
    public FontIcon nextCardIcon;

    private Card card;

    private int cardIndex = 0;

    private Deck deck;

    private boolean isFront = true;

    private void flipCard() {
        updateView();
        isFront = !isFront;
    }

    private void updateView() {
        if (isFront) {
            setText(card.getFront());
            cardRatingBox.setVisible(false);
        } else {
            setText(card.getBack());
            cardRatingBox.setVisible(true);
        }

        previousCardIcon.setVisible(cardIndex != 0);

        nextCardIcon.setVisible(cardIndex != deck.getCards().size() - 1);
    }

    private void updateCard() {
        this.card = this.deck.getCards().get(cardIndex);
        updateView();
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
        updateCard();
    }

    private void setText(String text) {
        cardButton.setText(text);
    }

    public void onCardClicked(MouseEvent mouseEvent) {
        flipCard();
    }

    public void handlePreviousCardClicked(MouseEvent mouseEvent) {
        cardIndex--;
        isFront = true;
        updateCard();
    }

    public void handleNextCardClicked(MouseEvent mouseEvent) {
        cardIndex++;
        isFront = true;
        updateCard();
    }

    public void handlePreviousButtonEnter(MouseEvent mouseEvent) {
        previousCardIcon.setIconColor(Color.web("#FFFFFF"));
    }

    public void handlePreviousButtonExited(MouseEvent mouseEvent) {
        previousCardIcon.setIconColor(Color.web("#000000"));
    }

    public void handleNextButtonEnter(MouseEvent mouseEvent) {
        nextCardIcon.setIconColor(Color.web("#FFFFFF"));
    }

    public void handleNextButtonExited(MouseEvent mouseEvent) {
        nextCardIcon.setIconColor(Color.web("#000000"));
    }
}
