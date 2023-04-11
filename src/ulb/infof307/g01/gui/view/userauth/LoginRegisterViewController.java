package ulb.infof307.g01.gui.view.userauth;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

public class LoginRegisterViewController {

    /* ====================================================================== */
    /*                               FXML Attributes                          */
    /* ====================================================================== */

    @FXML
    private HBox mainHbox;

    @FXML
    private TextField usernameInput;

    @FXML
    private PasswordField passwordInput;

    /* ====================================================================== */
    /*                                  Listener                              */
    /* ====================================================================== */

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private void clearTextFields() {
        passwordInput.clear();
        usernameInput.clear();
    }


    /* ====================================================================== */
    /*                               Button clicks                            */
    /* ====================================================================== */

    @FXML
    private void handleLoginClicked() {
        listener.onLoginClicked(usernameInput.getText(), passwordInput.getText());

        clearTextFields();
    }


    /* ====================================================================== */
    /*                               Key presses                             */
    /* ====================================================================== */

    @FXML
    private void handleLoginKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER)
            passwordInput.requestFocus();
    }

    @FXML
    private void handlePasswordKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() != KeyCode.ENTER)
            return;

        listener.onLoginClicked(usernameInput.getText(), passwordInput.getText());
        clearTextFields();
    }

    /* ====================================================================== */
    /*                             Listener Interface                         */
    /* ====================================================================== */

    public interface Listener {
        void onLoginClicked(String username, String password);
    }
}
