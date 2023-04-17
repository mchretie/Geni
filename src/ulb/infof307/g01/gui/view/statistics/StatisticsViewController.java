package ulb.infof307.g01.gui.view.statistics;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.util.List;

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
    @FXML
    private ListView<Node> gameHistoryContainer;

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

    public void setMainStatistics(String totalGamesPlayed, String gamesPlayedToday, String totalDecks, String totalScore) {
        totalGamesPlayedLabel.setText(totalGamesPlayed);
        gamesPlayedTodayLabel.setText(gamesPlayedToday);
        totalDecksLabel.setText(totalDecks);
        totalScoreLabel.setText(totalScore);
    }


    /* ====================================================================== */
    /*                              Game History                              */
    /* ====================================================================== */
    public void setGameHistory(List<Node> gameHistoryItem) {
        ObservableList<Node> items = FXCollections.observableArrayList(gameHistoryItem);
        gameHistoryContainer.setItems(items);
        gameHistoryContainer.refresh();
    }


    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface Listener {

    }
}
