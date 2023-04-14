package ulb.infof307.g01.gui.view.result;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ResultViewController {

    /* ====================================================================== */
    /*                               FXML Attributes                          */
    /* ====================================================================== */
    @FXML
    private Label scoreLabel;

    @FXML
    private Button goToMenuButton;


    /* ====================================================================== */
    /*                                Listener                                */
    /* ====================================================================== */

    private Listener listener;


    /* ====================================================================== */
    /*                                   Setters                              */
    /* ====================================================================== */

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setScore(int score) {
        scoreLabel.setText(String.valueOf(score));
    }


    /* ====================================================================== */
    /*                              Click handlers                            */
    /* ====================================================================== */

    @FXML
    private void onGoToMenuClicked() {
        listener.goToMenuButtonClicked();
    }


    /* ====================================================================== */
    /*                          Listener interface                            */
    /* ====================================================================== */

    public interface Listener {
        void goToMenuButtonClicked();
    }
}
