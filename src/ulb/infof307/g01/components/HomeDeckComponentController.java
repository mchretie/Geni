package ulb.infof307.g01.components;

import javafx.beans.value.ObservableStringValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.database.DeckManager;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;
import ulb.infof307.g01.view.HomeViewController;
import ulb.infof307.g01.view.MainViewController;

import java.io.IOException;
import java.util.List;

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
    @FXML
    public FlowPane deckTags;

    private Deck deck;

    private final DeckManager dm = DeckManager.singleton();

    public void setDeck(Deck deck) {
        this.deck = deck;
        this.deck.addTag(new Tag("test", "677c9e"));
        this.deck.addTag(new Tag("test2", "e0123f"));
        this.deck.addTag(new Tag("test3", "f011d9"));
        this.updateDeckButtonName();
        this.updateDeckTags();
    }

    private void updateDeckButtonName() {
        this.deckButton.setText(this.deck.getName());
    }

    private void updateDeckTags() {
        this.deckTags.getChildren().clear();
        List<Tag> tags = this.deck.getTags();
        int tagCount = 4;
        if (tags.size() < tagCount) {
            tagCount = this.deck.getTags().size();
        }
        for (int i = 0; i < tagCount; i++) {
            Tag tag = tags.get(i);
            StackPane tagPane = new StackPane();
            tagPane.setPrefWidth(80);
            tagPane.setPrefHeight(30);
            tagPane.setStyle("-fx-background-color: #%s; -fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #000000; -fx-border-width: 1;".formatted(tag.getColor()));
            Label tagLabel = new Label(tag.getName());
            tagLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 12;");
            tagPane.getChildren().add(tagLabel);
            this.deckTags.getChildren().add(tagPane);
        }
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
