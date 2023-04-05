package ulb.infof307.g01.gui.view.editdeck;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.Card;



public class EditFlashCardViewController {

    @FXML
    private StackPane frontCard;

    @FXML
    private WebView frontCardWebView;
    @FXML
    private TextField frontCardText;

    @FXML
    public FontIcon frontCardEditIcon;
    @FXML
    private TextField backCardText;

    private Listener listener;

    private Card card;


    /* ====================================================================== */
    /*                                Setters                                 */
    /* ====================================================================== */

    public void setListener(EditFlashCardViewController.Listener listener) {
        this.listener = listener;
    }

    public void setCard(Card card) {
        this.card = card;
    }


    /* ====================================================================== */
    /*                        Modify and  Card loading                        */
    /* ====================================================================== */
    private void loadCardEditor(Card card) {
        frontCardText.setText(card.getFront());
        backCardText.setText(card.getBack());
    }

    public void loadSelectedCardEditor() {
        loadCardEditor(card);
    }


    @FXML
    private void handleFrontEdit() {
        String newFront
                = frontCardWebView.getEngine()
                .executeScript("document.body.innerHTML")
                .toString();

        listener.frontOfCardModified(card, newFront);
    }

    @FXML
    private void handleBackEdit(KeyEvent keyEvent) {
        if (!keyEvent.getCode().equals(KeyCode.ENTER))
            return;
        listener.backOfCardModified(card, backCardText.getText());
    }

    @FXML
    private void handleTextFieldKeyPressed(KeyEvent keyEvent) {
        if (!keyEvent.getCode().equals(KeyCode.ENTER))
            return;

        frontCard.requestFocus();
    }


    @FXML
    private void handleFrontCardEditHover() {
        frontCardEditIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    private void handleFrontCardEditHoverExit() {
        frontCardEditIcon.setIconColor(Color.web("#000000"));
    }

    @FXML
    private void handleFrontEditClicked() {
        listener.editCardClicked(card);
    }


    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface Listener {
        void frontOfCardModified(Card card, String newFront);
        void backOfCardModified(Card card, String newBack);

        void editCardClicked(Card card);
    }
}
