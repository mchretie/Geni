package ulb.infof307.g01.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;
import ulb.infof307.g01.database.DeckDAO;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.view.deckmenu.DeckMenuViewController;
import ulb.infof307.g01.view.deckmenu.DeckViewController;
import ulb.infof307.g01.view.mainwindow.MainWindowViewController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for display the deck menu and listening to DeckMenuViewController
 */
public class DeckMenuController implements DeckMenuViewController.Listener, DeckViewController.Listener {

    private final Stage stage;

    private final ControllerListener controllerListener;

    private final DeckMenuViewController deckMenuViewController;
    private final MainWindowViewController mainWindowViewController;

    private final DeckDAO dm = DeckDAO.singleton();

    /* ============================================================================================================== */
    /*                                                  Constructor                                                   */
    /* ============================================================================================================== */

    public DeckMenuController(Stage stage, ControllerListener controllerListener, MainWindowViewController mainWindowViewController) {
        this.stage = stage;
        this.controllerListener = controllerListener;
        this.mainWindowViewController = mainWindowViewController;


        this.deckMenuViewController = mainWindowViewController.getDeckMenuViewController();
        deckMenuViewController.setListener(this);
    }


    /* ============================================================================================================== */
    /*                                            Stage manipulation                                                  */
    /* ============================================================================================================== */

    /**
     * Loads and displays the Deck Menu onto the main scene
     *
     * @throws IOException if FXMLLoader.load() fails
     */
    public void show() throws IOException {
        deckMenuViewController.setDecks( loadDecks() );

        mainWindowViewController.setDeckMenuViewVisible();
        mainWindowViewController.makeGoBackIconInvisible();

        stage.show();
    }


    /* ============================================================================================================== */
    /*                                              Database Access                                                   */
    /* ============================================================================================================== */

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
        try {
            dm.deleteDeck(deck);
            deckMenuViewController.setDecks( loadDecks() );

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
