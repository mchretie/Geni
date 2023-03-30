package ulb.infof307.g01.view.deckmenu;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.Deck;

public class DeckViewController {

    @FXML
    private Label playDeckLabel;

    @FXML
    private VBox deckVBox;
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

    private void setBackGroundImage()  {
        // TODO: make image depend on deck image
        BackgroundImage bckImage = new BackgroundImage(new Image("file:res/img/tmpdeckimage.jpg"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, true)
                );
//        deckVBox.setBackground(new Background(bckImage));
//        deckVBox.setStyle("-fx-background-radius: 20;");


//        deckVBox.setStyle("-fx-border-color: black; -fx-background-radius: 20; " +
//                "-fx-background-image:url('file:res/img/tmpdeckimage.jpg'); " +
//                "-fx-border-radius: 20;");
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
