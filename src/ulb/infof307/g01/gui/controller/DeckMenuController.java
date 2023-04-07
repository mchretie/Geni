package ulb.infof307.g01.gui.controller;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;
import ulb.infof307.g01.gui.httpdao.dao.DeckDAO;
import ulb.infof307.g01.gui.httpdao.dao.UserDAO;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.gui.view.deckmenu.DeckMenuViewController;
import ulb.infof307.g01.gui.view.deckmenu.DeckViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.model.DeckMetadata;

import java.io.*;
import java.net.URL;
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

    private final DeckDAO deckDAO;

    /* ====================================================================== */
    /*                              Constructor                               */
    /* ====================================================================== */

    public DeckMenuController(Stage stage,
                              ControllerListener controllerListener,
                              MainWindowViewController mainWindowViewController,
                              DeckDAO deckDAO, UserDAO userDAO) throws IOException, InterruptedException {

        this.stage = stage;
        this.controllerListener = controllerListener;
        this.mainWindowViewController = mainWindowViewController;

        this.deckDAO = deckDAO;
        this.deckDAO.setToken(userDAO.getToken());

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
    public void show() throws IOException, InterruptedException {
        showDecks();

        mainWindowViewController.setDeckMenuViewVisible();
        mainWindowViewController.makeGoBackIconInvisible();

        stage.show();
    }

    private void showDecks() throws IOException, InterruptedException {
        deckMenuViewController.setDecks(loadDecks(deckDAO.getAllDecks()));
    }


    /* ====================================================================== */
    /*                          Database Access                               */
    /* ====================================================================== */

    /**
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
            controller.setDeck(deck.getMetadata());
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
            if (name.contains(String.valueOf(c))) {
                controllerListener.invalidDeckName(name, c);
                return false;
            }
        }

        return true;
    }


    /* ====================================================================== */
    /*                        View Listener Method                            */
    /* ====================================================================== */

    @Override
    public void createDeckClicked(String name) {

        if (!isDeckNameValid(name))
            return;

        try {
            deckDAO.saveDeck(new Deck(name));
            showDecks();

        } catch (IOException e) {
            controllerListener.fxmlLoadingError(e);

        } catch (InterruptedException e) {
            controllerListener.savingError(e);
        }

    }

    @Override
    public void searchDeckClicked(String name) {
        try {
            deckMenuViewController.setDecks(loadDecks(deckDAO.searchDecks(name)));

        } catch (IOException e) {
            controllerListener.fxmlLoadingError(e);

        } catch (InterruptedException e) {
            controllerListener.savingError(e);
        }
    }

    @Override
    public void deckRemoved(DeckMetadata deck) {
        try {
            deckDAO.deleteDeck(deck);
            showDecks();

        } catch (IOException e) {
            controllerListener.fxmlLoadingError(e);

        } catch (InterruptedException e) {
            controllerListener.savingError(e);
        }
    }

    @Override
    public void deckDoubleClicked(DeckMetadata deck) {
        controllerListener.playDeckClicked(deck);
    }

    @Override
    public void editDeckClicked(DeckMetadata deck) {
        controllerListener.editDeckClicked(deck);
    }

    @Override
    public void deckImported(File file) {
        if (file == null)
            return;

        try {
            JsonReader reader = new JsonReader(new FileReader(file));
            Deck deck = new Gson().fromJson(reader, Deck.class);

            deck.setNewID();
            for (Card card : deck.getCards())
                card.setNewId();

            assignNameIfExists(deck);
            deckDAO.saveDeck(deck);

            showDecks();

        } catch (JsonSyntaxException e) {
            controllerListener.failedImport(e);

        } catch (IOException e) {
            controllerListener.fxmlLoadingError(e);

        } catch (InterruptedException e) {
            controllerListener.savingError(e);
        }
    }

    /**
     * Assigns a name to the deck if it already exists. The name will be
     *  the same as the original name with a number in parentheses.
     *
     * @param deck the deck to assign a name to
     */
    private void assignNameIfExists(Deck deck) throws IOException, InterruptedException {
        int i = 1;

        if (deckDAO.deckExists(deck.getName())
                && !deck.getName().contains("(" + i + ")"))

            deck.setName(deck.getName() + " (" + i + ")");

        while (deckDAO.deckExists(deck.getName())) {
            String current = "(" + i + ")";
            String next = "(" + (i + 1) + ")";
            deck.setName(deck.getName().replace(current, next));

            i++;
        }
    }

    @Override
    public void shareDeckClicked(DeckMetadata deckMetadata, File file) {
        if (file == null || !file.isDirectory())
            return;

        try {
            Deck deck = deckDAO.getDeck(deckMetadata);
            String fileName
                    = deck.getName()
                          .replace(" ", "_")
                          .toLowerCase();

            String filePath
                    = file.getAbsoluteFile() + "/" + fileName + ".json";

            FileWriter fileWriter = new FileWriter(filePath);

            new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .setPrettyPrinting()
                    .create()
                    .toJson(deck, Deck.class, fileWriter);

            fileWriter.flush();

        } catch (IOException e) {
            controllerListener.failedExport(e);
            e.printStackTrace();
        } catch (InterruptedException e) {
            controllerListener.failedFetch(e);
            e.printStackTrace();
        }
    }


    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void editDeckClicked(DeckMetadata deck);

        void playDeckClicked(DeckMetadata deck);

        void fxmlLoadingError(IOException e);
        void savingError(Exception e);
        void failedExport(IOException e);
        void failedImport(JsonSyntaxException e);
        void invalidDeckName(String name, char c);

        void failedFetch(InterruptedException e);
    }
}
