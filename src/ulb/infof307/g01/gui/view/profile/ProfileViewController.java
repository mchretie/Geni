package ulb.infof307.g01.gui.view.profile;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ulb.infof307.g01.gui.view.login.LoginViewController;

public class ProfileViewController implements Initializable {

    @FXML
    private Label usernameLabel;

    @FXML
    private Button logoutButton;

    protected ViewListener listener;

    /* ====================================================================== */
    /*                              Initializer                               */
    /* ====================================================================== */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ;
    }

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
        // ICI
        void logoutButtonClicked();

        //void setUsernameDisplay(String username);
    }
}
