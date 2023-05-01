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
import ulb.infof307.g01.gui.http.exceptions.ServerCommunicationFailedException;
import ulb.infof307.g01.gui.util.ImageLoader;
import ulb.infof307.g01.model.deck.MarketplaceDeckMetadata;
import ulb.infof307.g01.model.deck.Score;

import java.io.IOException;

public class DeckUserMarketplaceViewController {

    /* ====================================================================== */
    /*                              FXML Attributes                           */
    /* ====================================================================== */
    @FXML
    private StackPane stackPane;

    @FXML
    private Label deckNameLabel;

    @FXML
    private FontIcon removeDeckIcon;

    @FXML
    private ImageView imageBackground;

    @FXML
    private Rectangle colorBackground;

    @FXML
    private Label amountCardsLabel;

    @FXML
    private Label scoreLabel;


    /* ====================================================================== */
    /*                              Model Attributes                          */
    /* ====================================================================== */

    private MarketplaceDeckMetadata deck;


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

    public void setDeck(MarketplaceDeckMetadata deck, Score bestScore) {
        this.deck = deck;

        this.updateDeckLabelName();
        this.setDeckImage();
        this.setDeckColor();

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
    }


    /* ====================================================================== */
    /*                             Click handlers                             */
    /* ====================================================================== */

    @FXML
    private void handleRemoveDeckClicked() throws ServerCommunicationFailedException, IOException {
        listener.removeDeckClicked(deck);

    }


    /* ====================================================================== */
    /*                             Hover handlers                             */
    /* ====================================================================== */

    @FXML
    private void handleRemoveDeckHover() {
        removeDeckIcon.setIconLiteral("mdi2b-bookmark-minus");
    }

    @FXML
    private void handleRemoveDeckExit() {
        removeDeckIcon.setIconLiteral("mdi2b-bookmark-check");
    }

    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface Listener {
        void removeDeckClicked(MarketplaceDeckMetadata deck)
                throws ServerCommunicationFailedException, IOException;
    }
}
