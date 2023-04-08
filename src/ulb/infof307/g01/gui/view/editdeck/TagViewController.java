package ulb.infof307.g01.gui.view.editdeck;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import ulb.infof307.g01.model.Tag;

public class TagViewController {

    /* ====================================================================== */
    /*                                FXML: Panes                             */
    /* ====================================================================== */

    @FXML
    private BorderPane tagPane;


    /* ====================================================================== */
    /*                              FXML:Text Fields                          */
    /* ====================================================================== */

    @FXML
    private TextField tagNameField;


    /* ====================================================================== */
    /*                             Model Attributes                           */
    /* ====================================================================== */

    private Tag tag;


    /* ====================================================================== */
    /*                                Listener                                */
    /* ====================================================================== */

    private Listener listener;


    /* ====================================================================== */
    /*                                Setter                                  */
    /* ====================================================================== */

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private void setTagColor(String color) {
        tagPane.setStyle(tagPane.getStyle() + "-fx-background-color: " + color + ";");
    }

    public void setTag(Tag tag) {
        this.tag = tag;
        tagNameField.setText(tag.getName());
        tagPane.setPrefWidth(tagNameField.getText().length() * 10 + 50);
        setTagColor(tag.getColor());
    }


    /* ====================================================================== */
    /*                              Handlers                                  */
    /* ====================================================================== */

    @FXML
    private void handleKeyPressedOnTextField(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
            listener.tagNameChanged(tag, tagNameField.getText());
    }

    @FXML
    private void handleDeleteTagButton() {
        listener.tagDeleted(tag);
    }


    /* ====================================================================== */
    /*                              Listener                                  */
    /* ====================================================================== */

    public interface Listener {
        void tagNameChanged(Tag tag, String tagName);
        void tagDeleted(Tag tag);
    }
}
