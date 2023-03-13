package ulb.infof307.g01.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ulb.infof307.g01.model.Deck;

import java.net.URL;
import java.util.ResourceBundle;

public class EditDeckViewController implements Initializable {

    @FXML
    public BorderPane editDeckPane;
    @FXML
    public Label deckVal;

    private Deck deck;

    public void setDeck(Deck deck) {
        this.deck = deck;
        deckVal.setText(deck.getName());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        editDeckPane.setUserData(this);
    }
}
