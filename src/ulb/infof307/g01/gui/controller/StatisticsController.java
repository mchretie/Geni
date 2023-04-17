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
import ulb.infof307.g01.model.Game;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StatisticsController implements StatisticsViewController.Listener {
    private final Stage stage;
    private final ErrorHandler errorHandler;
    private final MainWindowViewController mainWindowViewController;
    private final UserSessionDAO userSessionDAO;
    private final GameHistoryDAO gameHistoryDAO;

    private final StatisticsViewController statisticsViewController;

    /* ====================================================================== */
    /*                              Constructor                               */
    /* ====================================================================== */

    public StatisticsController(Stage stage,
                                ErrorHandler errorHandler,
                                MainWindowViewController mainWindowViewController,
                                UserSessionDAO userSessionDAO,
                                GameHistoryDAO gameHistoryDAO) {
        this.stage = stage;
        this.errorHandler = errorHandler;
        this.mainWindowViewController = mainWindowViewController;
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
            gameHistoryDAO.setToken(userSessionDAO.getToken());
            mainWindowViewController.setStatisticsViewVisible();
            mainWindowViewController.makeGoBackIconVisible();

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

            for (Game game : gameHistoryDAO.getGameHistory()) {
                Node node = loadGameHistoryItem(game);
                playersScoreItem.add(node);
            }

            return playersScoreItem;

        } catch (IOException | InterruptedException e) {
            errorHandler.failedLoading(e);
            return new ArrayList<>();
        }
    }

    private Node loadGameHistoryItem(Game game) throws IOException {
        URL url = GameHistoryItemViewController
                .class.getResource("GameHistoryItemView.fxml");

        FXMLLoader loader = new FXMLLoader(url);
        Node node = loader.load();

        GameHistoryItemViewController gameHistoryItemViewController
                = loader.getController();

        gameHistoryItemViewController
                .setGameHistoryItem(
                        game.getFormattedTimestamp(),
                        game.deckName(),
                        game.score());

        return node;

    }
}
