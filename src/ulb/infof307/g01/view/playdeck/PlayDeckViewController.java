package ulb.infof307.g01.view.playdeck;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;

import java.util.List;

public class PlayDeckViewController {
    @FXML
    public Button neverSeen;
    public Button veryBad;
    public Button bad;
    public Button average;
    public Button good;
    public Button veryGood;
    public Button cardButton;
    public Button nextCardButton;
    public Button previousCardButton;

    @FXML
    public FontIcon previousCardIcon;
    public FontIcon nextCardIcon;

    @FXML
    public HBox cardRatingBox;

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    /* ============================================================================================================== */
    /*                                                  Click handlers                                                */
    /* ============================================================================================================== */

    public void handlePreviousCardClicked() {
        listener.previousCardClicked();
    }

    public void handleNextCardClicked() {
        listener.nextCardClicked();
    }

    public void onCardClicked() {
        listener.cardClicked();
    }

    /* ============================================================================================================== */
    /*                                                  Hover handlers                                                */
    /* ============================================================================================================== */

    public void handlePreviousButtonEnter() {
    }

    public void handlePreviousButtonExited() {
    }

    public void handleNextButtonEnter() {
    }

    public void handleNextButtonExited() {
    }

    public interface Listener {
        void cardClicked();
        void nextCardClicked();
        void previousCardClicked();
    }
}
