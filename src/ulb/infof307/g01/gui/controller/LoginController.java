package ulb.infof307.g01.gui.controller;

import java.io.IOException;

import javafx.stage.Stage;
import ulb.infof307.g01.gui.httpdao.dao.UserSessionDAO;
import ulb.infof307.g01.gui.view.login.LoginViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;

public class LoginController implements LoginViewController.Listener {

    private final Stage stage;

    private final MainWindowViewController mainWindowViewController;
    private final LoginViewController loginViewController;
    private final ControllerListener controllerListener;

    private final UserSessionDAO userSessionDAO;

    /* ====================================================================== */
    /*                              Constructor                               */
    /* ====================================================================== */

    public LoginController(Stage stage,
                           MainWindowViewController mainWindowViewController,
                           ControllerListener controllerListener,
                           UserSessionDAO userSessionDAO) {

        this.stage = stage;
        this.mainWindowViewController = mainWindowViewController;
        this.controllerListener = controllerListener;
        this.userSessionDAO = userSessionDAO;

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

        loginViewController.setLoginPaneVisible();
        stage.show();
    }

    /* ====================================================================== */
    /*                        View Listener Method                            */
    /* ====================================================================== */

    @Override
    public void loginClicked(String username, String password) {

        if (username.isEmpty() || password.isEmpty())
            return;

        try {
            userSessionDAO.login(username, password);
            controllerListener.handleLogin(username, password);

        } catch (IOException | InterruptedException e) {
            controllerListener.failedLogin(e);
        }
    }

    @Override
    public void registerClicked(String username, String password, String confirmPassword) {

        if (areCredentialsInvalid(username, password, confirmPassword)) {
            return;
        }

        try {
            userSessionDAO.register(username, password);
            loginClicked(username, password);

        } catch (IOException | InterruptedException e) {
            controllerListener.failedRegister(e);
        }
    }

    private boolean areCredentialsInvalid(String username, String password, String confirmPassword) {
        return username.isEmpty()
                || password.isEmpty()
                || !password.equals(confirmPassword);
    }

    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void handleLogin(String username, String password);
        void failedRegister(Exception e);
        void failedLogin(Exception e);
    }
}
