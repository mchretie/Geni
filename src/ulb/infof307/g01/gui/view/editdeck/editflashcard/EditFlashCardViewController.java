package ulb.infof307.g01.gui.view.editdeck.editflashcard;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import ulb.infof307.g01.gui.view.editdeck.EditFrontCardViewController;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.FlashCard;

public class EditFlashCardViewController implements EditFrontCardViewController.Listener {

    @FXML
    private EditFrontCardViewController frontCard;

    @FXML
    private TextField backCardText;

    private Listener listener;

    private FlashCard card;


    /* ====================================================================== */
    /*                                Setters                                 */
    /* ====================================================================== */

    public void setListener(Listener listener) {
        this.listener = listener;
        frontCard.setListener(this);
    }

    public void setCard(FlashCard card) {
        this.card = card;
        frontCard.setCard(card);
    }



    /* ====================================================================== */
    /*                        Modify and  Card loading                        */
    /* ====================================================================== */
    public void loadCardEditor() {
        if (card == null ) return;
        frontCard.loadFront();
        backCardText.setText(card.getBack());
    }


    @FXML
    private void handleBackEdit(KeyEvent keyEvent) {
        if (!keyEvent.getCode().equals(KeyCode.ENTER))
            return;
        listener.backOfCardModified(card, backCardText.getText());
    }

    @FXML
    public void frontModified(Card card, String newFront){
        listener.frontOfCardModified(card, newFront);
    }

    @Override
    public void editClicked(Card card) {
        listener.editCardClicked(card);
    }

    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface Listener {
        void frontOfCardModified(Card card, String newFront);
        void backOfCardModified(Card card, String newBack);
        void editCardClicked(Card card);
    }
}
