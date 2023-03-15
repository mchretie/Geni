package ulb.infof307.g01.view.navigation;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

public class UpperNavigationBarViewController {

    @FXML
    public Button goBack;
    public Button userProfile;

    @FXML
    public Label topBarText;

    @FXML
    public FontIcon goBackIcon;
    public FontIcon userProfileIcon;


    public void handleGoBackClicked() {

    }

    /* ============================================================================================================== */
    /*                                                  Hover handlers                                                */
    /* ============================================================================================================== */

    public void handleGoBackHover() {
        goBackIcon.setIconColor(Color.web("#FFFFFF"));
    }

    public void handleGoBackExitHover() {
        goBackIcon.setIconColor(Color.web("#000000"));
    }

    public void handleUserProfileHover() {
        userProfileIcon.setIconColor(Color.web("#FFFFFF"));
    }

    public void handleUserProfileExitHover() {
        userProfileIcon.setIconColor(Color.web("#000000"));
    }

}
