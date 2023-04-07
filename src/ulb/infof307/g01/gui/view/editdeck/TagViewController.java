package ulb.infof307.g01.gui.view.editdeck;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class TagViewController {

    @FXML
    private TextField tagNameField;

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @FXML
    private void handleKeyPressedOnTextField(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
            listener.tagNameChanged(tagNameField.getText());
    }


    /* ====================================================================== */
    /*                              Listener                                  */
    /* ====================================================================== */

    public interface Listener {
        void tagNameChanged(String tagName);
    }
}
