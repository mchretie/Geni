package ulb.infof307.g01.gui.view.playdeck;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import ulb.infof307.g01.model.Card;

public class PlayDeckViewController {

    @FXML
    private Label deckNameLabel;

    @FXML
    private Button cardButton;

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setDeckName(String deckName) { this.deckNameLabel.setText(deckName); }

    public void showFrontOfCard(Card currentCard) {
        cardButton.setText(currentCard.getFront());
    }

    public void showBackOfCard(Card currentCard) {
        cardButton.setText(currentCard.getBack());
    }

    /* ====================================================================== */
    /*                              Click handlers                            */
    /* ====================================================================== */

    @FXML
    private void handlePreviousCardClicked() {
        listener.previousCardClicked();
    }

    @FXML
    private void handleNextCardClicked() {
        listener.nextCardClicked();
    }

    @FXML
    private void onCardClicked() {
        listener.cardClicked();
    }

    /* ====================================================================== */
    /*                              Hover handlers                            */
    /* ====================================================================== */

    @FXML
    private void handlePreviousButtonEnter() {

    }

    @FXML
    private void handlePreviousButtonExited() {

    }

    @FXML
    private void handleNextButtonEnter() {

    }

    @FXML
    private void handleNextButtonExited() {

    }

    /* ====================================================================== */
    /*                          Listener interface                            */
    /* ====================================================================== */

    public interface Listener {
        void cardClicked();
        void nextCardClicked();
        void previousCardClicked();
    }
}
