package ulb.infof307.g01.gui.view.editdeck.cardeditors;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.Card;

public class EditFrontCardViewController {
    @FXML
    private WebView frontCardWebView;
    @FXML
    public FontIcon frontCardEditIcon;

    private Listener listener;

    private Card card;


    /* ====================================================================== */
    /*                                Setters                                 */
    /* ====================================================================== */

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setCard(Card card) {
        this.card = card;
    }



    /* ====================================================================== */
    /*                        Modify and  Front loading                        */
    /* ====================================================================== */

    public void loadFront(){
        if (card == null ) return;
        frontCardWebView.getEngine().loadContent(card.getFront());
    }

    @FXML
    private void handleFrontEdit(KeyEvent keyEvent) {
        if (!keyEvent.getCode().equals(KeyCode.ENTER))
            return;
        String newFront
                = frontCardWebView.getEngine()
                .executeScript("document.body.innerHTML")
                .toString();

        listener.frontModified(card, newFront);
    }

    @FXML
    private void handleFrontEditClicked() {
        listener.editClicked(card);
    }

    /* ====================================================================== */
    /*                             Hover handlers                             */
    /* ====================================================================== */

    @FXML
    private void handleFrontCardEditHover() {
        frontCardEditIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    private void handleFrontCardEditHoverExit() {
        frontCardEditIcon.setIconColor(Color.web("#000000"));
    }


    public interface Listener {
        void frontModified(Card card, String newFront);

        void editClicked(Card card);
    }

}
