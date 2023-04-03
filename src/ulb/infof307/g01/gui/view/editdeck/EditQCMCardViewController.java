package ulb.infof307.g01.gui.view.editdeck;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import ulb.infof307.g01.model.Card;

public class EditQCMCardViewController {
    @FXML
    private StackPane card;

    @FXML
    private TextField cardText;

    private Listener listener;

    /* ====================================================================== */
    /*                                Setters                                 */
    /* ====================================================================== */

    public void setListener(EditQCMCardViewController.Listener listener) {
        this.listener = listener;
    }

    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface Listener {
        void frontOfCardModified(Card card, String newFront);
    }
}
