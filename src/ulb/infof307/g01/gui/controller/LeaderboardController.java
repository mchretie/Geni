package ulb.infof307.g01.gui.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;
import ulb.infof307.g01.gui.httpdao.dao.DeckDAO;
import ulb.infof307.g01.gui.httpdao.dao.LeaderboardDAO;
import ulb.infof307.g01.gui.httpdao.dao.UserSessionDAO;
import ulb.infof307.g01.gui.view.leaderboard.LeaderboardViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.gui.view.leaderboard.PlayerScoreItemViewController;
import ulb.infof307.g01.model.Score;
import ulb.infof307.g01.model.User;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardController implements LeaderboardViewController.Listener, PlayerScoreItemViewController.Listener{
    private final Stage stage;
    private final MainWindowViewController mainWindowViewController;
    private final ControllerListener controllerListener;
    private final UserSessionDAO userSessionDAO;
    private final DeckDAO deckDAO;
    private final LeaderboardDAO leaderboardDAO;
    private final LeaderboardViewController leaderboardViewController;




    /* ====================================================================== */
    /*                              Constructor                               */
    /* ====================================================================== */

    public LeaderboardController(Stage stage,
                                 MainWindowViewController mainWindowViewController,
                                 ControllerListener controllerListener,
                                 UserSessionDAO userSessionDAO,
                                 DeckDAO deckDAO,
                                 LeaderboardDAO leaderboardDAO) {
        this.stage = stage;
        this.mainWindowViewController = mainWindowViewController;
        this.controllerListener = controllerListener;
        this.userSessionDAO = userSessionDAO;
        this.deckDAO = deckDAO;
        this.leaderboardDAO = leaderboardDAO;

        this.leaderboardViewController = mainWindowViewController.getLeaderboardViewController();
        leaderboardViewController.setListener(this);

    }


    /* ====================================================================== */
    /*                         Stage Manipulation                             */
    /* ====================================================================== */

    public void show() throws IOException, InterruptedException {
        // TODO
        mainWindowViewController.setLeaderboardViewVisible();
        mainWindowViewController.makeGoBackIconInvisible();
        int bestScore = leaderboardDAO.getScore(userSessionDAO.getUserId());


        leaderboardViewController.setPersonalInformation(userSessionDAO.getUsername(),
                String.valueOf(leaderboardDAO.getRank(userSessionDAO.getUsername())),
                String.valueOf(bestScore),
                String.valueOf(deckDAO.getAllDecksMetadata().size()));
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
