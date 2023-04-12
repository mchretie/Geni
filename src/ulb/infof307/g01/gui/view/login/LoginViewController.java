package ulb.infof307.g01.gui.view.login;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;

public class LoginViewController {

    @FXML
    private StackPane BIGPAIN;

    @FXML
    private VBox loginVBOX;

    @FXML
    private TextField loginUsernameField;

    @FXML
    private PasswordField loginPasswordField;

    @FXML
    private Button loginButton;

    @FXML
    private VBox signupVBOX;

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
        //BIGPAIN.setCenter(signupPane);
        this.loginVBOX.setVisible(false);
        //this.loginPane.toBack();
        //this.signupVBOX.toFront();
        this.signupVBOX.setVisible(true);
    }

    public void setLoginPaneVisible() {
        //BIGPAIN.setCenter(loginPane);
        this.loginVBOX.setVisible(true);
        this.signupVBOX.setVisible(false);
        //this.loginVBOX.toFront();
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
