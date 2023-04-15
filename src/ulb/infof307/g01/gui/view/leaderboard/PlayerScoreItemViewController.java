package ulb.infof307.g01.gui.view.leaderboard;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PlayerScoreItemViewController {
    /* ====================================================================== */
    /*                              FXML Attributes                           */
    /* ====================================================================== */
    @FXML
    private Label usernameLabel;
    @FXML
    private Label scoreLabel;


    /* ====================================================================== */
    /*                                Listener                                */
    /* ====================================================================== */

    private Listener listener;


    /* ====================================================================== */
    /*                                Setter                                  */
    /* ====================================================================== */

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setPlayerScoreItem(String username, String score) {
        usernameLabel.setText(username);
        scoreLabel.setText(score);
    }


    /* ====================================================================== */
    /*                              Listener                                  */
    /* ====================================================================== */

    public interface Listener {

    }
}
