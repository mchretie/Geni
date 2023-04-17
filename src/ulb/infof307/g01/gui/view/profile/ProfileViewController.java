package ulb.infof307.g01.gui.view.profile;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProfileViewController {

    @FXML
    private Label usernameLabel;

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

    public void setUsernameDisplay(String username) {
        usernameLabel.setText(username);
    }

    /* ====================================================================== */
    /*                             Click handlers                             */
    /* ====================================================================== */

    @FXML
    private void handleLogoutButtonClicked() { listener.logoutButtonClicked(); }
    @FXML
    public void goToStatisticsClicked() { listener.statisticsClicked(); }

    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface Listener {
        void logoutButtonClicked();
        void statisticsClicked();
    }
}
