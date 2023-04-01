package ulb.infof307.g01.gui.view.editdeck;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;

import ulb.infof307.g01.model.Card;

import java.net.URL;
import java.util.ResourceBundle;

public class EditFlashCardViewController {
    @FXML
    private VBox cardSidesBox;

    @FXML
    private StackPane frontCard;
    @FXML
    private StackPane backCard;

    @FXML
    private TextField frontCardText;
    @FXML
    private TextField backCardText;

    private Listener listener;

    private Card card;


    /* ====================================================================== */
    /*                                Setters                                 */
    /* ====================================================================== */

    public void setListener(EditFlashCardViewController.Listener listener) {
        this.listener = listener;
    }

    public void setCard(Card card) {
        this.card = card;
    }


    /* ====================================================================== */
    /*                        Modify and  Card loading                        */
    /* ====================================================================== */
    private void loadCardEditor(Card card) {
        frontCardText.setText(card.getFront());
        backCardText.setText(card.getBack());
    }

    public void loadSelectedCardEditor() {
        loadCardEditor(card);
    }


    @FXML
    private void handleFrontEdit(KeyEvent keyEvent) {
        if (!keyEvent.getCode().equals(KeyCode.ENTER))
            return;
        listener.frontOfCardModified(card, frontCardText.getText());
    }

    @FXML
    private void handleBackEdit(KeyEvent keyEvent) {
        if (!keyEvent.getCode().equals(KeyCode.ENTER))
            return;
        listener.backOfCardModified(card, backCardText.getText());
    }





    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface Listener {
        void frontOfCardModified(Card card, String newFront);
        void backOfCardModified(Card card, String newBack);
    }
}
