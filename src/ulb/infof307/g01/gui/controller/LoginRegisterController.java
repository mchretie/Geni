package ulb.infof307.g01.gui.controller;

import javafx.stage.Stage;
import ulb.infof307.g01.gui.httpdao.dao.UserDAO;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.gui.view.userauth.LoginRegisterViewController;

import java.io.IOException;

public class LoginRegisterController implements LoginRegisterViewController.Listener {
    private final Stage stage;
    private final ControllerListener controllerListener;
    private final MainWindowViewController mainWindowViewController;
    private final UserDAO userDAO;

    public LoginRegisterController(Stage stage, ControllerListener controllerListener,
                                   MainWindowViewController mainWindowViewController, UserDAO userDAO) {
        this.stage = stage;
        this.controllerListener = controllerListener;
        this.mainWindowViewController = mainWindowViewController;
        this.userDAO = userDAO;
    }

    public void show() {
        mainWindowViewController.makeGoBackIconVisible();
        mainWindowViewController.setLoginRegisterViewVisible();

        LoginRegisterViewController controller
                = mainWindowViewController.getLoginRegisterViewController();

        controller.setListener(this);

        stage.show();
    }

    @Override
    public void onLoginClicked(String username, String password) {
        try {
            userDAO.login(username, password);
            controllerListener.logInSucceeded();
            
        } catch (IOException | InterruptedException e) {
            controllerListener.logInFailed(e);
        }
    }

    public interface ControllerListener {
        void logInFailed(Exception e);
        void logInSucceeded();
    }
}
