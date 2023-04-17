package ulb.infof307.g01.gui.view.mainwindow;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.gui.view.deckmenu.DeckMenuViewController;
import ulb.infof307.g01.gui.view.editcard.EditCardViewController;
import ulb.infof307.g01.gui.view.editdeck.EditDeckViewController;
import ulb.infof307.g01.gui.view.leaderboard.LeaderboardViewController;
import ulb.infof307.g01.gui.view.playdeck.PlayDeckViewController;
import ulb.infof307.g01.gui.view.result.ResultViewController;
import ulb.infof307.g01.gui.view.statistics.StatisticsViewController;
import ulb.infof307.g01.gui.view.userauth.UserAuthViewController;
import ulb.infof307.g01.gui.view.profile.ProfileViewController;


public class MainWindowViewController {

    /* ====================================================================== */
    /*                               FXML Attributes                          */
    /* ====================================================================== */

    @FXML
    private FontIcon homeIcon;

    @FXML
    private FontIcon currentDeckIcon;

    @FXML
    private FontIcon leaderboardIcon;

    @FXML
    private FontIcon goBackIcon;

    @FXML
    private FontIcon userProfileIcon;

    @FXML
    private StackPane centerStackPane;

    @FXML
    private BorderPane deckMenuView;

    @FXML
    private HBox editDeckView;

    @FXML
    private BorderPane playDeckView;

    @FXML
    private VBox editCardView;

    @FXML
    private VBox profileView;

    @FXML
    private StackPane userAuthView;

    @FXML
    private BorderPane guestModeDeckMenuView;

    @FXML
    private BorderPane guestModeLeaderboardView;

    @FXML
    private VBox resultView;

    @FXML
    private BorderPane leaderboardView;

    @FXML
    private DeckMenuViewController deckMenuViewController;

    @FXML
    private EditDeckViewController editDeckViewController;

    @FXML
    private PlayDeckViewController playDeckViewController;

    @FXML
    private EditCardViewController editCardViewController;

    @FXML
    private ProfileViewController profileViewController;

    @FXML
    private UserAuthViewController userAuthViewController;

    @FXML
    private ResultViewController resultViewController;

    @FXML
    private LeaderboardViewController leaderboardViewController;

    @FXML
    private StatisticsViewController statisticsViewController;


    /* ====================================================================== */
    /*                                 Listener                               */
    /* ====================================================================== */

    private NavigationListener listener;


    /* ====================================================================== */
    /*                                Setter                                  */
    /* ====================================================================== */

    public void setListener(NavigationListener listener) {
        this.listener = listener;
    }

    /* ====================================================================== */
    /*                      Child view controllers getters                    */
    /* ====================================================================== */

    public DeckMenuViewController getDeckMenuViewController() {
        return deckMenuViewController;
    }
    public EditDeckViewController getEditDeckViewController() {
        return editDeckViewController;
    }
    public PlayDeckViewController getPlayDeckViewController() {
        return playDeckViewController;
    }
    public EditCardViewController getEditCardViewController() { 
        return editCardViewController; 
    }
    public LeaderboardViewController getLeaderboardViewController() { return leaderboardViewController; }
    public ProfileViewController getProfileViewController() {
        return profileViewController;
    }
    public UserAuthViewController getUserAuthViewController() {
        return userAuthViewController;
    }
    public ResultViewController getResultViewController() {
        return resultViewController;
    }
    public StatisticsViewController getStatisticsViewController() { return statisticsViewController; }


    /* ====================================================================== */
    /*                              Alerts                                    */
    /* ====================================================================== */

    public void alertError(String errorTitle, String errorDescription) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText(errorTitle);
        errorAlert.setContentText(errorDescription);
        errorAlert.showAndWait();
    }

    public void alertInformation(String infoTitle, String infoDescription) {
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setHeaderText(infoTitle);
        infoAlert.setContentText(infoDescription);
        infoAlert.showAndWait();
    }


    /* ====================================================================== */
    /*                        Center stage setters                            */
    /* ====================================================================== */

    private void setAllInvisibleExcept(Pane pane) {
        for (Node child : centerStackPane.getChildren())
            child.setVisible(pane == child);
    }

    public void setAllInvisible() {
        for (Node child : centerStackPane.getChildren())
            child.setVisible(false);
    }

    public void setDeckMenuViewVisible() {
        setAllInvisibleExcept(deckMenuView);
    }

    public void setEditDeckViewVisible() {
        setAllInvisibleExcept(editDeckView);
    }

    public void setPlayDeckViewVisible() {
        setAllInvisibleExcept(playDeckView);
    }

    public void setEditCardViewVisible() {
        setAllInvisibleExcept(editCardView);
    }

    public void setProfileViewVisible() {
        setAllInvisibleExcept(profileView);
    }

    public void setUserAuthViewController() {
        setAllInvisibleExcept(userAuthView);
    }

    public void setGuestModeDeckMenuViewVisible() { setAllInvisibleExcept(guestModeDeckMenuView); }

    public void setResultViewVisible() {
        setAllInvisibleExcept(resultView);
    }

    public void setLeaderboardViewVisible() { 
        setAllInvisibleExcept(leaderboardView); 
    }

    public void setGuestModeLeaderboardViewVisible() { setAllInvisibleExcept(guestModeLeaderboardView); }


    /* ====================================================================== */
    /*                          Icon Visibility                               */
    /* ====================================================================== */

    public void makeGoBackIconVisible() {
        goBackIcon.setVisible(true);
    }

    public void makeGoBackIconInvisible() {
        goBackIcon.setVisible(false);
    }


    /* ====================================================================== */
    /*                               Click handlers                           */
    /* ====================================================================== */

    @FXML
    private void handleGoBackClicked() {
        listener.goBackClicked();
    }

    @FXML
    private void goToHomeClicked() {
        listener.goToHomeClicked();
    }

    @FXML
    private void goToCurrentDeckClicked() {
        listener.goToCurrentPlayingDeck();
    }

    @FXML
    private void goToLeaderboardClicked() { 
        listener.goToLeaderboardClicked(); 
    }

    @FXML
    private void handleProfileClicked() {
        listener.goToProfileClicked();
    }

    /* ====================================================================== */
    /*                              Hover handlers                            */
    /* ====================================================================== */

    @FXML
    private void handleHomeHover() {
        homeIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    private void handleHomeExitHover() {
        homeIcon.setIconColor(Color.web("#000000"));
    }

    @FXML
    private void handleCurrentDeckHover() {
        currentDeckIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    private void handleCurrentDeckExitHover() {
        currentDeckIcon.setIconColor(Color.web("#000000"));
    }

    @FXML
    private void handleLeaderboardHover() {
        leaderboardIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    private void handleLeaderboardExitHover() {
        leaderboardIcon.setIconColor(Color.web("#000000"));
    }

    @FXML
    private void handleGoBackHover() {
        goBackIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    private void handleGoBackExitHover() {
        goBackIcon.setIconColor(Color.web("#000000"));
    }

    @FXML
    private void handleUserProfileHover() {
        userProfileIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    private void handleUserProfileExitHover() {
        userProfileIcon.setIconColor(Color.web("#000000"));
    }


    /* ====================================================================== */
    /*                        Listener interface                              */
    /* ====================================================================== */

    public interface NavigationListener {
        void goBackClicked();
        void goToHomeClicked();
        void goToCurrentPlayingDeck();
        void goToLeaderboardClicked();
        void goToProfileClicked();
    }
}
