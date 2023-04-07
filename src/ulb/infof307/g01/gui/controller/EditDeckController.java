package ulb.infof307.g01.gui.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ulb.infof307.g01.gui.httpdao.dao.DeckDAO;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;
import ulb.infof307.g01.gui.view.editdeck.EditDeckViewController;
import ulb.infof307.g01.gui.view.editdeck.EditFlashCardViewController;
import ulb.infof307.g01.gui.view.editdeck.EditQCMCardViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;

import java.io.IOException;
import java.net.URL;

public class EditDeckController implements EditDeckViewController.Listener,
                                           EditFlashCardViewController.Listener,
                                           EditQCMCardViewController.Listener {

    /* ====================================================================== */
    /*                             Model Attributes                           */
    /* ====================================================================== */

    private final Deck deck;


    /* ====================================================================== */
    /*                         Controller Listener                            */
    /* ====================================================================== */

    private final ControllerListener controllerListener;


    /* ====================================================================== */
    /*                        View Controller References                      */
    /* ====================================================================== */

    private final MainWindowViewController mainWindowViewController;
    private final EditDeckViewController editDeckViewController;
    private final EditFlashCardViewController editFlashCardViewController;
    private final EditQCMCardViewController editQCMCardViewController;


    /* ====================================================================== */
    /*                             Dao Attributes                             */
    /* ====================================================================== */

    private final DeckDAO deckDAO;


    /* ====================================================================== */
    /*                             Stage Attributes                           */
    /* ====================================================================== */

    private final Stage stage;


    /* ====================================================================== */
    /*                              Constructor                               */
    /* ====================================================================== */

    public EditDeckController(Stage stage, Deck deck,
                              MainWindowViewController mainWindowViewController,
                              ControllerListener controllerListener,
                              DeckDAO deckDAO) throws IOException {

        this.stage = stage;
        this.deck = deck;
        this.deckDAO = deckDAO;
        this.mainWindowViewController = mainWindowViewController;
        this.controllerListener = controllerListener;

        this.editDeckViewController
                = mainWindowViewController.getEditDeckViewController();

        editDeckViewController.setListener(this);
        editDeckViewController.setDeck(deck);

        editFlashCardViewController = setEditFlashCard();
        editQCMCardViewController = setEditQCMCard();
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

        editDeckViewController.showTags();
        editDeckViewController.showCards();

        if (deck.cardCount() > 0) {
            editDeckViewController.setSelectedCard(deck.getFirstCard());
            editFlashCardViewController.loadCardEditor(); //TODO
        }

        else
            editDeckViewController.hideCardEditor();

        stage.show();
    }

    /* ====================================================================== */
    /*                        Card Editor Setters                             */
    /* ====================================================================== */

    public EditFlashCardViewController setEditFlashCard() throws IOException {
        URL resource = EditDeckViewController
                .class
                .getResource("EditFlashCardView.fxml");
        FXMLLoader loader = new FXMLLoader(resource);

        Node node = loader.load();

        EditFlashCardViewController flashCardController = loader.getController();

        flashCardController.setListener(this);
        editDeckViewController.setFlashCardEditor(node);

        return  flashCardController;
    }

    public EditQCMCardViewController setEditQCMCard() throws IOException {
        URL resource = EditDeckViewController
                .class
                .getResource("EditQCMCardView.fxml");
        FXMLLoader loader = new FXMLLoader(resource);

        Node node = loader.load();

        EditQCMCardViewController QCMCardController = loader.getController();
        QCMCardController.setListener(this);
        editDeckViewController.setQCMCardEditor(node);

        return QCMCardController;
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
            if (tagName.trim().isEmpty() || deck.tagExists(tagName))
                return;

            deck.addTag(new Tag(tagName, color));

            editDeckViewController.showTags();
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
            editDeckViewController.showCards();

        } catch (InterruptedException | IOException e) {
            controllerListener.savingError(e);
        }
    }

    @Override
    public void backOfCardModified(Card card, String newBack) {
        try {
            card.setBack(newBack);
            deckDAO.saveDeck(deck);
            editDeckViewController.showCards();

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

            editDeckViewController.showCards();
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
            editDeckViewController.showCards();
            editDeckViewController.hideCardEditor();
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

        editQCMCardViewController.setCard(card); //TODO
        editFlashCardViewController.setCard(card);
        editFlashCardViewController.loadCardEditor();
    }

    @Override
    public void uploadImage(String filePath) {
        System.out.println(filePath);
    }

    @Override
    public void editCardClicked(Card selectedCard) {
        controllerListener.editCardClicked(deck, selectedCard);
    }

    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void savingError(Exception e);
        void editCardClicked(Deck deck, Card card);
    }
}
