package ulb.infof307.g01.gui.view.login;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

public class LoginViewController {

    @FXML
    private VBox loginVBOX;

    @FXML
    private TextField loginUsernameField;

    @FXML
    private PasswordField loginPasswordField;

    @FXML
    private VBox registerVBOX;

    @FXML
    private TextField registerUsernameField;

    @FXML
    private PasswordField registerPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button registerButton;

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

    public void setRegisterPaneVisible() {
        this.loginVBOX.setVisible(false);
        this.registerVBOX.setVisible(true);
    }

    public void setLoginPaneVisible() {
        this.loginVBOX.setVisible(true);
        this.registerVBOX.setVisible(false);
    }

    /*====================================================================== */
    /*                                Handlers                               */
    /*====================================================================== */

    private void handleLogin() {
        String password = this.loginPasswordField.getText();
        String username = this.loginUsernameField.getText();

        this.loginPasswordField.clear();
        this.loginUsernameField.clear();
        listener.loginClicked(username, password);
    }

    private void handleRegister() {
        String username =  this.registerUsernameField.getText();
        String password =  this.registerPasswordField.getText();
        String confirmPassword = this.confirmPasswordField.getText();

        this.registerUsernameField.clear();
        this.registerPasswordField.clear();
        this.confirmPasswordField.clear();

        listener.registerClicked(username, password, confirmPassword);
    }

    /* ====================================================================== */
    /*                             Click handlers                             */
    /* ====================================================================== */

    @FXML
    private void handleLoginButtonClicked() {
        handleLogin();
    }

    @FXML
    private void handleRegisterButtonClicked() {
        handleRegister();
    }

    @FXML
    private void handleToLoginClicked() {
        setLoginPaneVisible();
    }

    @FXML
    private void handleToRegisterClicked() { setRegisterPaneVisible(); }

    @FXML
    private void handleRegisterUsernameKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
            registerPasswordField.requestFocus();
    }

    @FXML
    private void handleRegisterPasswordKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
            confirmPasswordField.requestFocus();
    }

    @FXML
    private void handleRegisterConfPasswordActionEvent(KeyEvent actionEvent) {
        System.out.println("handleRegisterConfPasswordActionEvent " + actionEvent.getCharacter());
        if (!registerPasswordField.getText().equals(confirmPasswordField.getText())) {
            registerPasswordField.setStyle("-fx-border-color: red");
            confirmPasswordField.setStyle("-fx-border-color: red");
            registerButton.setDisable(true);
        }
        else {
            registerPasswordField.setStyle("-fx-border-color: green");
            confirmPasswordField.setStyle("-fx-border-color: green");
            registerButton.setDisable(false);
            System.out.println("actionEvent.getCode() = " + actionEvent.getCode());
            if (actionEvent.getCode().equals(KeyCode.ENTER)){
                handleRegister();
            }
        }
    }

    @FXML
    private void handleLoginUsernameKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
            loginPasswordField.requestFocus();
    }

    @FXML
    private void handleLoginPasswordKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER))
            handleLogin();
    }


    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface Listener {
        void loginClicked(String username, String password);
        void registerClicked(String username, String password, String confirmPassword);
    }
}
