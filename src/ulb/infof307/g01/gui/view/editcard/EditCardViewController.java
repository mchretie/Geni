package ulb.infof307.g01.gui.view.editcard;

import javafx.fxml.FXML;
import javafx.scene.web.HTMLEditor;
import ulb.infof307.g01.model.Card;

public class EditCardViewController {

    /* ====================================================================== */
    /*                               FXML Attributes                          */
    /* ====================================================================== */

    @FXML
    private HTMLEditor htmlEditor;


    /* ====================================================================== */
    /*                              Model Attributes                          */
    /* ====================================================================== */

    private Card selectedCard;


    /* ====================================================================== */
    /*                                  Listener                              */
    /* ====================================================================== */

    private Listener listener;


    /* ====================================================================== */
    /*                                   Setters                              */
    /* ====================================================================== */

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setCard(Card selectedCard) {
        this.selectedCard = selectedCard;
        htmlEditor.setHtmlText(selectedCard.getFront());
    }

    /* ====================================================================== */
    /*                              Click handlers                            */
    /* ====================================================================== */

    @FXML
    private void onSaveButtonClicked() {
        listener.saveButtonClicked(selectedCard, htmlEditor.getHtmlText());
    }


    /* ====================================================================== */
    /*                            Listener Interface                          */
    /* ====================================================================== */

    public interface Listener {
        void saveButtonClicked(Card card, String html);
    }
}
