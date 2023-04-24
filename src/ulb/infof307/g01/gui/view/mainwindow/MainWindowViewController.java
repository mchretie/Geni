package ulb.infof307.g01.gui.view.mainwindow;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.gui.view.deckmenu.DeckMenuViewController;
import ulb.infof307.g01.gui.view.deckpreview.DeckPreviewViewController;
import ulb.infof307.g01.gui.view.editcard.EditCardViewController;
import ulb.infof307.g01.gui.view.editdeck.EditDeckViewController;
import ulb.infof307.g01.gui.view.leaderboard.GlobalLeaderboardViewController;
import ulb.infof307.g01.gui.view.playdeck.PlayDeckViewController;
import ulb.infof307.g01.gui.view.result.ResultViewController;
import ulb.infof307.g01.gui.view.statistics.StatisticsViewController;
import ulb.infof307.g01.gui.view.userauth.UserAuthViewController;
import ulb.infof307.g01.gui.view.profile.ProfileViewController;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;


public class MainWindowViewController implements Initializable {


    /* ====================================================================== */
    /*                               FXML Attributes                          */
    /* ====================================================================== */

    @FXML
    private BorderPane borderPane;

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
    private BorderPane statisticsView;

    @FXML
    private BorderPane deckPreviewView;

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

    @FXML
    private DeckPreviewViewController deckPreviewViewController;

    private final Popup popup = new Popup();

    /* ====================================================================== */
    /*                                 Listener                               */
    /* ====================================================================== */

    private NavigationListener listener;


    /* ====================================================================== */
    /*                               Initializer                              */
    /* ====================================================================== */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        popup.getContent().add(deckPreviewView);

        popup.setOnHidden(event -> {
            centerStackPane.setEffect(null);
            deckPreviewView.setVisible(false);
            centerStackPane.setDisable(false);
            listener.deckPreviewClosed();
        });

        popup.setAutoHide(true);

        popup.setOnShown(event -> {
            centerStackPane.setDisable(true);
            centerStackPane.setEffect(new BoxBlur());

            BorderPane bp = (BorderPane) popup.getContent().get(0);
            bp.setPrefWidth(borderPane.getWidth() * 0.7);
            bp.setPrefHeight(borderPane.getHeight() * 0.6);

            Scene scene = borderPane.getScene();
            Window window = scene.getWindow();
            popup.setX(window.getX() + (scene.getWidth() - bp.getWidth()) / 2);
            popup.setY(window.getY() + (scene.getHeight() - bp.getHeight()) / 2);
        });

        borderPane.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> {
            BorderPane bp = (BorderPane) popup.getContent().get(0);
            bp.setPrefWidth(newSceneWidth.doubleValue() * 0.7);

            Scene scene = borderPane.getScene();
            Window window = scene.getWindow();
            popup.setX(window.getX() + (scene.getWidth() - popup.getWidth()) / 2);
        });

        borderPane.heightProperty().addListener((observableValue, oldSceneHeight, newSceneHeight) -> {
            BorderPane bp = (BorderPane) popup.getContent().get(0);
            bp.setPrefHeight(newSceneHeight.doubleValue() * 0.6);

            Scene scene = borderPane.getScene();
            Window window = scene.getWindow();
            popup.setY(window.getY() + (scene.getHeight() - popup.getHeight()) / 2);
        });
    }

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

    public DeckPreviewViewController getDeckPreviewViewController() {
        return deckPreviewViewController;
    }

    /* ====================================================================== */
    /*                              Alerts                                    */
    /* ====================================================================== */

    private void alert(String title, String description, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.initOwner(borderPane.getScene().getWindow());
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText(title);
        alert.setContentText(description);
        centerStackPane.setEffect(new BoxBlur(20,20,3));
        alert.showAndWait();
        centerStackPane.setEffect(null);
    }

    public void alertError(String errorTitle, String errorDescription) {
        alert(errorTitle, errorDescription, Alert.AlertType.ERROR);
    }

    public void alertInformation(String infoTitle, String infoDescription) {
        alert(infoTitle, infoDescription, Alert.AlertType.INFORMATION);
    }


    /* ====================================================================== */
    /*                        Center stage setters                            */
    /* ====================================================================== */

    private void setAllInvisibleExcept(Pane pane) {
        popup.hide();
        centerStackPane.getChildren()
                        .forEach(child -> child.setVisible(child == pane));
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

    public void setUserAuthViewVisible() {
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

    public void setDeckPreviewViewVisible() {
        deckPreviewView.setVisible(true);
        popup.show(centerStackPane.getScene().getWindow());
    }


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

    public void makeBottomNavigationBarInvisible() {
        bottomHBox.setVisible(false);
        bottomHBox.setManaged(false);
    }


    /* ====================================================================== */
    /*                               Click handlers                           */
    /* ====================================================================== */

    private void resetButtonExcept(Button button) {
        List<Button> buttons = Arrays.asList(homeButton, profileButton, currentDeckButton, leaderboardButton);
        String initialButtonStyle = "-fx-background-color: transparent;";
        buttons.stream()
                .filter(b -> b != button)
                .forEach(b -> b.setStyle(initialButtonStyle));
    }

    private void onClick(Button button) {
        button.setStyle("-fx-background-color: \"#b09fcb\";");
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
        void deckPreviewClosed();
    }
}
