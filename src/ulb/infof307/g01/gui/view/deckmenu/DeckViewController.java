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
import ulb.infof307.g01.gui.util.ImageLoader;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;

import java.io.File;

public class DeckViewController {

    /* ====================================================================== */
    /*                              FXML Attributes                           */
    /* ====================================================================== */

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
    private FlowPane tagsContainer;
    @FXML
    private Label amountCardsLabel;
    @FXML
    private Label amountTrophiesLabel;


    /* ====================================================================== */
    /*                              Model Attributes                          */
    /* ====================================================================== */

    private Deck deck;


    /* ====================================================================== */
    /*                                Listener                                */
    /* ====================================================================== */

    private Listener listener;
    private ImageLoader imageLoader;


    /* ====================================================================== */
    /*                                Setters                                 */
    /* ====================================================================== */

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setImageLoader(ImageLoader loader) {
        this.imageLoader = loader;
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
        this.setStats();
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
        Image img = imageLoader.get(filename);
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
        tagsContainer.setHgap(30);
        tagsContainer.setVgap(10);

        for (Tag tag : deck.getTags()) {
            Label tagLabel = new Label(tag.getName());

            tagLabel.setBackground(new Background(new BackgroundFill(
                    Color.web(tag.getColor()),
                    new CornerRadii(10, false),
                    new Insets(-2, -10, -2, -10))));

            tagsContainer.getChildren().add(tagLabel);
        }
    }

    private void setStats() {
        amountCardsLabel.setText(String.valueOf(deck.cardCount()));
        // TODO : use this when trophies are implemented
        //amountTrophiesLabel.setText(String.valueOf(deck.getTrophies()));
        amountTrophiesLabel.setText("666");
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
