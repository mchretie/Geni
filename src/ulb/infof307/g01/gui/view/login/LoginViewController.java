package ulb.infof307.g01.gui.view.login;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class LoginViewController {

    @FXML
    private BorderPane BIGPAIN;

    @FXML
    private Pane loginPane;

    @FXML
    private TextField loginUsernameField;

    @FXML
    private PasswordField loginPasswordField;

    @FXML
    private Button loginButton;

    @FXML
    private Pane signupPane;

    @FXML
    private TextField signupUsernameField;

    @FXML
    private PasswordField signupPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button signupButton;

    protected ViewListener listener;

    /* ====================================================================== */
    /*                                Setters                                 */
    /* ====================================================================== */

    public void setListener(ViewListener listener) {
        this.listener = listener;
    }

    public void setSignupPaneVisible() {
        BIGPAIN.setCenter(signupPane);
        //this.loginPane.setVisible(false);
        //this.loginPane.toBack();
        //this.signupPane.toFront();
        //this.signupPane.setVisible(true);
    }

    public void setLoginPaneVisible() {
        BIGPAIN.setCenter(loginPane);
        //this.loginPane.setVisible(true);
        //this.signupPane.setVisible(false);
        //this.loginPane.toFront();
        //this.signupPane.toBack();

    }

    /* ====================================================================== */
    /*                             Click handlers                             */
    /* ====================================================================== */

    @FXML
    private void handleLoginClicked() {
        String password = this.loginPasswordField.getText();
        String username = this.loginUsernameField.getText();

        this.loginPasswordField.clear();
        this.loginUsernameField.clear();
        listener.loginClicked(username, password);
    }

    @FXML
    private void handleSignupClicked() {
        String username = this.signupUsernameField.getText();
        String password = this.signupPasswordField.getText();
        String confirmPassword = this.confirmPasswordField.getText();



        this.signupUsernameField.clear();
        this.signupPasswordField.clear();
        this.confirmPasswordField.clear();

        listener.signupClicked(username, password, confirmPassword);
    }

    @FXML
    private void handleToLoginClicked() {
        setLoginPaneVisible();
    }

    @FXML
    private void handleToSignupClicked() { setSignupPaneVisible(); }

    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface ViewListener {

        void loginClicked(String username, String password);

        void signupClicked(String username, String password, String confirmPassword);

       // void toSignupClicked();
        //void toLoginClicked();
    }
}
