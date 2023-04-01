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

    @FXML
    private void handleTextFieldKeyPressed(KeyEvent keyEvent) {
        if (!keyEvent.getCode().equals(KeyCode.ENTER))
            return;

        card.requestFocus();
    }


/* ====================================================================== */
/*                           Listener Interface                           */
/* ====================================================================== */

    public interface Listener {
        void frontOfCardModified(Card card, String newFront);
    }
}
