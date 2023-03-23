package ulb.infof307.g01.view.deckmenu;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.*;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.Deck;

import java.net.URL;

public class DeckViewController {

    @FXML
    private Button playDeckButton;

    @FXML
    public javafx.scene.shape.Rectangle deckRect;

    @FXML
    public javafx.scene.shape.Rectangle deckGradientRect;

    @FXML
    private FontIcon editDeckIcon;
    @FXML
    private FontIcon removeDeckIcon;
    @FXML
    private FontIcon shareDeckIcon;

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
        this.updateDeckButtonName();

        this.setDeckColor();
        this.setBackGroundImage();
    }

    private void setDeckColor() {
        // TODO: make gradient color depend on deck color

        Color color
                = Color.color(Math.random(), Math.random(), Math.random());

        deckGradientRect.setFill(makeGradient(color));
    }

    private void setBackGroundImage()  {
        // TODO: make image depend on deck image

        Image img = new Image("file:res/img/tmpdeckimage.jpg");
        deckRect.setFill(new ImagePattern(img));
        deckRect.setOpacity(0.7);
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

    private void updateDeckButtonName() {
        this.playDeckButton.setText(this.deck.getName());
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
    }
}
