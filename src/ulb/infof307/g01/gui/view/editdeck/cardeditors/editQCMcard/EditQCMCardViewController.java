package ulb.infof307.g01.gui.view.editdeck.cardeditors.editQCMcard;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.gui.view.editdeck.cardeditors.EditFrontCardViewController;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.MCQCard;

import java.io.IOException;
import java.util.List;

public class EditQCMCardViewController implements EditFrontCardViewController.Listener, EditAnswerFieldController.Listener {
    @FXML
    private EditFrontCardViewController editFrontCardViewController;

    @FXML
    private EditAnswerFieldController editAnswerFieldController;

    @FXML
    private GridPane answersGrid;

    @FXML
    private BorderPane addAnswerField;

    @FXML
    private FontIcon addAnswerIcon;

    private int currentCol = 0;

    private int currentRow = 0;

    private MCQCard card;

    private Listener listener;

    /* ====================================================================== */
    /*                                Setters                                 */
    /* ====================================================================== */
    public EditQCMCardViewController get() { return this; }

    public void setListener(Listener listener) {
        this.listener = listener;
        editFrontCardViewController.setListener(this);
    }

    public void setCard(MCQCard card) {
        this.card = card;
        editFrontCardViewController.setCard(card);
    }
    /* ====================================================================== */
    /*                              Card loading                              */
    /* ====================================================================== */

    public void loadCardEditor(List<Node> answers){
        if (card == null) return;
        editFrontCardViewController.loadFront();
        setAnswers(answers);
    }


    /* ====================================================================== */
    /*                               FrontCard                                */
    /* ====================================================================== */

    @FXML
    public void frontModified(Card card, String newFront){
        listener.frontOfCardModified(card, newFront);
    }

    @Override
    public void editClicked(Card card) {
        listener.editCardClicked(card);
    }


    /* ====================================================================== */
    /*                            Grid handlers                               */
    /* ====================================================================== */

    @FXML
    private void handleAddNewChoice() throws IOException, InterruptedException {
        moveAnswerFieldToNext();
        listener.addNewAnswerToCard(card);
    }

    public void setAnswers(List<Node> answers){

        for (int i = 0; i < answers.size(); i++) {
            Node node = answers.get(i);
            currentRow = i / 2;
            currentCol = i % 2;
            GridPane.setRowIndex(node, currentRow);
            GridPane.setColumnIndex(node, currentCol);
        }
        answersGrid.getChildren().clear();
        answersGrid.getChildren().addAll(answers);

        moveAnswerFieldToNext();
    }

    private void moveAnswerFieldToNext(){
        int nextCol = currentCol ^ 1;
        int nextRow = currentRow;
        if (nextCol==0) nextRow +=1;

        answersGrid.getChildren().remove(addAnswerField);
        answersGrid.setConstraints(addAnswerField, nextCol, nextRow);
        answersGrid.getChildren().add(addAnswerField);

        currentCol = nextCol; currentRow = nextRow;

        //To prevent the user from adding more than 4 answersTo
        if (nextRow>1) addAnswerField.setVisible(false);
        else addAnswerField.setVisible(true);
    }

    @Override
    public void setCorrectAnswer(int idxOfAnswer) throws IOException, InterruptedException {
        listener.setCorrectAnswer(card, idxOfAnswer);
    }

    @Override
    public void answerChanged(int idxOfAnswer, String newAnswer) throws IOException, InterruptedException {
        listener.answerChanged(card, idxOfAnswer, newAnswer);
    }

    @Override
    public void deleteAnswer(int idxOfAnswer) throws IOException, InterruptedException {
        listener.removeAnswerFromCard(card, idxOfAnswer);
    }


    /* ====================================================================== */
    /*                             Hover handlers                             */
    /* ====================================================================== */

    @FXML
    private void handleAddChoiceHoverExit() {
        addAnswerIcon.setIconColor(Color.web("#000000"));}

    @FXML
    private void handleAddChoiceHover(){ addAnswerIcon.setIconColor(Color.web("#7f8281"));  }


    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface Listener {
        void frontOfCardModified(Card card, String newFront);
        void editCardClicked(Card card);
        void setCorrectAnswer(MCQCard card, int idxOfAnswer) throws IOException, InterruptedException;
        void answerChanged(MCQCard card, int idxOfAnswer, String newAnswer) throws IOException, InterruptedException;
        void addNewAnswerToCard(MCQCard card) throws IOException, InterruptedException;
        void removeAnswerFromCard(MCQCard card, int idxOfAnswer) throws IOException, InterruptedException;
    }
}
