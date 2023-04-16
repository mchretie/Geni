package ulb.infof307.g01.gui.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;
import ulb.infof307.g01.gui.controller.errorhandler.ErrorHandler;
import ulb.infof307.g01.gui.httpdao.dao.DeckDAO;
import ulb.infof307.g01.gui.httpdao.dao.LeaderboardDAO;
import ulb.infof307.g01.gui.httpdao.dao.UserSessionDAO;
import ulb.infof307.g01.gui.view.leaderboard.LeaderboardViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.gui.view.leaderboard.PlayerScoreItemViewController;
import ulb.infof307.g01.model.GlobalLeaderboard;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GlobalLeaderboardController implements LeaderboardViewController.Listener, PlayerScoreItemViewController.Listener{
    private final Stage stage;
    private final MainWindowViewController mainWindowViewController;
    private final ControllerListener controllerListener;
    private final UserSessionDAO userSessionDAO;
    private final DeckDAO deckDAO;
    private final LeaderboardDAO leaderboardDAO;
    private final LeaderboardViewController leaderboardViewController;
    private final ErrorHandler errorHandler;

    /* ====================================================================== */
    /*                              Constructor                               */
    /* ====================================================================== */

    public GlobalLeaderboardController(Stage stage,
                                       MainWindowViewController mainWindowViewController,
                                       ErrorHandler errorHandler,
                                       ControllerListener controllerListener,
                                       UserSessionDAO userSessionDAO,
                                       DeckDAO deckDAO,
                                       LeaderboardDAO leaderboardDAO) {

        this.stage = stage;
        this.mainWindowViewController = mainWindowViewController;
        this.errorHandler = errorHandler;
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
        if (userSessionDAO.isLoggedIn()) {
            mainWindowViewController.setLeaderboardViewVisible();
            mainWindowViewController.makeGoBackIconInvisible();

            leaderboardViewController.setBoard(loadBoard());
        }

        else {
            mainWindowViewController.setGuestModeLeaderboardViewVisible();
        }

        stage.show();
    }

    private List<Node> loadBoard() {
        try {
            List<Node> playersScoreItem = new ArrayList<>();
            GlobalLeaderboard leaderboard = leaderboardDAO.getGlobalLeaderboard();
            String username = userSessionDAO.getUsername();

            leaderboardViewController
                    .setPersonalInformation(
                            username,
                            leaderboard.getUserScore(username),
                            leaderboard.getUserRank(username),
                            deckDAO.getAllDecksMetadata().size() + "");

            for (Map<String, String> entry : leaderboard) {
                Node node = loadEntry(leaderboard, entry);
                playersScoreItem.add(node);
            }

            return playersScoreItem;

        } catch (IOException | InterruptedException e) {
            errorHandler.failedLoading(e);
            return new ArrayList<>();
        }
    }

    private Node loadEntry(GlobalLeaderboard leaderboard, Map<String, String> entry) throws IOException {
        URL url = PlayerScoreItemViewController
                        .class.getResource("PlayerScoreItemView.fxml");

        FXMLLoader loader = new FXMLLoader(url);
        Node node = loader.load();

        PlayerScoreItemViewController playerScoreItemViewController
                = loader.getController();

        playerScoreItemViewController.setListener(this);

        String entryUsername = entry.get("username");
        playerScoreItemViewController
                .setPlayerScoreItem(
                    entryUsername,
                    leaderboard.getUserRank(entryUsername),
                    leaderboard.getUserScore(entryUsername));

        return node;
    }

    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void leaderboardClicked();
    }
}
