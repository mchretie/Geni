package ulb.infof307.g01.gui.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ulb.infof307.g01.gui.controller.errorhandler.ErrorHandler;
import ulb.infof307.g01.gui.httpdao.dao.DeckDAO;
import ulb.infof307.g01.gui.view.editdeck.TagViewController;
import ulb.infof307.g01.model.*;
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

    private int selectedCardIndex = 0;


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
    private final ErrorHandler errorHandler;

    /* ====================================================================== */
    /*                              Constructor                               */
    /* ====================================================================== */

    public EditDeckController(Stage stage, Deck deck,
                              ErrorHandler errorHandler,
                              MainWindowViewController mainWindowViewController,
                              ControllerListener controllerListener,
                              DeckDAO deckDAO) {

        this.stage = stage;
        this.errorHandler = errorHandler;
        this.deck = deck;
        this.deckDAO = deckDAO;
        this.mainWindowViewController = mainWindowViewController;
        this.controllerListener = controllerListener;

        this.editDeckViewController
                = mainWindowViewController.getEditDeckViewController();

        editDeckViewController.setListener(this);
        editDeckViewController.init();
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

        editDeckViewController.setTags(loadTags());
        editDeckViewController.showCards();

        if (deck.cardCount() > 0) {
            List<Card> deckCards =deck.getCards();
            if (selectedCardIndex >= deckCards.size()) {
                selectedCardIndex = 0;
            }
            editDeckViewController.setSelectedCard(deckCards.get(selectedCardIndex));
            editDeckViewController.loadSelectedCardEditor();
        } else
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
            errorHandler.savingError(e);
        }
    }

    @Override
    public void setSelectedCardIndex(int index) {
        selectedCardIndex = index;
    }

    @Override
    public void tagAddedToDeck(Deck deck, String tagName, String color) {
        try {
            if (tagName.trim().isEmpty() || deck.tagExists(tagName))
                return;

            deck.addTag(new Tag(tagName, color));
            editDeckViewController.setTags(loadTags());

            deckDAO.saveDeck(deck);

        } catch (InterruptedException | IOException e) {
            errorHandler.savingError(e);
        }
    }

    @Override
    public void inputAnswerModified(InputCard inputcard, String answer) {
        try {
            inputcard.setAnswer(answer);
            deckDAO.saveDeck(deck);
            editDeckViewController.showCards();

        } catch (InterruptedException | IOException e) {
            errorHandler.savingError(e);
        }
    }

    @Override
    public void choiceModified(MCQCard mcqCard, String text, int index) {
        try {
            mcqCard.setAnswer(index, text);
            deckDAO.saveDeck(deck);
            editDeckViewController.showCards();

        } catch (InterruptedException | IOException e) {
            errorHandler.savingError(e);
        }
    }

    @Override
    public void correctChoiceChanged(MCQCard mcqCard, int index) {
        try {
            mcqCard.setCorrectAnswer(index);
            deckDAO.saveDeck(deck);
            editDeckViewController.showCards();

        } catch (InterruptedException | IOException e) {
            errorHandler.savingError(e);
        }
    }

    @Override
    public void choiceRemoved(MCQCard mcqCard, int index) {
        try {
            mcqCard.removeAnswer(index);
            deckDAO.saveDeck(deck);
            editDeckViewController.showCards();

        } catch (InterruptedException | IOException e) {
            errorHandler.savingError(e);
        }
    }

    @Override
    public void choiceAdded(MCQCard mcqCard) {
        try {
            mcqCard.addAnswer("Nouvelle réponse");
            deckDAO.saveDeck(deck);
            editDeckViewController.showCards();

        } catch (InterruptedException | IOException e) {
            errorHandler.savingError(e);
        }
    }

    @Override
    public void deckColorModified(Deck deck, Color color) {
        try {
            deck.setColor(color.toString());
            deckDAO.saveDeck(deck);

        } catch (InterruptedException | IOException e) {
            errorHandler.savingError(e);
        }
    }

    private void newCard(Card card) {
        try {
            deck.addCard(card);
            deckDAO.saveDeck(deck);
        } catch (InterruptedException | IOException e) {
            errorHandler.savingError(e);
        }
    }

    @Override
    public void newFlashCard() {
        String frontHtml = "Avant";
        newCard(new FlashCard(frontHtml, ""));

        editDeckViewController.showCards();
        editDeckViewController.setSelectedCard(deck.getLastCard());
        cardPreviewClicked(deck.getLastCard());

    }

    @Override
    public void newInputCard() {
        String frontHtml = "Avant";
        newCard(new InputCard(frontHtml, ""));

        editDeckViewController.showCards();
        editDeckViewController.setSelectedCard(deck.getLastCard());
        cardPreviewClicked(deck.getLastCard());


    }

    @Override
    public void newMCQCard() {
        String frontHtml = "Avant";
        List<String> answers = new ArrayList<>();

        final int MAX_QCM_ANSWERS = 4;
        for (int i = 0; i < MAX_QCM_ANSWERS; i++)
            answers.add("Réponse " + i);

        newCard(new MCQCard(frontHtml, answers, 0));

        editDeckViewController.showCards();
        editDeckViewController.setSelectedCard(deck.getLastCard());
        cardPreviewClicked(deck.getLastCard());

    }

    @Override
    public void removeCard(Card selectedCard) {
        try {
            deck.removeCard(selectedCard);
            deckDAO.saveDeck(deck);
            editDeckViewController.showCards();
            editDeckViewController.hideSelectedCardEditor();

            if (deck.cardCount() != 0) {
                cardPreviewClicked(deck.getLastCard());
            }

        } catch (InterruptedException | IOException e) {
            errorHandler.savingError(e);
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
    public void editBackOfCardClicked(FlashCard selectedCard) {
        controllerListener.editBackOfCardClicked(deck, selectedCard);
    }

    @Override
    public void editFrontOfCardClicked(Card selectedCard) {
        controllerListener.editFrontOfCardClicked(deck, selectedCard);
    }

    @Override
    public void tagNameChanged(Tag tag, String name) {
        try {
            tag.setName(name);
            deckDAO.saveDeck(deck);
            editDeckViewController.setTags(loadTags());

        } catch (InterruptedException | IOException e) {
            errorHandler.savingError(e);
        }
    }

    @Override
    public void tagDeleted(Tag tag) {
        try {
            deck.removeTag(tag);
            deckDAO.saveDeck(deck);
            editDeckViewController.setTags(loadTags());

        } catch (InterruptedException | IOException e) {
            errorHandler.savingError(e);
        }
    }

    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void editFrontOfCardClicked(Deck deck, Card card);
        void editBackOfCardClicked(Deck deck, FlashCard selectedCard);
    }
}
