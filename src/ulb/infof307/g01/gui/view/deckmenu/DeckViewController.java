package ulb.infof307.g01.gui.view.deckmenu;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.Deck;

import java.io.File;
import java.net.URL;

public class DeckViewController {

    @FXML
    private StackPane homeDeckPane;

    @FXML
    private Button playDeckButton;

    @FXML
    private Rectangle deckRect;
    @FXML
    private Rectangle deckGradientRect;

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
        deckGradientRect.setFill(makeGradient(Color.web(deck.getColor())));
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

    @FXML
    private void handleShareDeckClicked() {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose a directory to share your deck");

        File file = directoryChooser.showDialog(
                        homeDeckPane.getParent()
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
