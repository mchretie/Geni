package ulb.infof307.g01.gui.view.statistics;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StatisticsViewController {

    /* ====================================================================== */
    /*                              FXML Attributes                           */
    /* ====================================================================== */
    @FXML
    private Label totalGamesPlayedLabel;
    @FXML
    private Label gamesPlayedTodayLabel;
    @FXML
    private Label totalDecksLabel;
    @FXML
    private Label totalScoreLabel;

    /* ====================================================================== */
    /*                                Listener                                */
    /* ====================================================================== */
    private Listener listener;


    /* ====================================================================== */
    /*                                Setters                                 */
    /* ====================================================================== */

    public void setListener(Listener listener) {
        this.listener = listener;
    }


    /* ====================================================================== */
    /*                               Statistics                               */
    /* ====================================================================== */

    void setMainStatistics(String totalGamesPlayed, String gamesPlayedToday, String totalDecks, String totalScore) {
        totalGamesPlayedLabel.setText(totalGamesPlayed);
        gamesPlayedTodayLabel.setText(gamesPlayedToday);
        totalDecksLabel.setText(totalDecks);
        totalScoreLabel.setText(totalScore);
    }


    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface Listener {

    }
}
