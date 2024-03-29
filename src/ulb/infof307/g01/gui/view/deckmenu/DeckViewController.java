package ulb.infof307.g01.gui.view.deckmenu;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
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
import java.util.List;


public class DeckViewController {

    /* ====================================================================== */
    /*                              FXML Attributes                           */
    /* ====================================================================== */

    @FXML
    private Button editDeckButton;

    @FXML
    private StackPane stackPane;

    @FXML
    private Label deckNameLabel;

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
    private Label scoreLabel;


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
        Image img = imageLoader.get(deck.image());
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

        List<Tag> tags = deck.tags();

        int i =0;

        // 7 is the max number of tags we display
        while (i < 7 && i < tags.size()) {
            Tag tag = tags.get(i);
            Label tagLabel = new Label(tag.getName());

            tagLabel.setBackground(new Background(new BackgroundFill(
                    Color.web(tag.getColor()),
                    new CornerRadii(10, false),
                    new Insets(-2, -10, -2, -10))));

            tagLabel.setTextFill(tag.isBackgroundDark() ? Color.WHITE : Color.BLACK);

            tagsContainer.getChildren().add(tagLabel);
            i++;
        }
    }

    private void setStats(String bestScore) {
        amountCardsLabel.setText(String.valueOf(deck.cardCount()));
        scoreLabel.setText(bestScore);
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
        this.deckNameLabel.setText(this.deck.name());
        this.deckNameLabel.setTextFill(Color.web(this.deck.colorName()));
    }

    public void setDisableEdit(boolean disable) {
        editDeckButton.setDisable(disable);
    }


    /* ====================================================================== */
    /*                             Click handlers                             */
    /* ====================================================================== */

    @FXML
    private void handleEditDeckClicked() {
        listener.editDeckClicked(deck);
    }

    @FXML
    private void handleDeckClicked() {
        listener.deckClicked(deck);
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

    @FXML
    public void handleDeckMouseEnter() {
        stackPane.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.BLACK, 10, 0.5, 0, 0));
    }

    public void handleDeckMouseExit() {
        stackPane.setEffect(null);
    }


    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface Listener {
        void deckRemoved(DeckMetadata deck);

        void deckClicked(DeckMetadata deck);

        void editDeckClicked(DeckMetadata deck);

        void shareDeckClicked(DeckMetadata deck);
    }
}
