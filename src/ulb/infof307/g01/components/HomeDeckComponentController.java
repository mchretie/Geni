package ulb.infof307.g01.components;

import javafx.beans.value.ObservableStringValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.database.DeckManager;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.view.HomeViewController;
import ulb.infof307.g01.view.MainViewController;

import java.io.IOException;

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
    public Button deckButton;
    @FXML
    public AnchorPane homeDeckPane;

    private Deck deck;

    private final DeckManager dm = DeckManager.singleton();

    public void setDeck(Deck deck) {
        this.deck = deck;
        this.updateDeckButtonName();
    }

    private void updateDeckButtonName() {
        this.deckButton.setText(this.deck.getName());
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

    public void handleToggleEditDeckClicked(MouseEvent mouseEvent) throws IOException {
        Parent root = homeDeckPane.getScene().getRoot();
        MainViewController mainViewController = (MainViewController) root.getUserData();
        if (mainViewController == null) {
            System.out.println("MainViewController is null");
            return;
        }
        mainViewController.loadEditDeckView(deck);
    }

    public void handleHoverEditDeck(MouseEvent mouseEvent) {
        editDeckIcon.setIconColor(Color.web("#FFFFFF"));
    }

    public void handleHoverEditDeckExit(MouseEvent mouseEvent) {
        editDeckIcon.setIconColor(Color.web("#000000"));
    }

    public void handleToggleRemoveDeckClicked(MouseEvent mouseEvent) {
        dm.deleteDeck(deck);
        Parent parent = homeDeckPane.getParent();
        HomeViewController homeViewController = (HomeViewController) parent.getUserData();
        if (homeViewController == null) {
            System.out.println("HomeViewController is null");
            return;
        }
        homeViewController.loadDecks();
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
