package ulb.infof307.g01.gui.view.editcard;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;

public class EditCardViewController {
    public VBox vbox;

    /* ====================================================================== */
    /*                               FXML Attributes                          */
    /* ====================================================================== */

    @FXML
    private HTMLEditor htmlEditor;


    /* ====================================================================== */
    /*                                  Listener                              */
    /* ====================================================================== */

    private Listener listener;

    public void init() {
    } // PLEASE DO NOT DELETE

    /* ====================================================================== */
    /*                                   Setters                              */
    /* ====================================================================== */

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setContent(String content) {
        htmlEditor.setHtmlText(content);
    }

    /* ====================================================================== */
    /*                              Click handlers                            */
    /* ====================================================================== */

    @FXML
    private void onSaveButtonClicked() {
        listener.saveButtonClicked(htmlEditor.getHtmlText());
    }


    /* ====================================================================== */
    /*                            Listener Interface                          */
    /* ====================================================================== */

    public interface Listener {
        void saveButtonClicked(String html);
    }
}
