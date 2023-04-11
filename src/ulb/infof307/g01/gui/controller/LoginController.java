package ulb.infof307.g01.gui.controller;

import java.io.IOException;

import javafx.stage.Stage;
import ulb.infof307.g01.gui.httpdao.dao.UserDAO;
import ulb.infof307.g01.gui.view.login.LoginViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;

public class LoginController implements LoginViewController.ViewListener {

    private final Stage stage;

    private final MainWindowViewController mainWindowViewController;

    private final LoginViewController loginViewController;

    private final ControllerListener controllerListener;

    private final UserDAO userDAO = new UserDAO();

    /* ====================================================================== */
    /*                              Constructor                               */
    /* ====================================================================== */

    public LoginController(Stage stage,
                           MainWindowViewController mainWindowViewController,
                           ControllerListener controllerListener) {

        this.stage = stage;
        this.mainWindowViewController = mainWindowViewController;
        this.controllerListener = controllerListener;

        this.loginViewController =
                mainWindowViewController.getLoginViewController();

        this.loginViewController.setListener(this);
    }

    /* ====================================================================== */
    /*                         Stage Manipulation                             */
    /* ====================================================================== */

    /**
     * Loads and displays the Deck Menu onto the main scene
     *
     * @throws IOException if FXMLLoader.load() fails
     */
    public void show() throws IOException {
        mainWindowViewController.setLoginViewVisible();
        mainWindowViewController.makeGoBackIconVisible();
        stage.show();
    }

    /* ====================================================================== */
    /*                        View Listener Method                            */
    /* ====================================================================== */

    @Override
    public void loginClicked(String username, String password) {

        if (credentialsNOTValid(username, password)) {
            //Todo : Error message handling
            return;
        }
        try {
            System.out.println("try Logging in with username: " + username + " and password: " + password);
            userDAO.login(username, password);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        controllerListener.handleLogin(username, password);
    }

    @Override
    public void signupClicked(String username, String password) {

        if (credentialsNOTValid(username, password)) {
            //Todo : Error message handling
            return;
        }
        try {
            System.out.println("try Signing up with username: " + username + " and password: " + password);
            userDAO.register(username, password);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Signup successful. Loggin in...");
        loginClicked(username, password);
    }

    private boolean credentialsNOTValid(String username, String password) {
        // Todo :  basic test for now
        return username.length() == 0 && password.length() == 0;
    }

    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void handleLogin(String username, String password);
    }
}
