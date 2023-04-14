package ulb.infof307.g01.gui.view.leaderboard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import ulb.infof307.g01.model.Score;

import java.util.List;

public class LeaderboardViewController {
    public BorderPane borderPane;
    /* ====================================================================== */
    /*                              FXML Attributes                           */
    /* ====================================================================== */
    @FXML
    private Label usernameLabel;
    @FXML
    private ListView<Node> boardContainer;


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
    /*                              Leaderboard                               */
    /* ====================================================================== */
    public void setBoard(List<Node> PlayersScoreItem){
        //TODO add Players Score item to boardContainer
        ObservableList<Node> items = FXCollections.observableArrayList(PlayersScoreItem);
        boardContainer.setItems(items);
        boardContainer.refresh();

    }

    public void setPersonalInformation(String username, Score score) {
        //TODO set username and score in the view
        usernameLabel.setText(username);
    }

    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface Listener {

    }
}
