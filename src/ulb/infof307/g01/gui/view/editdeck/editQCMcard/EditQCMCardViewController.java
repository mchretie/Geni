package ulb.infof307.g01.gui.view.editdeck.editQCMcard;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import ulb.infof307.g01.gui.view.editdeck.EditFrontCardViewController;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.MCQCard;

public class EditQCMCardViewController {

//    @FXML
//    private EditFrontCardViewController frontCard;

    @FXML
    private GridPane choicesGrid;

    @FXML
    private BorderPane addChoiceField;

    @FXML
    private FontIcon addChoiceIcon;

    private int currentCol = 0;

    private int currentRow = 0;

    private MCQCard card;

    private Listener listener;

    /* ====================================================================== */
    /*                                Setters                                 */
    /* ====================================================================== */

    public void setListener(Listener listener) {
        this.listener = listener;
//        frontCard.setListener(this);
    }

    public void setCard(MCQCard card) {
        this.card = card;
//        frontCard.setCard(card);
    }

    public  void loadCardEditor(){
        return;
    }

    /* ====================================================================== */
    /*                            card Click handlers                         */
    /* ====================================================================== */
//    @FXML
//    public void frontModified(Card card, String newFront){
//        listener.frontOfCardModified(card, newFront);
//    }
//
//    @Override
//    public void editClicked(Card card) {
//        listener.editCardClicked(card);
//    }


    /* ====================================================================== */
    /*                            grid Click handlers                         */
    /* ====================================================================== */

    @FXML
    private void handleAddNewChoice() {
        int nextCol = currentCol ^ 1;
        int nextRow = currentRow;
        if (nextCol==0) nextRow +=1;

        choicesGrid.getChildren().remove(addChoiceField);
        choicesGrid.setConstraints(addChoiceField, nextCol, nextRow);
        choicesGrid.getChildren().add(addChoiceField);

        currentCol = nextCol; currentRow = nextRow;

        //TODO listener.addNewChoiceToCard(Card card);
    }


    /* ====================================================================== */
    /*                             Hover handlers                             */
    /* ====================================================================== */

    @FXML
    private void handleAddChoiceHoverExit() {addChoiceIcon.setIconColor(Color.web("#000000"));}

    @FXML
    private void handleAddChoiceHover(){ addChoiceIcon.setIconColor(Color.web("#7f8281"));  }


    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface Listener {
        void frontOfCardModified(Card card, String newFront);
        void editCardClicked(Card card);
    }
}
