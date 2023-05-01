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
    public Label avgTimeLabel;

    @FXML
    public Label totalTimeLabel;


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

    public void setTotalTime(double totalTime) {
        totalTimeLabel.setText(totalTime + "s");
    }

    public void setAverageTime(double averageTime) {
        avgTimeLabel.setText(averageTime + "s");
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
