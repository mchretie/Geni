package ulb.infof307.g01.gui.controller;

import javafx.stage.Stage;
import ulb.infof307.g01.gui.util.ImageLoader;
import ulb.infof307.g01.gui.view.leaderboard.LeaderboardViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;

import java.io.IOException;

public class LeaderboardController implements LeaderboardViewController.Listener{
    private final Stage stage;
    private final ControllerListener controllerListener;
    private final LeaderboardViewController leaderboardViewController;
    private final MainWindowViewController mainWindowViewController;
    private final ImageLoader imageLoader = new ImageLoader();

    /* ====================================================================== */
    /*                              Constructor                               */
    /* ====================================================================== */

    public LeaderboardController(Stage stage, MainWindowViewController mainWindowViewController, ControllerListener controllerListener) {
        this.stage = stage;
        this.controllerListener = controllerListener;
        this.mainWindowViewController = mainWindowViewController;

        this.leaderboardViewController = mainWindowViewController.getLeaderboardViewController();

        leaderboardViewController.setListener(this);

    }


    /* ====================================================================== */
    /*                         Stage Manipulation                             */
    /* ====================================================================== */

    public void show() throws IOException, InterruptedException {
        // TODO
        System.out.println("LeaderboardController.show()");
        mainWindowViewController.setLeaderboardViewVisible();
        mainWindowViewController.makeGoBackIconInvisible();

        stage.show();
    }

    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void leaderboardClicked();
    }
}
