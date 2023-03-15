package ulb.infof307.g01.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.infof307.g01.database.DeckManager;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.view.deckmenu.DeckMenuViewController;
import ulb.infof307.g01.view.deckmenu.DeckViewController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeckMenuController implements DeckMenuViewController.Listener, DeckViewController.Listener {

    private final ControllerListener controllerListener;
    private final Stage stage;

    private DeckMenuViewController deckMenuViewController;

    private final DeckManager dm = DeckManager.singleton();

    public DeckMenuController(Stage stage, ControllerListener controllerListener) {
        this.stage = stage;
        this.controllerListener = controllerListener;
    }


    /**
     * Loads and displays the Deck Menu onto the main scene
     *
     * @throws IOException if FXMLLoader.load() fails
     */
    public void show() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DeckMenuViewController.class.getResource("DeckMenuView.fxml"));
        Parent root = fxmlLoader.load();

        deckMenuViewController = fxmlLoader.getController();
        deckMenuViewController.setListener(this);
        deckMenuViewController.setDecks( loadDecks() );

        stage.setScene(new Scene(root));
        stage.show();
    }


    /**
     *
     * @return List of loaded nodes representing decks
     * @throws IOException if FXMLLoader.load() fails
     */
    private List<Node> loadDecks() throws IOException {
        List<Node> decks = new ArrayList<>();

        for (Deck deck : dm.getAllDecks()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/deckmenu/DeckView.fxml"));
            Node node = loader.load();

            DeckViewController controller = loader.getController();
            controller.setDeck(deck);
            controller.setListener(this);

            decks.add(node);
        }

        return decks;
    }


    /* ============================================================================================================== */
    /*                                          View Listener Methods                                                 */
    /* ============================================================================================================== */

    @Override
    public void createDeckClicked(String name) {

        if (name.isEmpty())
            return;

        try {
            dm.saveDeck(new Deck(name));
            deckMenuViewController.setDecks( loadDecks() );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deckRemoved(Deck deck) {
        System.out.println("removed");
        try {
            dm.deleteDeck(deck);
            // deckMenuViewController.setDecks( loadDecks() );
            show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deckDoubleClicked(Deck deck) {
        controllerListener.playDeckClicked(deck);
    }

    @Override
    public void editDeckClicked(Deck deck) {
        controllerListener.editDeckClicked(deck);
    }


    /* ============================================================================================================== */
    /*                                      Controller Listener Interface                                             */
    /* ============================================================================================================== */

    public interface ControllerListener {
        void editDeckClicked(Deck deck);
        void playDeckClicked(Deck deck);
    }
}
