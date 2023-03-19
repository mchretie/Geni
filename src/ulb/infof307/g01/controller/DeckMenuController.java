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
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for display the deck menu and listening to
 * DeckMenuViewController
 */
public class DeckMenuController implements DeckMenuViewController.Listener,
                                                DeckViewController.Listener {

    private final Stage stage;

    private final ControllerListener controllerListener;

    private final DeckMenuViewController deckMenuViewController;
    private final MainWindowViewController mainWindowViewController;

    private final DeckDAO dm = DeckDAO.singleton();

    /* ====================================================================== */
    /*                              Constructor                               */
    /* ====================================================================== */

    public DeckMenuController(Stage stage,
                              ControllerListener controllerListener,
                              MainWindowViewController mainWindowViewController) {

        this.stage = stage;
        this.controllerListener = controllerListener;
        this.mainWindowViewController = mainWindowViewController;


        this.deckMenuViewController
                = mainWindowViewController.getDeckMenuViewController();

        deckMenuViewController.setListener(this);
    }


    /* ====================================================================== */
    /*                         Stage Manipulation                             */
    /* ====================================================================== */

    /**
     * Loads and displays the Deck Menu onto the main scene
     *
     * @throws IOException if FXMLLoader.load() fails
     */
    public void show() throws IOException, SQLException {
        deckMenuViewController.setDecks( loadDecks( dm.getAllDecks() ) );

        mainWindowViewController.setDeckMenuViewVisible();
        mainWindowViewController.makeGoBackIconInvisible();

        stage.show();
    }


    /* ====================================================================== */
    /*                          Database Access                               */
    /* ====================================================================== */

    /**
     *
     * @return List of loaded nodes representing decks
     * @throws IOException if FXMLLoader.load() fails
     */
    private List<Node> loadDecks(List<Deck> decks) throws IOException {
        List<Node> decksLoaded = new ArrayList<>();

        for (Deck deck : decks) {

            URL resource = DeckMenuViewController
                                .class
                                .getResource("DeckView.fxml");

            FXMLLoader loader = new FXMLLoader(resource);

            Node node = loader.load();

            DeckViewController controller = loader.getController();
            controller.setDeck(deck);
            controller.setListener(this);

            decksLoaded.add(node);
        }

        return decksLoaded;
    }


    /* ====================================================================== */
    /*                        View Listener Method                            */
    /* ====================================================================== */

    @Override
    public void createDeckClicked(String name) {

        if (name.isEmpty())
            return;

        try {
            dm.saveDeck(new Deck(name));
            deckMenuViewController.setDecks( loadDecks( dm.getAllDecks() ) );

        } catch (IOException e) {
            controllerListener.fxmlLoadingError(e);

        } catch (SQLException e) {
            controllerListener.savingError(e);
        }

    }

    @Override
    public void searchDeckClicked(String name) {
        try {
            deckMenuViewController.setDecks( loadDecks( dm.searchDecks(name) ) );
        } catch (IOException e) {
            controllerListener.fxmlLoadingError(e);
        } catch (SQLException e) {
            controllerListener.savingError(e);
        }
    }

    @Override
    public void deckRemoved(Deck deck) {
        try {
            dm.deleteDeck(deck);
            deckMenuViewController.setDecks( loadDecks( dm.getAllDecks() ) );

        } catch (IOException e) {
            controllerListener.fxmlLoadingError(e);

        } catch (SQLException e) {
            controllerListener.savingError(e);
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


    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void editDeckClicked(Deck deck);
        void playDeckClicked(Deck deck);
        void fxmlLoadingError(IOException e);
        void savingError(SQLException e);
    }
}
