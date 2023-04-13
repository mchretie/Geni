package ulb.infof307.g01.gui.view.leaderboard;

import javafx.scene.layout.BorderPane;
import ulb.infof307.g01.gui.view.deckmenu.DeckMenuViewController;

public class LeaderboardViewController {
    public BorderPane borderPane;
    /* ====================================================================== */
    /*                              FXML Attributes                           */
    /* ====================================================================== */


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
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface Listener {

    }
}
