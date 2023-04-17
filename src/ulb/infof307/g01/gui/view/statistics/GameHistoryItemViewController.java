package ulb.infof307.g01.gui.view.statistics;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GameHistoryItemViewController {
    /* ====================================================================== */
    /*                              FXML Attributes                           */
    /* ====================================================================== */
    @FXML
    private Label dateLabel;
    @FXML
    private Label deckNameLabel;
    @FXML
    private Label scoreLabel;

    /* ====================================================================== */
    /*                                Setter                                  */
    /* ====================================================================== */
    public void setGameHistoryItem(String date, String deckName, String score) {
        dateLabel.setText(date);
        deckNameLabel.setText(deckName);
        scoreLabel.setText(score);
    }
}
