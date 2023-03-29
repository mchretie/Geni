package ulb.infof307.g01.gui.controller;

import javafx.stage.Stage;
import ulb.infof307.g01.gui.httpclient.dao.DeckDAO;
import ulb.infof307.g01.gui.view.profile.ProfileViewController;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;
import ulb.infof307.g01.gui.view.editdeck.EditDeckViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;

import java.io.IOException;

public class ProfileController implements ProfileViewController.Listener {

    private final Stage stage;


    private final MainWindowViewController mainWindowViewController;
    private final ProfileViewController profileViewController;


    private final ControllerListener controllerListener;

    /* ====================================================================== */
    /*                              Constructor                               */
    /* ====================================================================== */

    public ProfileController(Stage stage,
                              MainWindowViewController mainWindowViewController,
                              ControllerListener controllerListener) {

        this.stage = stage;
        this.mainWindowViewController = mainWindowViewController;
        this.controllerListener = controllerListener;

        this.profileViewController
                = mainWindowViewController.getProfileViewController();

        profileViewController.setListener(this);
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
        // Todo :  tests

        stage.show();
    }


    /* ====================================================================== */
    /*                        View Listener Method                            */
    /* ====================================================================== */

    @Override
    public void profileClicked() {
        controllerListener.profileClicked();
    }

    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void profileClicked();
    }
}
