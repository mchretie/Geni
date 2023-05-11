package ulb.infof307.g01.gui.view.leaderboard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

import java.util.List;

public class GlobalLeaderboardViewController {
    public BorderPane borderPane;
    /* ====================================================================== */
    /*                              FXML Attributes                           */
    /* ====================================================================== */
    @FXML
    private Label usernameLabel;
    @FXML
    private Label rankLabel;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label numberDecksLabel;
    @FXML
    private ListView<Node> boardContainer;


    /* ====================================================================== */
    /*                              Leaderboard                               */
    /* ====================================================================== */
    public void setBoard(List<Node> playersScoreItem) {
        ObservableList<Node> items = FXCollections.observableArrayList(playersScoreItem);
        boardContainer.setItems(items);
        boardContainer.refresh();

    }

    public void setPersonalInformation(String username, String rang, String score, String numberDecks) {
        usernameLabel.setText(username);
        rankLabel.setText(rang);
        scoreLabel.setText(score);
        numberDecksLabel.setText(numberDecks);
    }
}
