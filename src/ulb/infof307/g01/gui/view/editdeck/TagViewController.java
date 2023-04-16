package ulb.infof307.g01.gui.view.editdeck;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.Tag;

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
        String color = isDarkHex(tag.getColor()) ? "#FFFFFF" : "#000000";
        tagNameLabel.setStyle(tagNameLabel.getStyle() + "-fx-text-fill: " + color + ";");
        trashIcon.setIconColor(Color.web(color));
    }

    private boolean isDarkHex(String hexString) {
        // Convert the hex string to an RGB color value
        int r = Integer.parseInt(hexString.substring(1, 3), 16);
        int g = Integer.parseInt(hexString.substring(3, 5), 16);
        int b = Integer.parseInt(hexString.substring(5, 7), 16);

        // Calculate the brightness value of the color
        double brightness = (0.299 * r + 0.587 * g + 0.114 * b) / 255;

        // Return true if the brightness is less than 0.5 (i.e., the color is "dark")
        return brightness < 0.3;
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
