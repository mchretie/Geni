package ulb.infof307.g01.gui.view.login;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginViewController implements Initializable {

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
    /*                              Initializer                               */
    /* ====================================================================== */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set the menu pane to be initially hidden
    /*menuPane.setTranslateX(200);

    // Set up the button to toggle the menu pane
    rootPane.setOnMouseClicked(event -> {
      if (event.getX() >= rootPane.getWidth() - 200) {
        // If the user clicks within the right 200 pixels of the window, toggle
        // the menu pane
        TranslateTransition transition =
            new TranslateTransition(Duration.millis(300), menuPane);
        if (menuPane.getTranslateX() != 0) {
          // If the menu pane is currently hidden, slide it in from the right
          transition.setToX(0);
        } else {
          // If the menu pane is currently visible, slide it out to the right
          transition.setToX(200);
        }
        transition.play();

      }
    }); */
        ;
    }

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
        System.out.println("handle Login clicked");
        // get information from the text fields
        String password = this.passwordField.getText();
        String username = this.usernameField.getText();

        System.out.println("received: " + password + " " + username);
        this.passwordField.clear();
        this.usernameField.clear();
        listener.loginClicked(username, password);
    }

    @FXML
    private void handleSignupClicked() {
        System.out.println("handle Signup clicked");
        // get information from the text fields
        String password = this.passwordField.getText();
        String username = this.usernameField.getText();

        System.out.println("received: " + password + " " + username);
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
