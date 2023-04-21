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
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.deck.DeckMetadata;
import ulb.infof307.g01.gui.util.ImageLoader;
import ulb.infof307.g01.model.deck.Score;
import ulb.infof307.g01.model.deck.Tag;

public class DeckViewController {

    /* ====================================================================== */
    /*                              FXML Attributes                           */
    /* ====================================================================== */

    @FXML
    private StackPane stackPane;

    @FXML
    private Label playDeckLabel;

    @FXML
    private FontIcon editDeckIcon;

    @FXML
    private FontIcon removeDeckIcon;

    @FXML
    private FontIcon shareDeckIcon;

    @FXML
    private ImageView imageBackground;

    @FXML
    private Rectangle colorBackground;

    @FXML
    private FlowPane tagsContainer;

    @FXML
    private Label amountCardsLabel;

    @FXML
    private Label amountTrophiesLabel;


    /* ====================================================================== */
    /*                              Model Attributes                          */
    /* ====================================================================== */

    private DeckMetadata deck;


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

    public void setDeck(DeckMetadata deck, Score bestScore) {
        this.deck = deck;
        this.updateDeckLabelName();

        this.setDeckImage();
        this.setDeckColor();

        this.setTags();
        if (bestScore == null)
            this.setStats("N/A");
        else
            this.setStats(String.valueOf(bestScore.getScore()));
    }

    private void setDeckColor() {
        colorBackground.setArcHeight(40);
        colorBackground.setArcWidth(40);
        colorBackground.heightProperty().bind(imageBackground.fitHeightProperty());
        colorBackground.widthProperty().bind(imageBackground.fitWidthProperty());
        Color color = Color.web(deck.color());
        colorBackground.setFill(makeGradient(color));
    }

    private void setDeckImage() {
        Image img = new Image(deck.image());
        imageBackground.setImage(img);
        imageBackground.setPreserveRatio(false);
        imageBackground.fitWidthProperty().bind(stackPane.widthProperty());
        imageBackground.fitHeightProperty().bind(stackPane.heightProperty());

        // add clip to image so that it has rounded corner
        Rectangle clip = new Rectangle();
        clip.setArcHeight(40);
        clip.setArcWidth(40);
        clip.heightProperty().bind(imageBackground.fitHeightProperty());
        clip.widthProperty().bind(imageBackground.fitWidthProperty());
        imageBackground.setClip(clip);
    }

    private void setTags() {
        tagsContainer.setHgap(30);
        tagsContainer.setVgap(10);

        for (Tag tag : deck.tags()) {
            Label tagLabel = new Label(tag.getName());

            tagLabel.setBackground(new Background(new BackgroundFill(
                    Color.web(tag.getColor()),
                    new CornerRadii(10, false),
                    new Insets(-2, -10, -2, -10))));

            tagLabel.setTextFill(tag.isBackgroundDark() ? Color.WHITE : Color.BLACK);

            tagsContainer.getChildren().add(tagLabel);
        }
    }

    private void setStats(String bestScore) {
        amountCardsLabel.setText(String.valueOf(deck.cardCount()));
        amountTrophiesLabel.setText(bestScore);
    }

    private LinearGradient makeGradient(Color color) {
        float gradientHeight = 0.6f;
        float gradientStrengthInverted = 1.2f;

        Stop[] stops = {new Stop(0, color),
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
        this.playDeckLabel.setText(this.deck.name());
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
        listener.shareDeckClicked(deck);
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
        void deckRemoved(DeckMetadata deck);

        void deckDoubleClicked(DeckMetadata deck);

        void editDeckClicked(DeckMetadata deck);

        void shareDeckClicked(DeckMetadata deck);
    }
}
