package ulb.infof307.g01.gui.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ulb.infof307.g01.gui.httpdao.dao.DeckDAO;
import ulb.infof307.g01.gui.view.editdeck.editQCMcard.EditAnswerFieldController;
import ulb.infof307.g01.model.*;
import ulb.infof307.g01.gui.view.editdeck.EditDeckViewController;
import ulb.infof307.g01.gui.view.editdeck.editflashcard.EditFlashCardViewController;
import ulb.infof307.g01.gui.view.editdeck.editQCMcard.EditQCMCardViewController;
import ulb.infof307.g01.gui.view.editdeck.TagViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EditDeckController implements EditDeckViewController.Listener,
                                           EditFlashCardViewController.Listener,
                                           EditQCMCardViewController.Listener,
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

        editDeckViewController.setTags(loadTags());
        editDeckViewController.showCards();

        if (deck.cardCount() > 0) {
            editDeckViewController.setSelectedCard(deck.getFirstCard());
            editFlashCardViewController.loadCardEditor(); //TODO
        }

        else
            editDeckViewController.hideCardEditor();

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
    /*                        Card Editors Setters                            */
    /* ====================================================================== */

    public EditFlashCardViewController setEditFlashCard() throws IOException {
        URL resource = EditDeckViewController
                .class
                .getResource("editflashcard/EditFlashCardView.fxml");
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
                .getResource("editQCMcard/EditQCMCardView.fxml");
        FXMLLoader loader = new FXMLLoader(resource);

        Node node = loader.load();

        EditQCMCardViewController QCMCardController = loader.getController();
        QCMCardController.setListener(this);
        editDeckViewController.setQCMCardEditor(node);

        return QCMCardController;
    }

    /* ====================================================================== */
    /*                        View Listener Methods                           */
    /* ====================================================================== */


    /* ====================================================================== */
    /*                             Modify Card                                */
    /* ====================================================================== */

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
    public void editCardClicked(Card selectedCard) {
        controllerListener.editCardClicked(deck, selectedCard);
    }

    @Override
    public void backOfCardModified(FlashCard card, String newBack) {
        try {
            card.setBack(newBack);
            deckDAO.saveDeck(deck);
            editDeckViewController.showCards();

        } catch (InterruptedException | IOException e) {
            controllerListener.savingError(e);
        }
    }

    private List<Node> loadAnswers(MCQCard card){
        try {
            int idx = 0;
            List<Node> answerViews = new ArrayList<>();
            for (String answer: card.getAnswers()){
                URL url = EditAnswerFieldController.class.getResource("EditAnswerField.fxml");
                FXMLLoader loader = new FXMLLoader(url);

                Node node = loader.load();
                EditAnswerFieldController answerViewController = loader.getController();
                answerViewController.setListener(editQCMCardViewController.get());
                answerViewController.setAnswerText(answer, idx);
                if (idx == card.getCorrectAnswer())
                    answerViewController.setCorrectAnswer();
                idx++;
                answerViews.add(node);
            }
            return answerViews;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void setCorrectAnswer(MCQCard card, int idxOfAnswer) {
        card.setCorrectAnswer(idxOfAnswer);
        editQCMCardViewController.loadCardEditor(loadAnswers(card));
    }

    @Override
    public void answerChanged(MCQCard card, int idxOfAnswer, String newAnswer) {
        ArrayList<String> answers = new ArrayList<>(card.getAnswers());
        answers.set(idxOfAnswer, newAnswer);
        card.setAnswers(answers);
        editQCMCardViewController.loadCardEditor(loadAnswers(card));
    }

    @Override
    public void addNewAnswerToCard(MCQCard card) {
        ArrayList<String> answers = new ArrayList<>(card.getAnswers());
        answers.add("Réponse");
        card.setAnswers(answers);
        editQCMCardViewController.loadCardEditor(loadAnswers(card));
    }

    @Override
    public void removeAnswerFromCard(MCQCard card, int idxOfAnswer) {
        ArrayList<String> answers = new ArrayList<>(card.getAnswers());
        answers.remove(idxOfAnswer);
        card.setAnswers(answers);
        editQCMCardViewController.loadCardEditor(loadAnswers(card));
    }


    /* ====================================================================== */
    /*                             Cards Manager                              */
    /* ====================================================================== */

    @Override
    public void newCard(String type) {
        try {
            if (type.equals("flashCard"))
            deck.addCard(new FlashCard("Avant", "Arrière"));
            else
                deck.addCard(new MCQCard("Avant", List.of("Réponse 1", "Réponse 2", "Répponse 3"), 1));
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

        if (card instanceof FlashCard){
            editFlashCardViewController.setCard((FlashCard) card);
            editFlashCardViewController.loadCardEditor();
            editDeckViewController.showFlashCardEditor();
        }
        else if (card instanceof MCQCard){
            editQCMCardViewController.setCard((MCQCard) card);
            editQCMCardViewController.loadCardEditor(loadAnswers((MCQCard) card));
            editDeckViewController.showQCMCardEditor();
        }

    }

    /* ====================================================================== */
    /*                             Tags Manager                              */
    /* ====================================================================== */


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
    /*                             Modify Deck                                */
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
    public void deckColorModified(Deck deck, Color color) {
        try {
            deck.setColor(color.toString());
            deckDAO.saveDeck(deck);

        } catch (InterruptedException | IOException e) {
            controllerListener.savingError(e);
        }
    }

    @Override
    public void uploadImage(String filePath) {
        System.out.println(filePath);
    }



    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void savingError(Exception e);
        void editCardClicked(Deck deck, Card card);
    }
}
