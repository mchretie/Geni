package ulb.infof307.g01.gui.controller;

import javafx.stage.Stage;
import ulb.infof307.g01.gui.http.ServerCommunicator;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.gui.view.profile.ProfileViewController;

public class ProfileController implements ProfileViewController.Listener {

    private final Stage stage;

    private final MainWindowViewController mainWindowViewController;
    private final ProfileViewController profileViewController;
    private final ControllerListener controllerListener;
    private final ServerCommunicator serverCommunicator;


    /* ====================================================================== */
    /*                              Constructor                               */
    /* ====================================================================== */

    public ProfileController(Stage stage,
                             MainWindowViewController mainWindowViewController,
                             ControllerListener controllerListener,
                             ServerCommunicator serverCommunicator) {

        this.stage = stage;
        this.mainWindowViewController = mainWindowViewController;
        this.controllerListener = controllerListener;

        this.serverCommunicator = serverCommunicator;

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
     */
    public void show() {
        setUserNameInProfile(serverCommunicator.getSessionUsername());
        mainWindowViewController.setProfileViewVisible();
        mainWindowViewController.makeGoBackIconVisible();
        stage.show();
    }

    /* ====================================================================== */
    /*                        View Listener Method                            */
    /* ====================================================================== */

    @Override
    public void logoutButtonClicked() {
        serverCommunicator.userLogout();
        controllerListener.userLoggedOut();
    }

    @Override
    public void statisticsClicked() { controllerListener.statisticsClicked();}

    public interface ControllerListener {
        void userLoggedOut();
        void statisticsClicked();
    }
}
