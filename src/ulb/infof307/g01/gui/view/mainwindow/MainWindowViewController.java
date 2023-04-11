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
import ulb.infof307.g01.gui.view.login.LoginViewController;
import ulb.infof307.g01.gui.view.playdeck.PlayDeckViewController;
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
    private FontIcon aboutIcon;

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
    private AnchorPane loginView;

    @FXML
    private AnchorPane profileView;

    @FXML
    private DeckMenuViewController deckMenuViewController;

    @FXML
    private BorderPane guestModeDeckMenuView;

    @FXML
    private EditDeckViewController editDeckViewController;

    @FXML
    private PlayDeckViewController playDeckViewController;

    @FXML
    private EditCardViewController editCardViewController;

    @FXML
    private LoginViewController loginViewController;

    @FXML
    private ProfileViewController profileViewController;


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

    public LoginViewController getLoginViewController() {
        return loginViewController;
    }

    public ProfileViewController getProfileViewController() {
        return profileViewController;
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
        for (Node child : centerStackPane.getChildren())
            child.setVisible(pane == child);
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

    public void setLoginViewVisible() {
        setAllInvisibleExcept(loginView);
    }

    public void setProfileViewVisible() {
        setAllInvisibleExcept(profileView);
    }

    public void setGuestModeVisible() {
        setAllInvisibleExcept(guestModeDeckMenuView);
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
        listener.goToCurrentDeckClicked();
    }

    @FXML
    private void goToAboutClicked() {
        listener.goToAboutClicked();
    }

    @FXML
    private void handleProfileClicked() { listener.handleProfileClicked();  }

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
    private void handleAboutHover() {
        aboutIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    private void handleAboutExitHover() {
        aboutIcon.setIconColor(Color.web("#000000"));
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
        void goToCurrentDeckClicked();
        void goToAboutClicked();
        void handleProfileClicked();
    }
}
