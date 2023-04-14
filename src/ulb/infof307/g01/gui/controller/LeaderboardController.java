package ulb.infof307.g01.gui.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;
import ulb.infof307.g01.gui.httpdao.dao.LeaderboardDAO;
import ulb.infof307.g01.gui.httpdao.dao.UserSessionDAO;
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
    private final UserSessionDAO userDAO;
    private final LeaderboardDAO leaderboardDAO;

    /* ====================================================================== */
    /*                              Constructor                               */
    /* ====================================================================== */

    public LeaderboardController(Stage stage, MainWindowViewController mainWindowViewController, ControllerListener controllerListener, UserSessionDAO userDAO, LeaderboardDAO leaderboardDAO) {
        this.stage = stage;
        this.controllerListener = controllerListener;
        this.mainWindowViewController = mainWindowViewController;

        this.leaderboardViewController = mainWindowViewController.getLeaderboardViewController();

        this.userDAO = userDAO;
        this.leaderboardDAO = leaderboardDAO;
        leaderboardViewController.setListener(this);

    }


    /* ====================================================================== */
    /*                         Stage Manipulation                             */
    /* ====================================================================== */

    public void show() throws IOException, InterruptedException {
        // TODO
        mainWindowViewController.setLeaderboardViewVisible();
        mainWindowViewController.makeGoBackIconInvisible();

        leaderboardViewController.setPersonalInformation(userDAO.getUsername(), leaderboardDAO.getScore(userDAO.getUsername()));
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
