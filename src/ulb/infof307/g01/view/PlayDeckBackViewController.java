package ulb.infof307.g01.view;

import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import ulb.infof307.g01.database.DeckManager;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;

import java.util.UUID;

public class PlayDeckBackViewController {

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

    private Card card;

    public void setCard(Card card) {
        this.card = card;
        setText(card.getBack());
    }

    private void setText(String text) {
        cardButton.setText(text);
    }

    public void onCardClicked(MouseEvent mouseEvent) {

    }
}
