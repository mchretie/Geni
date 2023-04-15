package ulb.infof307.g01.gui.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.util.Pair;
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
        leaderboardViewController.setBoard(loadBoard());

        stage.show();
    }

    private List<Node> loadBoard() {
        try {
            List<Node> playersScoreItem = new ArrayList<>();
            //TODO Display depending of the backend

            List<Pair<String, Integer>> leaderboard = leaderboardDAO.getGlobalLeaderboard();

            for (int i = 0; i < leaderboard.size(); i++) {
                Pair<String, Integer> pair = leaderboard.get(i);

                if (pair.getKey().equals(userSessionDAO.getUsername())) {
                    leaderboardViewController.setPersonalInformation(userSessionDAO.getUsername(),
                            String.valueOf(i+1),
                            String.valueOf(pair.getValue()),
                            String.valueOf(deckDAO.getAllDecksMetadata().size()));  //TODO get the number of decks played
                }

                URL url = PlayerScoreItemViewController.class.getResource("PlayerScoreItemView.fxml");
                FXMLLoader loader = new FXMLLoader(url);

                Node node = loader.load();
                PlayerScoreItemViewController playerScoreItemViewController = loader.getController();
                playerScoreItemViewController.setListener(this);
                playerScoreItemViewController.setPlayerScoreItem();

                playersScoreItem.add(node);
            }

            return playersScoreItem;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void leaderboardClicked();
    }
}
