package ulb.infof307.g01.gui.controller;

import com.google.gson.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;
import ulb.infof307.g01.gui.controller.errorhandler.ErrorHandler;
import ulb.infof307.g01.gui.http.ServerCommunicator;
import ulb.infof307.g01.gui.http.exceptions.ServerCommunicationFailedException;
import ulb.infof307.g01.gui.util.DeckIO;
import ulb.infof307.g01.gui.util.ImageLoader;
import ulb.infof307.g01.model.deck.Deck;
import ulb.infof307.g01.gui.view.deckmenu.DeckMenuViewController;
import ulb.infof307.g01.gui.view.deckmenu.DeckMenuViewController.SearchType;
import ulb.infof307.g01.gui.view.deckmenu.DeckViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.model.deck.DeckMetadata;
import ulb.infof307.g01.model.deck.Score;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
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

    private final ErrorHandler errorHandler;

    private final ServerCommunicator serverCommunicator;

    private final ImageLoader imageLoader = new ImageLoader();
    private final DeckIO deckIO = new DeckIO();

    /* ====================================================================== */
    /*                              Constructor                               */
    /* ====================================================================== */

    public DeckMenuController(Stage stage,
                              ErrorHandler errorHandler,
                              ControllerListener controllerListener,
                              MainWindowViewController mainWindowViewController,
                              ServerCommunicator serverCommunicator) {

        this.stage = stage;

        this.errorHandler = errorHandler;

        this.controllerListener = controllerListener;
        this.mainWindowViewController = mainWindowViewController;

        this.serverCommunicator = serverCommunicator;

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
    public void show() throws ServerCommunicationFailedException,
                                IOException,
                                InterruptedException {

        showDecks();
        mainWindowViewController.setDeckMenuViewVisible();
        mainWindowViewController.makebottomNavigationBarVisible();
        mainWindowViewController.makeTopNavigationBarVisible();

        mainWindowViewController.makeGoBackIconInvisible();
        stage.show();
    }

    private void showDecks() throws ServerCommunicationFailedException, IOException, InterruptedException {
        deckMenuViewController.setDecks(loadDecks(serverCommunicator.getAllDecksMetadata()));
    }


    /* ====================================================================== */
    /*                          Database Access                               */
    /* ====================================================================== */

    /**
     * @return List of loaded nodes representing decks
     * @throws IOException if FXMLLoader.load() fails
     */
    private List<Node> loadDecks(List<DeckMetadata> decks)
            throws ServerCommunicationFailedException, IOException, InterruptedException {
        List<Node> decksLoaded = new ArrayList<>();

        decks.sort(Comparator.comparing(DeckMetadata::name));

        for (DeckMetadata deck : decks) {

            URL resource = DeckMenuViewController
                    .class
                    .getResource("DeckView.fxml");

            FXMLLoader loader = new FXMLLoader(resource);

            Node node = loader.load();

            DeckViewController controller = loader.getController();
            controller.setImageLoader(imageLoader);

            controller.setDisableEdit(deck.isPublic());

            Score bestScore = serverCommunicator.getBestScoreForDeck(deck.id());

            controller.setDeck(deck, bestScore);
            controller.setListener(this);

            decksLoaded.add(node);
        }

        return decksLoaded;
    }


    /* ====================================================================== */
    /*                         Deck Name Validation                           */
    /* ====================================================================== */

    private boolean isDeckNameValid(String name) {

        if (name.isEmpty())
            return false;

        String bannedCharacters = "!\"#$%&()*+,./:;<=>?@[\\]^_`{}~";

        for (char c : bannedCharacters.toCharArray()) {
            if (!name.contains(String.valueOf(c)))
                continue;

            errorHandler.invalidDeckName(c);
            return false;
        }

        return true;
    }


    /* ====================================================================== */
    /*                        View Listener Method                            */
    /* ====================================================================== */

    @Override
    public void createDeckClicked(String name) {
        try {
            if (!isDeckNameValid(name) || serverCommunicator.deckNameExists(name))
                return;

            Deck deck = new Deck(name);
            serverCommunicator.saveDeck(deck);
            serverCommunicator.addDeckToCollection(deck.getId());
            showDecks();

        } catch (IOException | InterruptedException e) {
            errorHandler.failedLoading(e);

        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
        }
    }

    @Override
    public void searchDeckClicked(String name) {
        try {
            List<DeckMetadata> decks = null;
            if (deckMenuViewController.getSearchType().equals(SearchType.Name)) {
                decks = serverCommunicator.searchDecks(name);

            } else if (deckMenuViewController.getSearchType().equals(SearchType.Tag)) {
                decks = serverCommunicator.searchDecksByTags(name);
            }
            assert decks != null;
            deckMenuViewController.setDecks(loadDecks(decks));

        } catch (InterruptedException | IOException e) {
            errorHandler.failedLoading(e);

        } catch (ServerCommunicationFailedException e) {
            errorHandler.severConnectionError();
        }
    }

    @Override
    public void deckRemoved(DeckMetadata deck) {
        try {
            serverCommunicator.removeDeckFromCollection(deck.id());
            showDecks();

        } catch (InterruptedException | IOException e) {
            errorHandler.failedLoading(e);

        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
        }
    }

    @Override
    public void deckDoubleClicked(DeckMetadata deck) {
        controllerListener.deckClicked(deck);
    }

    @Override
    public void editDeckClicked(DeckMetadata deck) {
        controllerListener.editDeckClicked(deck);
    }

    @Override
    public void deckImportClicked() {
        try {
            deckIO.setAllDecks(serverCommunicator.getAllDecksMetadata());

            Path path = deckMenuViewController.pickOpenFile();
            Deck deck = deckIO.importFrom(path);

            serverCommunicator.saveDeck(deck);
            showDecks();

        } catch (JsonSyntaxException
                 | IllegalStateException
                 | IllegalArgumentException e) {

            errorHandler.failedDeckImportError(e);

        } catch (IOException | InterruptedException e) {
            errorHandler.failedLoading(e);

        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
        }
    }

    @Override
    public void shareDeckClicked(DeckMetadata deckMetadata) {
        try {
            Deck deck = serverCommunicator.getDeck(deckMetadata).orElse(null);
            assert deck != null;

            Path path = deckMenuViewController.pickSaveFile();
            deckIO.export(deck, path);

        } catch (IOException | IllegalArgumentException e) {
            errorHandler.failedDeckExportError(e);

        } catch (ServerCommunicationFailedException e) {
            errorHandler.failedServerCommunication(e);
        }
    }

    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void editDeckClicked(DeckMetadata deck);
        void deckClicked(DeckMetadata deck);
    }
}
