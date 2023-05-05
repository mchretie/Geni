package ulb.infof307.g01.gui.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ulb.infof307.g01.gui.controller.errorhandler.ErrorHandler;
import ulb.infof307.g01.gui.http.ServerCommunicator;
import ulb.infof307.g01.gui.http.exceptions.ServerCommunicationFailedException;
import ulb.infof307.g01.gui.view.editdeck.TagViewController;
import ulb.infof307.g01.gui.view.editdeck.EditDeckViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.model.card.*;
import ulb.infof307.g01.model.card.visitor.CardVisitor;
import ulb.infof307.g01.model.deck.Deck;
import ulb.infof307.g01.model.deck.Tag;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EditDeckController implements EditDeckViewController.Listener,
                                                    TagViewController.Listener, CardVisitor {

    /* ====================================================================== */
    /*                             Model Attributes                           */
    /* ====================================================================== */

    private final Deck deck;
    private Card selectedCard;


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

    private final ServerCommunicator serverCommunicator;


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
                              ServerCommunicator serverCommunicator) {

        this.stage = stage;
        this.errorHandler = errorHandler;
        this.deck = deck;
        this.serverCommunicator = serverCommunicator;
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
            selectedCard = deck.getFirstCard();
            showSelectedCardEditor();
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
            return new ArrayList<>();
        }
    }

    /* ====================================================================== */
    /*                        View Listener Method                            */
    /* ====================================================================== */

    @Override
    public void deckNameModified(String newName) {
        try {
            deck.setName(newName.trim());
            serverCommunicator.saveDeck(deck);

        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
        }
    }

    @Override
    public void tagAddedToDeck(Deck deck, String tagName, String color) {
        try {
            if (tagName.trim().isEmpty() || deck.tagExists(tagName))
                return;

            deck.addTag(new Tag(tagName, color));
            editDeckViewController.setTags(loadTags());

            serverCommunicator.saveDeck(deck);

        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
        }
    }

    @Override
    public void inputAnswerModified(String answer) {
        try {
            ((InputCard) selectedCard).setAnswer(answer);
            serverCommunicator.saveDeck(deck);
            editDeckViewController.showCards();
            serverCommunicator.saveDeck(deck);

        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
        }
    }

    @Override
    public void timerValueChanged(int value) {
        try {
            ((TimedCard) selectedCard).setCountdownTime(value);
            serverCommunicator.saveDeck(deck);

        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
        }
    }

    @Override
    public void mcqChoiceModified(String text, int index) {
        try {
            MCQCard card = (MCQCard) selectedCard;

            if (index >= card.getChoicesCount())
                return;

            if (text.trim().isEmpty())
                card.removeChoice(index);

            else card.setChoice(index, text);

            serverCommunicator.saveDeck(deck);
            showSelectedCardEditor();

        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
        }
    }

    @Override
    public void mcqAnswerChanged(int index) {
        try {
            ((MCQCard) selectedCard).setCorrectChoice(index);
            serverCommunicator.saveDeck(deck);
            showSelectedCardEditor();

        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
        }
    }

    @Override
    public void mcqCardChoiceRemoved(int index) {
        try {
            ((MCQCard) selectedCard).removeChoice(index);
            serverCommunicator.saveDeck(deck);
            editDeckViewController.showCards();
            showSelectedCardEditor();

        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
        }
    }

    @Override
    public void mcqCardChoiceAdded() {
        try {
            ((MCQCard) selectedCard).addChoice("Nouvelle réponse");
            serverCommunicator.saveDeck(deck);
            showSelectedCardEditor();

        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
        }
    }

    private void showSelectedCardEditor() {
        selectedCard.accept(this);
    }

    @Override
    public void deckColorModified(Deck deck, Color color) {
        try {
            String colorString
                = color.toString().replace("0x", "#");
            deck.setColor(colorString);
            serverCommunicator.saveDeck(deck);

        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
        }
    }

    @Override
    public void deckTitleColorModified(Deck deck, Color color) {
        try {
            deck.setColorName(color.toString());
            serverCommunicator.saveDeck(deck);

        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
        }
    }

    @Override
    public void deckImageModified(Deck deck, File image, String filename) {
        try {
            deck.setImage(filename);
            serverCommunicator.uploadImage(image, filename);
            serverCommunicator.saveDeck(deck);

        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
        }
    }

    private void newCard(Card card) {
        try {
            deck.addCard(card);
            serverCommunicator.saveDeck(deck);

        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
        }
    }

    private void focusLastCard() {
        editDeckViewController.setSelectedCard(deck.cardCount() - 1);
        cardPreviewClicked(deck.cardCount() - 1);
    }

    @Override
    public void newFlashCard() {
        newCard(new FlashCard());
        editDeckViewController.showCards();
        focusLastCard();
    }

    @Override
    public void newInputCard() {
        newCard(new InputCard());
        editDeckViewController.showCards();
        focusLastCard();
    }

    @Override
    public void newMCQCard() {
        newCard(new MCQCard());
        editDeckViewController.showCards();
        focusLastCard();
    }

    @Override
    public void selectedCardRemoved() {
        try {
            deck.removeCard(selectedCard);
            serverCommunicator.saveDeck(deck);
            editDeckViewController.showCards();
            editDeckViewController.hideSelectedCardEditor();

            if (deck.cardCount() != 0) {
                cardPreviewClicked(0);
            }

        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
        }
    }

    @Override
    public void cardPreviewClicked(int index) {
        selectedCard = deck.getCard(index);
        showSelectedCardEditor();
    }

    @Override
    public void editBackOfCardClicked() {
        controllerListener.editBackOfCardClicked(deck, (FlashCard) selectedCard);
    }

    @Override
    public void editFrontOfCardClicked() {
        controllerListener.editFrontOfCardClicked(deck, selectedCard);
    }

    @Override
    public void tagNameChanged(Tag tag, String name) {
        try {
            tag.setName(name);
            serverCommunicator.saveDeck(deck);
            editDeckViewController.setTags(loadTags());

        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
        }
    }

    @Override
    public void tagDeleted(Tag tag) {
        try {
            deck.removeTag(tag);
            serverCommunicator.saveDeck(deck);
            editDeckViewController.setTags(loadTags());

        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
        }
    }

    @Override
    public void visit(FlashCard flashCard) {
        editDeckViewController.loadFlashCardEditor(flashCard);
    }

    @Override
    public void visit(MCQCard multipleChoiceCard) {
        editDeckViewController.loadMCQCardEditor(multipleChoiceCard);
        editDeckViewController.setRemoveChoiceButtonEnabled(multipleChoiceCard.canRemoveChoice());
    }

    @Override
    public void visit(InputCard inputCard) {
        editDeckViewController.loadInputCardEditor(inputCard);
    }

    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void editFrontOfCardClicked(Deck deck, Card card);

        void editBackOfCardClicked(Deck deck, FlashCard selectedCard);
    }
}
