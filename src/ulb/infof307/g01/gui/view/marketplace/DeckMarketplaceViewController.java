package ulb.infof307.g01.gui.view.marketplace;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.gui.util.ImageLoader;
import ulb.infof307.g01.model.deck.MarketplaceDeckMetadata;
import ulb.infof307.g01.model.deck.Score;

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

        if (this.deckAvailability == DeckAvailability.OWNED) {
            addRemoveDeckIcon.setIconLiteral("mdi2b-bookmark-check");
        } else
            addRemoveDeckIcon.setIconLiteral("mdi2b-bookmark-plus");

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
        System.out.println(deck.image());
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
    }

    private void setStats(String bestScore) {
        amountCardsLabel.setText(String.valueOf(deck.cardCount()));
        scoreLabel.setText(bestScore);
        creatorLabel.setText(deck.owner());
    }


    /* ====================================================================== */
    /*                             Click handlers                             */
    /* ====================================================================== */

    @FXML
    private void handleAddRemoveDeckClicked() {
        //TODO
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

    }
}
