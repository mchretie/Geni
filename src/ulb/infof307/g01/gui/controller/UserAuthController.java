package ulb.infof307.g01.gui.controller;

import javafx.stage.Stage;
import ulb.infof307.g01.gui.controller.errorhandler.ErrorHandler;
import ulb.infof307.g01.gui.httpdao.dao.UserSessionDAO;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.gui.view.userauth.UserAuthViewController;

import java.io.IOException;

public class UserAuthController implements UserAuthViewController.Listener {
    private final Stage stage;

    private final ErrorHandler errorHandler;

    private final MainWindowViewController mainWindowViewController;
    private final UserAuthViewController userAuthViewController;
    private final ControllerListener controllerListener;

    private final UserSessionDAO userSessionDAO;


    /* ====================================================================== */
    /*                              Constructor                               */
    /* ====================================================================== */

    public UserAuthController(Stage stage,
                                ErrorHandler errorHandler,
                                MainWindowViewController mainWindowViewController,
                                ControllerListener controllerListener,
                                UserSessionDAO userSessionDAO) {

        this.stage = stage;

        this.errorHandler = errorHandler;

        this.mainWindowViewController = mainWindowViewController;
        this.controllerListener = controllerListener;
        this.userSessionDAO = userSessionDAO;

        this.userAuthViewController =
                mainWindowViewController.getUserAuthViewController();

        this.userAuthViewController.setListener(this);
    }

    /* ====================================================================== */
    /*                         Stage Manipulation                             */
    /* ====================================================================== */

    /**
     * Loads and displays the Deck Menu onto the main scene
     *
     */
    public void show() {
        mainWindowViewController.setUserAuthViewController();
        mainWindowViewController.makeGoBackIconVisible();
        userAuthViewController.setLoginPaneVisible();
        stage.show();
    }

    @Override
    public void loginClicked(String username, String password) {
        if (username.isEmpty() || password.isEmpty())
            return;

        try {
            userSessionDAO.login(username, password);
            controllerListener.handleLogin(username, password);

        } catch (IOException | InterruptedException e) {
            errorHandler.failedLogin(e);
        }
    }

    @Override
    public void registerClicked(String username, String password, String confirmPassword) {
        if (!areCredentialsValid(username, password, confirmPassword)) {
            return;
        }

        try {
            userSessionDAO.register(username, password);
            loginClicked(username, password);

        } catch (IOException | InterruptedException e) {
            errorHandler.failedRegister(e);
        }
    }

    private boolean areCredentialsValid(String username, String password, String confirmPassword) {
        return !username.isEmpty()
                && !password.isEmpty()
                && password.equals(confirmPassword);
    }


    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void handleLogin(String username, String password);
    }
}
