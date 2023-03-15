package ulb.infof307.g01.view.editdeck;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import ulb.infof307.g01.database.DeckDAO;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;

import java.util.List;

public class EditDeckViewController {

    @FXML
    public VBox cardsContainer;
    public TextField frontCardText;
    public StackPane frontCard;
    public StackPane backCard;
    public TextField backCardText;
    public TextField deckName;

    private Deck deck;

    private Card selectedCard;

    private final DeckDAO deckDAO = DeckDAO.singleton();

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
        deckDAO.saveDeck(deck);
        loadCards();
    }

    public void handleFrontEdit(KeyEvent actionEvent) {
        selectedCard.setFront(frontCardText.getText());
        deckDAO.saveDeck(deck);
    }

    public void handleBackEdit(KeyEvent actionEvent) {
        selectedCard.setBack(backCardText.getText());
        deckDAO.saveDeck(deck);
    }

    public void handleUpdateDeckName(KeyEvent keyEvent) {
        deck.setName(deckName.getText());
        deckDAO.saveDeck(deck);
    }
}
