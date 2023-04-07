package ulb.infof307.g01.gui.view.editdeck;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import ulb.infof307.g01.model.Tag;

import java.net.URL;
import java.util.ResourceBundle;

public class TagViewController implements Initializable {

    @FXML
    private BorderPane tagPane;

    @FXML
    private TextField tagNameField;


    private Tag tag;
    private Listener listener;

    /* ====================================================================== */
    /*                                Setter                                  */
    /* ====================================================================== */

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private void setTagColor(String color) {
        tagPane.setStyle("-fx-background-color: " + color + ";");
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

    public void handleDeleteTagButton() {
        listener.tagDeleted(tag);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /* ====================================================================== */
    /*                              Listener                                  */
    /* ====================================================================== */

    public interface Listener {
        void tagNameChanged(Tag tag, String tagName);
        void tagDeleted(Tag tag);
    }
}
