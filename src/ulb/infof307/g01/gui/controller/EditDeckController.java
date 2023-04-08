package ulb.infof307.g01.gui.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ulb.infof307.g01.gui.httpdao.dao.DeckDAO;
import ulb.infof307.g01.gui.view.editdeck.TagViewController;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;
import ulb.infof307.g01.gui.view.editdeck.EditDeckViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EditDeckController implements EditDeckViewController.Listener,
                                            TagViewController.Listener {

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

        editDeckViewController.setTags( loadTags() );
        editDeckViewController.showCards();

        if (deck.cardCount() > 0) {
            editDeckViewController.setSelectedCard(deck.getFirstCard());
            editDeckViewController.loadSelectedCardEditor();
        }

        else
            editDeckViewController.hideSelectedCardEditor();

        stage.show();
    }


    private List<Node> loadTags() {
        try {
            List<Node> tagViews = new ArrayList<>();

            for (Tag tag : deck.getTags()) {

                URL url = TagViewController.class.getResource("TagView.fxml");
                FXMLLoader loader = new FXMLLoader(url);

                Node node = loader.load();
                TagViewController tagViewController = loader.getController();
                tagViewController.setListener(this);
                tagViewController.setTag(tag);

                tagViews.add(node);
            }

            return tagViews;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
            editDeckViewController.setTags( loadTags() );

            deckDAO.saveDeck(deck);

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
    public void uploadImage(String filePath) {
        System.out.println(filePath);
    }

    @Override
    public void editCardClicked(Card selectedCard) {
        controllerListener.editCardClicked(deck, selectedCard);
    }

    @Override
    public void tagNameChanged(Tag tag, String name) {
        try {
            tag.setName(name);
            deckDAO.saveDeck(deck);
            editDeckViewController.setTags( loadTags() );

        } catch (InterruptedException | IOException e) {
            controllerListener.savingError(e);
        }
    }

    @Override
    public void tagDeleted(Tag tag) {
        try {
            deck.removeTag(tag);
            deckDAO.saveDeck(deck);
            editDeckViewController.setTags( loadTags() );

        } catch (InterruptedException | IOException e) {
            controllerListener.savingError(e);
        }
    }

    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void savingError(Exception e);
        void editCardClicked(Deck deck, Card card);
    }
}
