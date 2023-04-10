package ulb.infof307.g01.gui.view.login;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginViewController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button signupButton;

    protected ViewListener listener;

    /* ====================================================================== */
    /*                                Setters                                 */
    /* ====================================================================== */

    public void setListener(ViewListener listener) {
        this.listener = listener;
    }

    /* ====================================================================== */
    /*                             Click handlers                             */
    /* ====================================================================== */

    @FXML
    private void handleLoginClicked() {
        String password = this.passwordField.getText();
        String username = this.usernameField.getText();

        this.passwordField.clear();
        this.usernameField.clear();
        listener.loginClicked(username, password);
    }

    @FXML
    private void handleSignupClicked() {
        String password = this.passwordField.getText();
        String username = this.usernameField.getText();

        this.passwordField.clear();
        this.usernameField.clear();
        listener.signupClicked(username, password);
    }

    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface ViewListener {
        // ICI
        void loginClicked(String username, String password);

        void signupClicked(String username, String password);
    }
}
