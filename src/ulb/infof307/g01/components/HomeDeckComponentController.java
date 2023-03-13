package ulb.infof307.g01.components;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

public class HomeDeckComponentController {
    @FXML
    public Button toggleOptions;
    @FXML
    public FontIcon threeDotIcon;
    public FontIcon threeDotIconShown;
    public StackPane optionsOverlay;
    public StackPane optionsPanel;
    @FXML
    private String deckName;

    public void setDeckName(String deckName) {
        // TODO fix this
        this.deckName = deckName;
    }


    public void handleHoverOptions(MouseEvent mouseEvent) {
        threeDotIcon.setIconColor(Color.web("#FFFFFF"));
        threeDotIconShown.setIconColor(Color.web("#FFFFFF"));
    }

    public void handleHoverOptionsExit(MouseEvent mouseEvent) {
        threeDotIcon.setIconColor(Color.web("#000000"));
        threeDotIconShown.setIconColor(Color.web("#000000"));
    }

    public void handleToggleOptionClicked(MouseEvent mouseEvent) {
        optionsOverlay.setVisible(!optionsOverlay.isVisible());
        optionsPanel.setVisible(!optionsPanel.isVisible());
    }
}
