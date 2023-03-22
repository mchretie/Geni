package ulb.infof307.g01.view.playdeck;

import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import ulb.infof307.g01.model.Card;

public class PlayDeckViewController {

    @FXML
    private Label deckNameLabel;

    @FXML
    private Button cardButton;

    private Listener listener;

    /* ====================================================================== */
    /*                                 Setters                                */
    /* ====================================================================== */

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setDeckName(String deckName) { this.deckNameLabel.setText(deckName); }

    public void showCardContent(String content) {
        cardButton.setText(content);
    }


    /* ====================================================================== */
    /*                                 Getters                                */
    /* ====================================================================== */

    public String getCurrentContent() { return cardButton.getText(); }


    /* ====================================================================== */
    /*                                Animation                               */
    /* ====================================================================== */

    public void flipCard(String contentToShow) {
        RotateTransition rotateTransition
                = new RotateTransition(Duration.millis(300), cardButton);

        rotateTransition.setAxis(Rotate.Y_AXIS);
        rotateTransition.setFromAngle(0);
        rotateTransition.setToAngle(90);

        rotateTransition.setOnFinished(e -> {
            if (getCurrentContent().equals(contentToShow))
                return;

            showCardContent(contentToShow);
            rotateTransition.setFromAngle(90);
            rotateTransition.setToAngle(0);
            rotateTransition.play();
        });

        rotateTransition.play();
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
