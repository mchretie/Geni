package ulb.infof307.g01.gui.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;
import ulb.infof307.g01.gui.controller.errorhandler.ErrorHandler;
import ulb.infof307.g01.gui.http.ServerCommunicator;
import ulb.infof307.g01.gui.http.exceptions.ServerCommunicationFailedException;
import ulb.infof307.g01.gui.view.statistics.GameHistoryItemViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.gui.view.statistics.StatisticsViewController;
import ulb.infof307.g01.model.gamehistory.Game;
import ulb.infof307.g01.model.gamehistory.GameHistory;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StatisticsController {
    private final Stage stage;
    private final ErrorHandler errorHandler;
    private final MainWindowViewController mainWindowViewController;

    private final StatisticsViewController statisticsViewController;

    private final ServerCommunicator serverCommunicator;

    /* ====================================================================== */
    /*                              Constructor                               */
    /* ====================================================================== */

    public StatisticsController(Stage stage,
                                ErrorHandler errorHandler,
                                MainWindowViewController mainWindowViewController,
                                ServerCommunicator serverCommunicator) {
        this.stage = stage;
        this.errorHandler = errorHandler;
        this.mainWindowViewController = mainWindowViewController;
        this.serverCommunicator = serverCommunicator;

        this.statisticsViewController
                = mainWindowViewController.getStatisticsViewController();
    }


    /* ====================================================================== */
    /*                         Stage Manipulation                             */
    /* ====================================================================== */

    public void show() {
        mainWindowViewController.setStatisticsViewVisible();

        if (serverCommunicator.isUserLoggedIn()) {
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

            GameHistory gameHistory = serverCommunicator.getGameHistory();

            for (Game game : gameHistory) {
                Node node = loadGameHistoryItem(game);
                playersScoreItem.add(node);
            }

            statisticsViewController
                    .setMainStatistics(
                            gameHistory.getNumberOfGames() + "",
                            serverCommunicator.getDeckCount() + "",
                            gameHistory.totalScore() + "");

            return playersScoreItem;

        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
            return new ArrayList<>();
        }

        catch (IOException e) {
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
