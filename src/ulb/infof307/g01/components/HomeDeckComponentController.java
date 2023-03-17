package ulb.infof307.g01.components;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.database.DeckDAO;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;
import ulb.infof307.g01.view.HomeViewController;
import ulb.infof307.g01.view.MainViewController;

import java.io.IOException;
import java.util.List;

public class HomeDeckComponentController {

    @FXML
    public Rectangle optionsOverlayRect;

    @FXML
    public Button deckButton;
    public Button shareDeckButton;
    public Button deleteDeckButton;
    public Button editDeck;
    public Button toggleOptions;

    @FXML
    public FontIcon editDeckIcon;
    public FontIcon removeDeckIcon;
    public FontIcon shareDeckIcon;
    public FontIcon threeDotIcon;

    @FXML
    public StackPane optionsOverlay;
    public StackPane homeDeckPane;
    public FlowPane deckTags;
    public StackPane optionsPanel;

    private Deck deck;
    private final DeckDAO dm = DeckDAO.singleton();

    public void setDeck(Deck deck) {
        this.deck = deck;
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

    public void handleHoverOptions() {
        threeDotIcon.setIconColor(Color.web("#FFFFFF"));
    }

    public void handleHoverOptionsExit() {
        threeDotIcon.setIconColor(Color.web("#000000"));
    }

    public void handleToggleOptionClicked() {
        optionsOverlayRect.setVisible(!optionsOverlayRect.isVisible());
        optionsPanel.setVisible(!optionsPanel.isVisible());
    }

    public void handleToggleEditDeckClicked(MouseEvent mouseEvent) throws IOException {
        MainViewController mainViewController = getMainViewController();
        if (mainViewController == null) {
            System.out.println("MainViewController is null");
            return;
        }
        mainViewController.setEditDeckView(deck);
    }

    private MainViewController getMainViewController() {
        Parent root = homeDeckPane.getScene().getRoot();
        return (MainViewController) root.getUserData();
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

    public void handleDoubleDeckClicked(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getClickCount() == 2) {
            MainViewController mainViewController = getMainViewController();
            mainViewController.setPlayDeckView(deck);

        }
    }
}
