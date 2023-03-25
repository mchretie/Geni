package ulb.infof307.g01.gui.controller;

import javafx.stage.Stage;
import ulb.infof307.g01.gui.httpclient.DeckDAO;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;
import ulb.infof307.g01.gui.view.editdeck.EditDeckViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;

import java.io.IOException;

public class EditDeckController implements EditDeckViewController.Listener {

    private final Stage stage;

    private final Deck deck;

    private final MainWindowViewController mainWindowViewController;
    private final EditDeckViewController editDeckViewController;

    private final DeckDAO dm = DeckDAO.getInstance();

    private final ControllerListener controllerListener;

    /* ====================================================================== */
    /*                              Constructor                               */
    /* ====================================================================== */

    public EditDeckController(Stage stage, Deck deck,
                              MainWindowViewController mainWindowViewController,
                              ControllerListener controllerListener) {

        this.stage = stage;
        this.deck = deck;
        this.mainWindowViewController = mainWindowViewController;
        this.controllerListener = controllerListener;

        this.editDeckViewController
                = mainWindowViewController.getEditDeckViewController();

        editDeckViewController.setListener(this);
        editDeckViewController.setDeck(deck);
    }


    /* ====================================================================== */
    /*                         Stage Manipulation                             */
    /* ====================================================================== */

    /**
     * Loads and displays the Deck Menu onto the main scene
     *
     * @throws IOException if FXMLLoader.load() fails
     */
    public void show() throws IOException {
        mainWindowViewController.setEditDeckViewVisible();
        mainWindowViewController.makeGoBackIconVisible();

        editDeckViewController.loadCardsFromDeck();

        if (deck.cardCount() > 0) {
            editDeckViewController.setSelectedCard(deck.getFirstCard());
            editDeckViewController.loadSelectedCardEditor();
        }

        else
            editDeckViewController.hideSelectedCardEditor();

        stage.show();
    }


    /* ====================================================================== */
    /*                        View Listener Method                            */
    /* ====================================================================== */

    @Override
    public void deckNameModified(String newName) {
        try {
            deck.setName(newName);
            dm.saveDeck(deck);

        } catch (InterruptedException | IOException e) {
            controllerListener.savingError(e);
        }
    }

    @Override
    public void tagAddedToDeck(Deck deck, String tagName) {
        try {
            deck.addTag(new Tag(tagName));
            dm.saveDeck(deck);

        } catch (IOException | InterruptedException e) {
            controllerListener.savingError(e);
        }
    }

    @Override
    public void frontOfCardModified(Card card, String newFront) {
        try {
            card.setFront(newFront);
            dm.saveDeck(deck);
            editDeckViewController.loadCardsFromDeck();

        } catch (IOException | InterruptedException e) {
            controllerListener.savingError(e);
        }
    }

    @Override
    public void backOfCardModified(Card card, String newBack) {
        try {
            card.setBack(newBack);
            dm.saveDeck(deck);
            editDeckViewController.loadCardsFromDeck();

        } catch (IOException | InterruptedException e) {
            controllerListener.savingError(e);
        }

    }

    @Override
    public void newCard() {
        try {
            deck.addCard(new Card("Avant", "Arrière"));
            dm.saveDeck(deck);

            editDeckViewController.loadCardsFromDeck();
            editDeckViewController.setSelectedCard(deck.getLastCard());
            cardPreviewClicked(deck.getLastCard());

        } catch (IOException | InterruptedException e) {
            controllerListener.savingError(e);
        }
    }

    @Override
    public void removeCard(Card selectedCard){
        try {
            deck.removeCard(selectedCard);
            dm.saveDeck(deck);
            editDeckViewController.loadCardsFromDeck();
            editDeckViewController.hideSelectedCardEditor();
            if (deck.cardCount() != 0) {
                cardPreviewClicked(deck.getLastCard());
            }

        } catch (IOException | InterruptedException e) {
            controllerListener.savingError(e);
        }
    }

    @Override
    public void cardPreviewClicked(Card card) {
        editDeckViewController.setSelectedCard(card);
        editDeckViewController.loadSelectedCardEditor();
    }

    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void savingError(Exception e);
    }
}
