package ulb.infof307.g01.gui.view.editdeck;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.deck.Tag;

public class TagViewController {

    /* ====================================================================== */
    /*                              FXML Attributes                           */
    /* ====================================================================== */

    @FXML
    private BorderPane tagPane;

    @FXML
    private Label tagNameLabel;

    @FXML
    public FontIcon trashIcon;


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

    private void determineTagNameLabelColor() {
        Color color = this.tag.isBackgroundDark() ? Color.WHITE : Color.BLACK;
        tagNameLabel.setTextFill(color);
        trashIcon.setIconColor(color);
    }

    public void setTag(Tag tag) {
        this.tag = tag;
        tagNameLabel.setText(tag.getName());
        setTagColor(tag.getColor());
        determineTagNameLabelColor();
    }


    /* ====================================================================== */
    /*                              Handlers                                  */
    /* ====================================================================== */

    @FXML
    private void handleKeyPressedOnTextField(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
            listener.tagNameChanged(tag, tagNameLabel.getText());
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
