package ulb.infof307.g01.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
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
    public Rectangle answer;
    public TextFlow cardText;

    private final Card card;

    public PlayDeckBackViewController(Card card) {
        this.card = card;
        setText(card.getBack());
    }

    private void setText(String text) {
        Text fxText = new Text(text);
        cardText.getChildren().add(fxText);
    }

}
