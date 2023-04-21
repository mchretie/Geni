package ulb.infof307.g01.gui.view.mainwindow;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.gui.view.deckmenu.DeckMenuViewController;
import ulb.infof307.g01.gui.view.editcard.EditCardViewController;
import ulb.infof307.g01.gui.view.editdeck.EditDeckViewController;
import ulb.infof307.g01.gui.view.leaderboard.GlobalLeaderboardViewController;
import ulb.infof307.g01.gui.view.playdeck.PlayDeckViewController;
import ulb.infof307.g01.gui.view.result.ResultViewController;
import ulb.infof307.g01.gui.view.statistics.StatisticsViewController;
import ulb.infof307.g01.gui.view.userauth.UserAuthViewController;
import ulb.infof307.g01.gui.view.profile.ProfileViewController;

import java.util.Arrays;
import java.util.List;


public class MainWindowViewController {

    private final String initialButtonStyle = "-fx-background-color: transparent;";

    /* ====================================================================== */
    /*                               FXML Attributes                          */
    /* ====================================================================== */

    @FXML
    private Button currentDeckButton;

    @FXML
    private Button leaderboardButton;

    @FXML
    private Button profileButton;

    @FXML
    private Button homeButton;

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
    private HBox bottomHBox;

    @FXML
    private HBox topHBox;

    @FXML
    private BorderPane playDeckView;

    @FXML
    private BorderPane myBorderPane;

    @FXML
    private VBox editCardView;

    @FXML
    private VBox profileView;

    @FXML
    private StackPane userAuthView;

    @FXML
    private BorderPane guestModeDeckMenuView;

    @FXML
    private StackPane infoAppView;

    @FXML
    private BorderPane Profileview;
    @FXML
    private BorderPane guestModeLeaderboardView;

    @FXML
    private VBox resultView;

    @FXML
    private BorderPane leaderboardView;

    @FXML
    private BorderPane statisticsView;

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
    private GlobalLeaderboardViewController leaderboardViewController;

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

    public GlobalLeaderboardViewController getLeaderboardViewController() {
        return leaderboardViewController;
    }

    public ProfileViewController getProfileViewController() {
        return profileViewController;
    }
    public UserAuthViewController getUserAuthViewController() {
        return userAuthViewController;
    }

    public ResultViewController getResultViewController() {
        return resultViewController;
    }

    public StatisticsViewController getStatisticsViewController() {
        return statisticsViewController;
    }


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
        for (Node child : centerStackPane.getChildren()) {
            child.setVisible(pane == child);
        }
        if(pane != userAuthView){
            infoAppView.setVisible(false);
            infoAppView.setManaged(false);
        }

    }

    public void setAllInvisible() {
        for (Node child : centerStackPane.getChildren())
            child.setVisible(false);
    }

    public void setDeckMenuViewVisible() {
        setAllInvisibleExcept(deckMenuView);
        onClick(homeButton);
    }

    public void setEditDeckViewVisible() {
        setAllInvisibleExcept(editDeckView);
    }

    public void setPlayDeckViewVisible() {
        setAllInvisibleExcept(playDeckView);
        onClick(currentDeckButton);
    }

    public void setEditCardViewVisible() {
        setAllInvisibleExcept(editCardView);
    }

    public void setProfileViewVisible() {
        setAllInvisibleExcept(profileView);
        onClick(profileButton);
    }

    public void setUserAuthViewController() {
        setAllInvisibleExcept(userAuthView);
        onClick(profileButton);
    }

    public void setGuestModeDeckMenuViewVisible() {
        setAllInvisibleExcept(guestModeDeckMenuView);
        onClick(homeButton);
    }

    public void setResultViewVisible() {
        setAllInvisibleExcept(resultView);
    }

    public void setLeaderboardViewVisible() { 
        setAllInvisibleExcept(leaderboardView);
        onClick(leaderboardButton);
    }

    public void setGuestModeLeaderboardViewVisible() {
        setAllInvisibleExcept(guestModeLeaderboardView);
        onClick(leaderboardButton);
    }

    public void setStatisticsViewVisible() {
        setAllInvisibleExcept(statisticsView);
    }

    public void setInfoAppViewVisible(){
        infoAppView.setVisible(true);
        infoAppView.setManaged(true);
    }
    public void setMyBorderPaneDarkgrey(){myBorderPane.setStyle("-fx-background-color: #2C3F4E");}


    /* ====================================================================== */
    /*                          Icon Visibility                               */
    /* ====================================================================== */

    public void makeGoBackIconVisible() {
        goBackIcon.setVisible(true);
    }

    public void makeGoBackIconInvisible() {
        goBackIcon.setVisible(false);
    }

    public void makeTopNavigationBarVisible() {
        topHBox.setVisible(true);
        topHBox.setManaged(true);
    }

    public void makeTopNavigationBarInvisible() {
        topHBox.setVisible(false);
        topHBox.setManaged(false);
    }

    public void makebottomNavigationBarVisible() {
        bottomHBox.setVisible(true);
        bottomHBox.setManaged(true);
    }

    public void makebottomNavigationBarInvisible() {
        bottomHBox.setVisible(false);
        bottomHBox.setManaged(false);
    }


    /* ====================================================================== */
    /*                               Click handlers                           */
    /* ====================================================================== */

    private void resetButtonExcept(Button button) {
        List<Button> buttons = Arrays.asList(homeButton, profileButton, currentDeckButton, leaderboardButton);
        for (Button b : buttons)
            if (b != button)
                b.setStyle(initialButtonStyle);
    }

    private void onClick(Button button) {
        button.setStyle("-fx-background-color: \"#50C878\";");
        resetButtonExcept(button);
    }

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
    public void handleProfileClicked() {
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
