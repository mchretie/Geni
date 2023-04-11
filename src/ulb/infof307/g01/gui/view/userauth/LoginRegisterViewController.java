package ulb.infof307.g01.gui.view.userauth;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

    public void handleLoginClicked() {
        listener.onLoginClicked(usernameInput.getText(), passwordInput.getText());
    }


    /* ====================================================================== */
    /*                             Listener Interface                         */
    /* ====================================================================== */

    public interface Listener {
        void onLoginClicked(String username, String password);
    }
}
