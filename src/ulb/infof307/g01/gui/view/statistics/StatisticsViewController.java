package ulb.infof307.g01.gui.view.statistics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

import java.util.List;

public class StatisticsViewController {

    /* ====================================================================== */
    /*                              FXML Attributes                           */
    /* ====================================================================== */
    @FXML
    private BorderPane borderPane;
    @FXML
    private Label totalGamesPlayedLabel;
    @FXML
    private Label totalDecksLabel;
    @FXML
    private Label totalScoreLabel;
    @FXML
    private ListView<Node> gameHistoryContainer;


    /* ====================================================================== */
    /*                               Statistics                               */
    /* ====================================================================== */

    public void setMainStatistics(String totalGamesPlayed, String totalDecks, String totalScore) {
        totalGamesPlayedLabel.setText(totalGamesPlayed);
        totalDecksLabel.setText(totalDecks);
        totalScoreLabel.setText(totalScore);
    }


    /* ====================================================================== */
    /*                              Game History                              */
    /* ====================================================================== */

    public void setGameHistory(List<Node> gameHistoryItem) {

        ObservableList<Node> items = FXCollections.observableArrayList(gameHistoryItem);
        gameHistoryContainer.setItems(items);

        borderPane.requestFocus();

        gameHistoryContainer.refresh();
    }


    /* ====================================================================== */
    /*                              Click Handlers                            */
    /* ====================================================================== */

    @FXML
    private void gameHistoryContainerClicked() {
        gameHistoryContainer.getSelectionModel().clearSelection();
        borderPane.requestFocus();
    }

}
