package ulb.infof307.g01.components;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.kordamp.ikonli.javafx.FontIcon;

public class HomeDeckComponentController {
    @FXML
    public Button toggleOptions;
    @FXML
    public FontIcon threeDotIcon;
    @FXML
    public StackPane optionsOverlay;
    @FXML
    public StackPane optionsPanel;
    @FXML
    public FontIcon editDeckIcon;
    @FXML
    public FontIcon removeDeckIcon;
    @FXML
    public FontIcon shareDeckIcon;
    @FXML
    public Rectangle optionsOverlayRect;
    @FXML
    private String deckName = "Deck Name";

    public void setDeckName(String deckName) {
        // TODO fix this
        this.deckName = deckName;
    }


    public void handleHoverOptions(MouseEvent mouseEvent) {
        threeDotIcon.setIconColor(Color.web("#FFFFFF"));
    }

    public void handleHoverOptionsExit(MouseEvent mouseEvent) {
        threeDotIcon.setIconColor(Color.web("#000000"));
    }

    public void handleToggleOptionClicked(MouseEvent mouseEvent) {
        optionsOverlayRect.setVisible(!optionsOverlayRect.isVisible());
        optionsPanel.setVisible(!optionsPanel.isVisible());
    }

    public void handleToggleEditDeckClicked(MouseEvent mouseEvent) {
    }

    public void handleHoverEditDeck(MouseEvent mouseEvent) {
        editDeckIcon.setIconColor(Color.web("#FFFFFF"));
    }

    public void handleHoverEditDeckExit(MouseEvent mouseEvent) {
        editDeckIcon.setIconColor(Color.web("#000000"));
    }

    public void handleToggleRemoveDeckClicked(MouseEvent mouseEvent) {
    }

    public void handleHoverRemoveDeck(MouseEvent mouseEvent) {
        removeDeckIcon.setIconColor(Color.web("#FFFFFF"));
    }

    public void handleHoverRemoveDeckExit(MouseEvent mouseEvent) {
        removeDeckIcon.setIconColor(Color.web("#000000"));
    }

    public void handleToggleShareDeckClicked(MouseEvent mouseEvent) {
    }

    public void handleHoverShareDeck(MouseEvent mouseEvent) {
        shareDeckIcon.setIconColor(Color.web("#FFFFFF"));
    }

    public void handleHoverShareDeckExit(MouseEvent mouseEvent) {
        shareDeckIcon.setIconColor(Color.web("#000000"));
    }
}
