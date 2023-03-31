package ulb.infof307.g01.gui.controller;

import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ulb.infof307.g01.gui.httpdao.dao.DeckDAO;
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

    private final DeckDAO deckDAO;

    private final ControllerListener controllerListener;

    /* ====================================================================== */
    /*                              Constructor                               */
    /* ====================================================================== */

    public EditDeckController(Stage stage, Deck deck,
                              MainWindowViewController mainWindowViewController,
                              ControllerListener controllerListener,
                              DeckDAO deckDAO) {

        this.stage = stage;
        this.deck = deck;
        this.deckDAO = deckDAO;
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

        editDeckViewController.loadTagsFromDeck();
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
            deck.setName(newName.trim());
            deckDAO.saveDeck(deck);

        } catch (InterruptedException | IOException e) {
            controllerListener.savingError(e);
        }
    }

    @Override
    public void tagAddedToDeck(Deck deck, String tagName, String color) {
        try {
            deck.addTag(new Tag(tagName, color));
            deckDAO.saveDeck(deck);

        } catch (InterruptedException | IOException e) {
            controllerListener.savingError(e);
        }
    }

    @Override
    public void frontOfCardModified(Card card, String newFront) {
        try {
            card.setFront(newFront);
            deckDAO.saveDeck(deck);
            editDeckViewController.loadCardsFromDeck();

        } catch (InterruptedException | IOException e) {
            controllerListener.savingError(e);
        }
    }

    @Override
    public void backOfCardModified(Card card, String newBack) {
        try {
            card.setBack(newBack);
            deckDAO.saveDeck(deck);
            editDeckViewController.loadCardsFromDeck();

        } catch (InterruptedException | IOException e) {
            controllerListener.savingError(e);
        }
    }

    @Override
    public void deckColorModified(Deck deck, Color color) {
        try {
            deck.setColor(color.toString());
            deckDAO.saveDeck(deck);

        } catch (InterruptedException | IOException e) {
            controllerListener.savingError(e);
        }
    }

    @Override
    public void newCard() {
        try {
            deck.addCard(new Card("Avant", "Arrière"));
            deckDAO.saveDeck(deck);

            editDeckViewController.loadCardsFromDeck();
            editDeckViewController.setSelectedCard(deck.getLastCard());
            cardPreviewClicked(deck.getLastCard());

        } catch (InterruptedException | IOException e) {
            controllerListener.savingError(e);
        }
    }

    @Override
    public void removeCard(Card selectedCard){
        try {
            deck.removeCard(selectedCard);
            deckDAO.saveDeck(deck);
            editDeckViewController.loadCardsFromDeck();
            editDeckViewController.hideSelectedCardEditor();
            if (deck.cardCount() != 0) {
                cardPreviewClicked(deck.getLastCard());
            }

        } catch (InterruptedException | IOException e) {
            controllerListener.savingError(e);
        }
    }

    @Override
    public void cardPreviewClicked(Card card) {
        editDeckViewController.setSelectedCard(card);
        editDeckViewController.loadSelectedCardEditor();
    }

    @Override
    public void openFrontCardEditor(Card selectedCard) {
//        mainWindowViewController.openCardEditor(selectedCard);
        controllerListener.openCardEditor(selectedCard, deck);
    }

    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void savingError(Exception e);

        void openCardEditor(Card selectedCard, Deck deck);
    }
}
