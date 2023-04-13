package ulb.infof307.g01.gui.controller;

import java.io.IOException;

import javafx.stage.Stage;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.gui.view.profile.ProfileViewController;

public class ProfileController implements ProfileViewController.ViewListener {

    private final Stage stage;

    private final MainWindowViewController mainWindowViewController;
    private final ProfileViewController profileViewController;
    private final ControllerListener controllerListener;

    /* ====================================================================== */
    /*                              Constructor                               */
    /* ====================================================================== */
    public ProfileController(
            Stage stage, MainWindowViewController mainWindowViewController,
            ProfileController.ControllerListener controllerListener) {

        this.stage = stage;
        this.mainWindowViewController = mainWindowViewController;
        this.controllerListener = controllerListener;

        this.profileViewController =
                mainWindowViewController.getProfileViewController();
        this.profileViewController.setListener(this);
    }

    /* ====================================================================== */
    /*                         Setter & Getters                               */
    /* ====================================================================== */

    public void setUserNameInProfile(String userName) {
        profileViewController.setUsernameDisplay(userName);
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
        mainWindowViewController.setProfileViewVisible();
        mainWindowViewController.makeGoBackIconVisible();
        stage.show();
    }

    /* ====================================================================== */
    /*                        View Listener Method                            */
    /* ====================================================================== */

    @Override
    public void logoutButtonClicked() {
        controllerListener.handleLogout();
    }

    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void handleLogout();
    }
}
