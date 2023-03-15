package ulb.infof307.g01.view.navigation;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

public class LowerNavigationBarViewController {

    @FXML
    public Button homeButton;
    public Button currentDeckButton;
    public Button aboutButton;

    @FXML
    public FontIcon homeIcon;
    public FontIcon currentDeckIcon;
    public FontIcon aboutIcon;

    /* ============================================================================================================== */
    /*                                                  Hover handlers                                                */
    /* ============================================================================================================== */

    public void handleHomeHover() {
        homeIcon.setIconColor(Color.web("#FFFFFF"));
    }

    public void handleHomeExitHover() {
        homeIcon.setIconColor(Color.web("#000000"));
    }

    public void handleCurrentDeckHover() {
        currentDeckIcon.setIconColor(Color.web("#FFFFFF"));
    }

    public void handleCurrentDeckExitHover() {
        currentDeckIcon.setIconColor(Color.web("#000000"));
    }

    public void handleAboutHover() {
        aboutIcon.setIconColor(Color.web("#FFFFFF"));
    }

    public void handleAboutExitHover() {
        aboutIcon.setIconColor(Color.web("#000000"));
    }

}
