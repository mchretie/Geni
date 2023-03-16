package ulb.infof307.g01.view.mainwindow;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import ulb.infof307.g01.view.deckmenu.DeckMenuViewController;
import ulb.infof307.g01.view.editdeck.EditDeckViewController;
import ulb.infof307.g01.view.playdeck.PlayDeckViewController;

public class MainWindowViewController {

    @FXML
    public Button homeButton;
    public Button currentDeckButton;
    public Button aboutButton;
    public Button goBack;
    public Button userProfile;

    @FXML
    public FontIcon homeIcon;
    public FontIcon currentDeckIcon;
    public FontIcon aboutIcon;
    public FontIcon goBackIcon;
    public FontIcon userProfileIcon;

    @FXML
    public Label topBarText;

    @FXML
    public StackPane centerStackPane;
    public BorderPane deckMenuView;
    public AnchorPane editDeckView;
    public BorderPane playDeckView;

    @FXML
    public DeckMenuViewController deckMenuViewController;
    public EditDeckViewController editDeckViewController;
    public PlayDeckViewController playDeckViewController;

    private NavigationListener listener;

    public void setListener(NavigationListener listener) {
        this.listener = listener;
    }


    /* ============================================================================================================== */
    /*                                         Child view controllers getters                                          */
    /* ============================================================================================================== */

    public DeckMenuViewController getDeckMenuViewController() {
        return deckMenuViewController;
    }

    public EditDeckViewController getEditDeckViewController() {
        return editDeckViewController;
    }

    public PlayDeckViewController getPlayDeckViewController() {
        return playDeckViewController;
    }


    /* ============================================================================================================== */
    /*                                            Center stage setters                                                */
    /* ============================================================================================================== */

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


    /* ============================================================================================================== */
    /*                                                  Click handlers                                                */
    /* ============================================================================================================== */

    @FXML
    public void handleGoBackClicked() {
        listener.goBackClicked();
    }

    @FXML
    public void goToHomeClicked() {
        listener.goToHomeClicked();
    }

    @FXML
    public void goToCurrentDeckClicked() {
        listener.goToCurrentDeckClicked();
    }

    @FXML
    public void goToAboutClicked() {
        listener.goToAboutClicked();
    }


    /* ============================================================================================================== */
    /*                                                  Hover handlers                                                */
    /* ============================================================================================================== */

    @FXML
    public void handleHomeHover() {
        homeIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    public void handleHomeExitHover() {
        homeIcon.setIconColor(Color.web("#000000"));
    }

    @FXML
    public void handleCurrentDeckHover() {
        currentDeckIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    public void handleCurrentDeckExitHover() {
        currentDeckIcon.setIconColor(Color.web("#000000"));
    }

    @FXML
    public void handleAboutHover() {
        aboutIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    public void handleAboutExitHover() {
        aboutIcon.setIconColor(Color.web("#000000"));
    }

    @FXML
    public void handleGoBackHover() {
        goBackIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    public void handleGoBackExitHover() {
        goBackIcon.setIconColor(Color.web("#000000"));
    }

    @FXML
    public void handleUserProfileHover() {
        userProfileIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    public void handleUserProfileExitHover() {
        userProfileIcon.setIconColor(Color.web("#000000"));
    }


    /* ============================================================================================================== */
    /*                                              Listener interface                                                */
    /* ============================================================================================================== */

    public interface NavigationListener {
        void goBackClicked();
        void goToHomeClicked();
        void goToCurrentDeckClicked();
        void goToAboutClicked();
    }
}
