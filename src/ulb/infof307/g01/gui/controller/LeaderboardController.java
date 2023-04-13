package ulb.infof307.g01.gui.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;
import ulb.infof307.g01.gui.util.ImageLoader;
import ulb.infof307.g01.gui.view.leaderboard.LeaderboardViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.gui.view.leaderboard.PlayerScoreItemViewController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardController implements LeaderboardViewController.Listener, PlayerScoreItemViewController.Listener{
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

        leaderboardViewController.setBoard(loadBoard());

        stage.show();
    }

    private List<Node> loadBoard() {
        try {
            List<Node> PlayersScoreItem = new ArrayList<>();
            //TODO Display depending of the backend
            for (int i = 0; i < 10; i++) {

                URL url = PlayerScoreItemViewController.class.getResource("PlayerScoreItemView.fxml");
                FXMLLoader loader = new FXMLLoader(url);

                Node node = loader.load();
                PlayerScoreItemViewController playerScoreItemViewController = loader.getController();
                playerScoreItemViewController.setListener(this);
                playerScoreItemViewController.setPlayerScoreItem();

                PlayersScoreItem.add(node);
            }

            return PlayersScoreItem;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void leaderboardClicked();
    }
}
