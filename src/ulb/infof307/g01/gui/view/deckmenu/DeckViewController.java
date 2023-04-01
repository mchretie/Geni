package ulb.infof307.g01.gui.view.deckmenu;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.Deck;

public class DeckViewController {

    @FXML
    private StackPane stackPane;
    @FXML
    private ImageView imageView;
    @FXML
    private Label playDeckLabel;
    @FXML
    private VBox deckVBox;
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

//        this.setDeckColor();
        this.setBackGroundImage();
    }

    private void setDeckColor() {
        // TODO: make gradient color depend on deck color

//        Color color
//                = Color.color(Math.random(), Math.random(), Math.random());
//
//        deckGradientRect.setFill(makeGradient(color));
    }

    private void setBackGroundImage() {
        // TODO: make image depend on deck image
        Image img = new Image("file:res/img/tmpdeckimage.jpg");
        imageView.setImage(img);
        imageView.setPreserveRatio(false);
        imageView.fitWidthProperty().bind(stackPane.widthProperty());
        imageView.fitHeightProperty().bind(stackPane.heightProperty());
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
