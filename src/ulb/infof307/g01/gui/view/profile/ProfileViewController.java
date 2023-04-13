package ulb.infof307.g01.gui.view.profile;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ProfileViewController {

    @FXML
    private Label usernameLabel;

    @FXML
    private Button logoutButton;

    /* ====================================================================== */
    /*                                Listener                                */
    /* ====================================================================== */

    private ViewListener listener;


    /* ====================================================================== */
    /*                                Setters                                 */
    /* ====================================================================== */

    public void setListener(ViewListener listener) {
        this.listener = listener;
    }

    public void setUsernameDisplay(String username) {
        usernameLabel.setText(username);
    }

    /* ====================================================================== */
    /*                             Click handlers                             */
    /* ====================================================================== */

    @FXML
    private void handleLogoutButtonClicked() {
        listener.logoutButtonClicked();
    }

    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface ViewListener {
        void logoutButtonClicked();

    }
}
