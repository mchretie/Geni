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
import java.util.UUID;

public class EditDeckController implements EditDeckViewController.Listener,
                                                    TagViewController.Listener,
                                                    CardVisitor {

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
    /*                   Card Editor Visitor Interface                        */
    /* ====================================================================== */

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
    /*                         Stage Manipulation                             */
    /* ====================================================================== */

    private void showSelectedCardEditor() {
        selectedCard.accept(this);
    }

    /**
     * Loads and displays the Deck Menu onto the main scene
     *
     */
    public void show() {
        mainWindowViewController.setEditDeckViewVisible();
        mainWindowViewController.makeGoBackIconVisible();

        editDeckViewController.setTags(loadTags());
        editDeckViewController.showCardsFromDeck(deck);

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
    /*                           Database Access                              */
    /* ====================================================================== */

    private void saveChanges() {
        try {
            serverCommunicator.saveDeck(deck);

        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
        }
    }

    /* ====================================================================== */
    /*                        View Listener Method                            */
    /* ====================================================================== */

    @Override
    public void deckNameModified(String newName) {
        deck.setName(newName.trim());
        saveChanges();
    }

    @Override
    public void tagAddedToDeck(String tagName, String color) {
        if (tagName.trim().isEmpty() || deck.tagExists(tagName))
            return;

        deck.addTag(new Tag(tagName, color));
        editDeckViewController.setTags(loadTags());
        saveChanges();
    }

    @Override
    public void inputAnswerModified(String answer) {
        ((InputCard) selectedCard).setAnswer(answer);
        saveChanges();
    }

    @Override
    public void timerValueChanged(int value) {
        ((TimedCard) selectedCard).setCountdownTime(value);
        saveChanges();
    }

    @Override
    public void mcqChoiceModified(String text, int index) {
        MCQCard card = (MCQCard) selectedCard;

        if (index >= card.getChoicesCount())
            return;

        if (text.trim().isEmpty())
            card.removeChoice(index);

        else card.setChoice(index, text);

        saveChanges();
        showSelectedCardEditor();
    }

    @Override
    public void mcqAnswerChanged(int index) {
        ((MCQCard) selectedCard).setCorrectChoice(index);
        saveChanges();
        showSelectedCardEditor();
    }

    @Override
    public void mcqCardChoiceRemoved(int index) {
        ((MCQCard) selectedCard).removeChoice(index);
        saveChanges();
        showSelectedCardEditor();
    }

    @Override
    public void mcqCardChoiceAdded() {
        ((MCQCard) selectedCard).addChoice("Nouvelle réponse");
        saveChanges();
        showSelectedCardEditor();
    }

    @Override
    public void deckColorModified(Color color) {
        String colorString
            = color.toString().replace("0x", "#");
        deck.setColor(colorString);
        saveChanges();
    }

    @Override
    public void deckTitleColorModified(Color color) {
        deck.setColorName(color.toString());
        saveChanges();
    }

    @Override
    public void deckImageModified(File image) {
        try {
            String filename = "/backgrounds/" + UUID.randomUUID().toString() + ".jpg";
            deck.setImage(filename);
            serverCommunicator.uploadImage(image, filename);
            saveChanges();

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
        deck.addCard(new FlashCard());
        editDeckViewController.showCardsFromDeck(deck);
        focusLastCard();
        saveChanges();
    }

    @Override
    public void newInputCard() {
        deck.addCard(new InputCard());
        editDeckViewController.showCardsFromDeck(deck);
        focusLastCard();
        saveChanges();
    }

    @Override
    public void newMCQCard() {
        deck.addCard(new MCQCard());
        editDeckViewController.showCardsFromDeck(deck);
        focusLastCard();
        saveChanges();
    }

    @Override
    public void selectedCardRemoved() {
        deck.removeCard(selectedCard);
        saveChanges();

        editDeckViewController.showCardsFromDeck(deck);
        editDeckViewController.hideSelectedCardEditor();

        if (deck.cardCount() != 0) {
            cardPreviewClicked(0);
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
        tag.setName(name);
        saveChanges();
        editDeckViewController.setTags(loadTags());
    }

    @Override
    public void tagDeleted(Tag tag) {
        deck.removeTag(tag);
        saveChanges();
        editDeckViewController.setTags(loadTags());
    }


    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void editFrontOfCardClicked(Deck deck, Card card);

        void editBackOfCardClicked(Deck deck, FlashCard selectedCard);
    }
}
