package ulb.infof307.g01.gui.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;
import ulb.infof307.g01.gui.controller.errorhandler.ErrorHandler;
import ulb.infof307.g01.gui.httpdao.dao.UserSessionDAO;
import ulb.infof307.g01.gui.httpdao.dao.GameHistoryDAO;
import ulb.infof307.g01.gui.view.statistics.GameHistoryItemViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.gui.view.statistics.StatisticsViewController;
import ulb.infof307.g01.model.GameHistory;
import ulb.infof307.g01.model.Statistics;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatisticsController implements StatisticsViewController.Listener {
    private final Stage stage;
    private final ErrorHandler errorHandler;
    private final MainWindowViewController mainWindowViewController;
    private final ControllerListener controllerListener;
    private final UserSessionDAO userSessionDAO;
    private final GameHistoryDAO gameHistoryDAO;

    private final StatisticsViewController statisticsViewController;

    private GameHistory gameHistory;

    /* ====================================================================== */
    /*                              Constructor                               */
    /* ====================================================================== */

    public StatisticsController(Stage stage,
                                ErrorHandler errorHandler,
                                MainWindowViewController mainWindowViewController,
                                ControllerListener controllerListener,
                                UserSessionDAO userSessionDAO,
                                GameHistoryDAO gameHistoryDAO) {
        this.stage = stage;
        this.errorHandler = errorHandler;
        this.mainWindowViewController = mainWindowViewController;
        this.controllerListener = controllerListener;
        this.userSessionDAO = userSessionDAO;
        this.gameHistoryDAO = gameHistoryDAO;

        this.statisticsViewController = mainWindowViewController.getStatisticsViewController();
        statisticsViewController.setListener(this);
    }


    /* ====================================================================== */
    /*                         Stage Manipulation                             */
    /* ====================================================================== */

    public void show() throws IOException {
        mainWindowViewController.setStatisticsViewVisible();
        if (userSessionDAO.isLoggedIn()) {
            mainWindowViewController.setLeaderboardViewVisible();
            mainWindowViewController.makeGoBackIconInvisible();

            statisticsViewController.setGameHistory(loadGameHistory());
        }

        else {
            mainWindowViewController.setGuestModeLeaderboardViewVisible();
        }

        stage.show();
    }

    private List<Node> loadGameHistory(){
        try {
            List<Node> playersScoreItem = new ArrayList<>();

            gameHistory = gameHistoryDAO.getGameHistory();

            for (Map<String, String> leaderboardEntry : gameHistory) {
                Node node = loadGameHistoryItem(leaderboardEntry);
                playersScoreItem.add(node);
            }

            return playersScoreItem;

        } catch (IOException | InterruptedException e) {
            errorHandler.failedLoading(e);
            return new ArrayList<>();
        }

    }

    private Node loadGameHistoryItem(Map<String, String> gameHistory) throws IOException {
        URL url = GameHistoryItemViewController
                .class.getResource("GameHistoryItemView.fxml");

        FXMLLoader loader = new FXMLLoader(url);
        Node node = loader.load();

        GameHistoryItemViewController gameHistoryItemViewController
                = loader.getController();

        String date = gameHistory.get(Statistics.ENTRY_DATE);
        String deckName = gameHistory.get(Statistics.ENTRY_DECK_NAME);
        String score = gameHistory.get(Statistics.ENTRY_SCORE);
        gameHistoryItemViewController.setGameHistoryItem(date, deckName, score);

        return node;

    }

    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void StatisticsClicked() throws IOException;
    }

}
