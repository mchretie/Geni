package ulb.infof307.g01.controller;

import javafx.stage.Stage;
import ulb.infof307.g01.database.DeckManager;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;
import ulb.infof307.g01.view.editdeck.EditDeckViewController;
import ulb.infof307.g01.view.mainwindow.MainWindowViewController;

import java.io.IOException;

public class EditDeckController implements EditDeckViewController.Listener {

    private final Stage stage;

    private final Deck deck;

    private final MainWindowViewController mainWindowViewController;
    private final EditDeckViewController editDeckViewController;

    private final DeckManager dm = DeckManager.singleton();

    /* ============================================================================================================== */
    /*                                                  Constructor                                                   */
    /* ============================================================================================================== */

    public EditDeckController(Stage stage, Deck deck, MainWindowViewController mainWindowViewController) {
        this.stage = stage;
        this.deck = deck;
        this.mainWindowViewController = mainWindowViewController;

        this.editDeckViewController = mainWindowViewController.getEditDeckViewController();
        editDeckViewController.setListener(this);
        editDeckViewController.setDeck(deck);
    }


    /* ============================================================================================================== */
    /*                                            Stage manipulation                                                  */
    /* ============================================================================================================== */

    /**
     * Loads and displays the Deck Menu onto the main scene
     *
     * @throws IOException if FXMLLoader.load() fails
     */
    public void show() throws IOException {
        mainWindowViewController.setEditDeckViewVisible();
        mainWindowViewController.makeGoBackIconVisible();

        stage.show();
    }


    /* ============================================================================================================== */
    /*                                          View Listener Methods                                                 */
    /* ============================================================================================================== */

    @Override
    public void deckNameModified(String newName) {
        deck.setName(newName);
        dm.saveDeck(deck);
    }

    @Override
    public void tagAddedToDeck(Deck deck, String tagName) {
        deck.addTag(new Tag(tagName));
        dm.saveDeck(deck);
    }

    @Override
    public void frontOfCardModified(Card card, String newFront) {
        card.setFront(newFront);
        deck.addCard(card);
        editDeckViewController.loadCardsFromDeck();
    }

    @Override
    public void backOfCardModified(Card card, String newBack) {
        card.setBack(newBack);
        deck.addCard(card);
        editDeckViewController.loadCardsFromDeck();
    }

    @Override
    public void newCard() {
        deck.addCard(new Card("Avant", "Arrière"));
        editDeckViewController.loadCardsFromDeck();
        editDeckViewController.setSelectedCard(deck.getCards().get(deck.cardCount() - 1));
    }

    @Override
    public void cardPreviewClicked(Card card) {
        editDeckViewController.loadCardEditor(card);
    }
}