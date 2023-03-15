package ulb.infof307.g01.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.infof307.g01.database.DeckManager;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.view.editdeck.EditDeckViewController;

import java.io.IOException;

public class EditDeckController implements EditDeckViewController.Listener {


    private final ControllerListener controllerListener;
    private final Stage stage;
    private final DeckManager dm = DeckManager.singleton();

    private Deck deck;

    public EditDeckController(Stage stage, Deck deck, ControllerListener controllerListener) {
        this.stage = stage;
        this.deck = deck;
        this.controllerListener = controllerListener;
    }


    /**
     * Loads and displays the Deck Menu onto the main scene
     *
     * @throws IOException if FXMLLoader.load() fails
     */
    public void show() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EditDeckViewController.class.getResource("EditDeckView.fxml"));
        Parent root = fxmlLoader.load();

        EditDeckViewController editDeckViewController = fxmlLoader.getController();
        editDeckViewController.setListener(this);

        stage.setScene(new Scene(root));
        stage.show();
    }


    /* ============================================================================================================== */
    /*                                          View Listener Methods                                                 */
    /* ============================================================================================================== */

    @Override
    public void deckNameModified(String newName) {

    }

    @Override
    public void tagAddedToDeck(Deck deck, String tagName) {

    }

    @Override
    public void frontOfCardModified(Card card, String newFront) {

    }

    @Override
    public void backOfCardModified(Card card, String newBack) {

    }


    /* ============================================================================================================== */
    /*                                      Controller Listener Interface                                             */
    /* ============================================================================================================== */

    public interface ControllerListener {

    }
}
