package ulb.infof307.g01.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ulb.infof307.g01.database.DeckManager;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EditDeckViewController {

    @FXML
    public AnchorPane editDeckPane;
    public VBox cardsContainer;
    public TextField frontCardText;
    public StackPane frontCard;
    public StackPane backCard;
    public TextField backCardText;
    public TextField deckName;

    private Deck deck;

    private Card selectedCard;

    private final DeckManager deckManager = DeckManager.singleton();

    public void setDeck(Deck deck) {
        this.deck = deck;
        deckName.setText(deck.getName());
        loadCards();
    }

    private void loadCards() {
        List<Card> cards = deck.getCards();
        ListView<String> listView = new ListView<String>();

        ObservableList<String> list = FXCollections.observableArrayList();
        listView.setItems(list);

        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            list.add(card.getFront());
        }
        listView.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                int index = listView.getSelectionModel().getSelectedIndex();
                updateSelectedCard(index);
            }
        });
        cardsContainer.getChildren().clear();
        cardsContainer.getChildren().add(listView);
    }

    public void updateSelectedCard(int index) {
        selectedCard = this.deck.getCards().get(index);
        loadCardEditor();
    }

    private void loadCardEditor() {
        frontCardText.setText(selectedCard.getFront());
        backCardText.setText(selectedCard.getBack());
        frontCard.setVisible(true);
        backCard.setVisible(true);
    }

    public void handleTagAdd(KeyEvent keyEvent) {
    }

    public void handleAddCard(ActionEvent actionEvent) {
        selectedCard = new Card("Avant", "Arrière");
        deck.addCard(selectedCard);
        deckManager.saveDeck(deck);
        loadCards();
    }

    public void handleFrontEdit(KeyEvent actionEvent) {
        selectedCard.setFront(frontCardText.getText());
        deckManager.saveDeck(deck);
    }

    public void handleBackEdit(KeyEvent actionEvent) {
        selectedCard.setBack(frontCardText.getText());
        deckManager.saveDeck(deck);
    }

    public void handleUpdateDeckName(KeyEvent keyEvent) {
        deck.setName(deckName.getText());
        deckManager.saveDeck(deck);
    }
}
