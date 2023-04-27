package ulb.infof307.g01.gui.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;
import ulb.infof307.g01.gui.controller.errorhandler.ErrorHandler;
import ulb.infof307.g01.gui.http.ServerCommunicator;
import ulb.infof307.g01.gui.view.leaderboard.GlobalLeaderboardViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.gui.view.leaderboard.GlobalLeaderboardEntryViewController;
import ulb.infof307.g01.model.leaderboard.GlobalLeaderboard;
import ulb.infof307.g01.model.leaderboard.GlobalLeaderboardEntry;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GlobalLeaderboardController {
    private final Stage stage;
    private final MainWindowViewController mainWindowViewController;
    private final GlobalLeaderboardViewController leaderboardViewController;
    private final ErrorHandler errorHandler;

    private final ServerCommunicator serverCommunicator;

    /* ====================================================================== */
    /*                              Constructor                               */
    /* ====================================================================== */

    public GlobalLeaderboardController(Stage stage,
                                       MainWindowViewController mainWindowViewController,
                                       ErrorHandler errorHandler,
                                       ServerCommunicator serverCommunicator) {

        this.stage = stage;
        this.mainWindowViewController = mainWindowViewController;
        this.errorHandler = errorHandler;
        this.serverCommunicator = serverCommunicator;

        this.leaderboardViewController = mainWindowViewController.getLeaderboardViewController();
    }


    /* ====================================================================== */
    /*                         Stage Manipulation                             */
    /* ====================================================================== */

    public void show() throws IOException, InterruptedException {
        if (serverCommunicator.isUserLoggedIn()) {
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

            GlobalLeaderboard leaderboard = serverCommunicator.getGlobalLeaderboard();

            String username = serverCommunicator.getSessionUsername();

            leaderboardViewController
                    .setPersonalInformation(
                            username,
                            leaderboard.getUserRank(username),
                            leaderboard.getUserScore(username),
                            serverCommunicator.numberOfPublicPlayedDecks() + "");

            for (GlobalLeaderboardEntry leaderboardEntry : leaderboard) {
                Node node = loadEntry(leaderboardEntry);
                playersScoreItem.add(node);
            }

            return playersScoreItem;

        } catch (IOException | InterruptedException e) {
            errorHandler.failedLoading(e);
            return new ArrayList<>();
        }
    }

    private Node loadEntry(GlobalLeaderboardEntry leaderboardEntry) throws IOException {
        URL url = GlobalLeaderboardEntryViewController
                        .class.getResource("GlobalLeaderboardEntryView.fxml");

        FXMLLoader loader = new FXMLLoader(url);
        Node node = loader.load();

        GlobalLeaderboardEntryViewController playerScoreItemViewController
                = loader.getController();

        playerScoreItemViewController
                .setPlayerScoreItem(
                        leaderboardEntry.getRank(),
                        leaderboardEntry.getUsername(),
                        leaderboardEntry.getTotalScore());

        return node;
    }
}
