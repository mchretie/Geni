package ulb.infof307.g01.gui.view.marketplace;


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
import ulb.infof307.g01.gui.http.exceptions.ServerCommunicationFailedException;
import ulb.infof307.g01.gui.util.ImageLoader;
import ulb.infof307.g01.model.deck.MarketplaceDeckMetadata;
import ulb.infof307.g01.model.deck.Score;
import ulb.infof307.g01.model.deck.Tag;
import java.util.List;



public class DeckMarketplaceViewController {

    /* ====================================================================== */
    /*                              FXML Attributes                           */
    /* ====================================================================== */
    @FXML
    private StackPane stackPane;

    @FXML
    private Label deckNameLabel;

    @FXML
    private FontIcon addRemoveDeckIcon;

    @FXML
    private ImageView imageBackground;

    @FXML
    private Rectangle colorBackground;

    @FXML
    private Label amountCardsLabel;

    @FXML
    private Label scoreLabel;

    @FXML
    private Label creatorLabel;

    @FXML
    private FlowPane tagsContainer;


    /* ====================================================================== */
    /*                              Model Attributes                          */
    /* ====================================================================== */

    private MarketplaceDeckMetadata deck;

    public enum DeckAvailability {
        OWNED,
        MISSING
    }

    private DeckAvailability deckAvailability;


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

    public void setDeck(MarketplaceDeckMetadata deck, Score bestScore, DeckAvailability deckAvailability) {
        this.deck = deck;
        this.deckAvailability = deckAvailability;

        this.updateDeckLabelName();
        this.setDeckImage();
        this.setDeckColor();

        changeDeckAvailabilityIcon();

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

        List<Tag> tags= deck.tags();

        int i =0;

        while (i < 10 && i < tags.size()) {
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

    private void setStats(String bestScore) {
        amountCardsLabel.setText(String.valueOf(deck.cardCount()));
        scoreLabel.setText(bestScore);
        creatorLabel.setText(deck.owner());
    }

    private void changeDeckAvailabilityIcon(){
        if (this.deckAvailability == DeckAvailability.OWNED) {
            addRemoveDeckIcon.setIconLiteral("mdi2b-bookmark-check");
        } else
            addRemoveDeckIcon.setIconLiteral("mdi2b-bookmark-plus");
    }

    /* ====================================================================== */
    /*                             Click handlers                             */
    /* ====================================================================== */

    @FXML
    private void handleAddRemoveDeckClicked() throws ServerCommunicationFailedException {
        listener.addRemoveDeckClicked(deck, this.deckAvailability);

        if (this.deckAvailability == DeckAvailability.OWNED) {
            this.deckAvailability = DeckAvailability.MISSING;
        } else
            this.deckAvailability = DeckAvailability.OWNED;

        changeDeckAvailabilityIcon();
    }


    /* ====================================================================== */
    /*                             Hover handlers                             */
    /* ====================================================================== */

    @FXML
    private void handleAddRemoveDeckHover() {
        if (this.deckAvailability == DeckAvailability.OWNED) {
            addRemoveDeckIcon.setIconLiteral("mdi2b-bookmark-minus");
        } else
            addRemoveDeckIcon.setIconLiteral("mdi2b-bookmark-check");
    }

    @FXML
    private void handleAddRemoveDeckExit() {
        if (this.deckAvailability == DeckAvailability.OWNED) {
            addRemoveDeckIcon.setIconLiteral("mdi2b-bookmark-check");
        } else
            addRemoveDeckIcon.setIconLiteral("mdi2b-bookmark-plus");
    }

    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface Listener {
        void addRemoveDeckClicked(MarketplaceDeckMetadata deck, DeckAvailability deckAvailability)
                throws ServerCommunicationFailedException;
    }
}
