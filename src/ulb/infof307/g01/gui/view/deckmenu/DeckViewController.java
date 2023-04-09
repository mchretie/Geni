package ulb.infof307.g01.gui.view.deckmenu;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.Deck;

import java.io.File;

public class DeckViewController {

    @FXML
    private StackPane stackPane;
    @FXML
    private ImageView backgroundImage;
    @FXML
    private Label playDeckLabel;
    @FXML
    private FontIcon editDeckIcon;
    @FXML
    private FontIcon removeDeckIcon;
    @FXML
    private FontIcon shareDeckIcon;
    @FXML
    private Rectangle colorRect;
    @FXML
    private HBox tagsContainer;
    @FXML
    private HBox bandContainer;

    private Deck deck;

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    /* ====================================================================== */
    /*                           Updating Deck                                */
    /* ====================================================================== */

    public void setDeck(Deck deck) {
        this.deck = deck;
        this.updateDeckLabelName();

        this.setDeckColor();
        this.setBackGroundImage("file:res/img/tmpdeckimage.jpg");

        this.setTags();
    }

    private void setDeckColor() {
        colorRect.setArcHeight(40);
        colorRect.setArcWidth(40);
        colorRect.heightProperty().bind(backgroundImage.fitHeightProperty());
        colorRect.widthProperty().bind(backgroundImage.fitWidthProperty());
        Color color = Color.web(deck.getColor());
        colorRect.setFill(makeGradient(color));
    }

    private void setBackGroundImage(String filename) {
        // TODO: make image depend on deck image
        Image img = new Image(filename);
        backgroundImage.setImage(img);
        backgroundImage.setPreserveRatio(false);
        backgroundImage.fitWidthProperty().bind(stackPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(stackPane.heightProperty());

        // add clip to image so that it has rounded corner
        Rectangle clip = new Rectangle();
        clip.setArcHeight(40);
        clip.setArcWidth(40);
        clip.heightProperty().bind(backgroundImage.fitHeightProperty());
        clip.widthProperty().bind(backgroundImage.fitWidthProperty());
        backgroundImage.setClip(clip);
    }

    private void setTags() {
        Label tag1 = new Label("Tag1");
        Label tag2 = new Label("Tag22");
        Label tag3 = new Label("Tag333");
        Label tag4 = new Label("Tag4444");
        Label tag5 = new Label("Tag55555");

        Background background = new Background(new BackgroundFill(
                Color.web("#FFFFFF"),
                new CornerRadii(10, false),
                new Insets(-2)));

        tag1.setBackground(background);
        tag2.setBackground(background);
        tag3.setBackground(background);
        tag4.setBackground(background);
        tag5.setBackground(background);

        this.tagsContainer.setSpacing(20);

        this.tagsContainer.getChildren().add(tag1);
        this.tagsContainer.getChildren().add(tag2);
        this.tagsContainer.getChildren().add(tag3);
        this.tagsContainer.getChildren().add(tag4);
        this.tagsContainer.getChildren().add(tag5);
    }

    private LinearGradient makeGradient(Color color) {
        float gradientHeight = 0.6f;
        float gradientStrengthInverted = 1.2f;

        Stop[] stops = { new Stop(0, color),
                         new Stop(gradientHeight, Color.web("#FFFFFF00"))};

        return new LinearGradient(
                1,
                gradientStrengthInverted,
                1,
                0,
                true,
                CycleMethod.NO_CYCLE,
                stops);

    }

    private void updateDeckLabelName() {
        this.playDeckLabel.setText(this.deck.getName());
    }


    /* ====================================================================== */
    /*                             Click handlers                             */
    /* ====================================================================== */

    @FXML
    private void handleEditDeckClicked() {
        listener.editDeckClicked(deck);
    }

    @FXML
    private void handleDoubleDeckClicked() {
        listener.deckDoubleClicked(deck);
    }

    @FXML
    private void handleRemoveDeckClicked() {
        listener.deckRemoved(deck);
    }

    @FXML
    private void handleShareDeckClicked() {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose a directory to share your deck");

        File file = directoryChooser.showDialog(
                stackPane.getParent()
                                    .getScene()
                                    .getWindow()
                    );

        listener.shareDeckClicked(deck, file);
    }


    /* ====================================================================== */
    /*                             Hover handlers                             */
    /* ====================================================================== */

    @FXML
    private void handleHoverEditDeck() {
        editDeckIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    private void handleHoverEditDeckExit() {
        editDeckIcon.setIconColor(Color.web("#000000"));
    }

    @FXML
    private void handleHoverRemoveDeck() {
        removeDeckIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    private void handleHoverRemoveDeckExit() {
        removeDeckIcon.setIconColor(Color.web("#000000"));
    }

    @FXML
    private void handleHoverShareDeck() {
        shareDeckIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    private void handleHoverShareDeckExit() {
        shareDeckIcon.setIconColor(Color.web("#000000"));
    }


    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface Listener {
        void deckRemoved(Deck deck);
        void deckDoubleClicked(Deck deck);
        void editDeckClicked(Deck deck);
        void shareDeckClicked(Deck deck, File file);
    }
}
