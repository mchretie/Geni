package ulb.infof307.g01.gui.view.profile;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

public class ProfileViewController {

    @FXML
    private Label usernameLabel;
    @FXML
    private FontIcon statisticsIcon;

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
    /*                              Hover handlers                            */
    /* ====================================================================== */

    public void handleStatisticsHover() { statisticsIcon.setIconColor(Color.web("#FFFFFF")); }
    public void handleStatisticsExitHover() { statisticsIcon.setIconColor(Color.web("#000000")); }

    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface Listener {
        void logoutButtonClicked();
        void statisticsClicked();
    }
}
