package ulb.infof307.g01.gui.controller;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;
import ulb.infof307.g01.gui.controller.errorhandler.ErrorHandler;
import ulb.infof307.g01.gui.httpdao.dao.DeckDAO;
import ulb.infof307.g01.gui.httpdao.dao.UserSessionDAO;
import ulb.infof307.g01.gui.httpdao.dao.LeaderboardDAO;
import ulb.infof307.g01.gui.util.ImageLoader;
import ulb.infof307.g01.gui.view.userauth.UserAuthViewController;
import ulb.infof307.g01.model.card.Card;
import ulb.infof307.g01.model.deck.Deck;
import ulb.infof307.g01.gui.view.deckmenu.DeckMenuViewController;
import ulb.infof307.g01.gui.view.deckmenu.DeckMenuViewController.SearchType;
import ulb.infof307.g01.gui.view.deckmenu.DeckViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.model.deck.DeckMetadata;
import ulb.infof307.g01.model.deck.Score;


import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Class responsible for display the deck menu and listening to
 * DeckMenuViewController
 */
public class DeckMenuController implements DeckMenuViewController.Listener,
        DeckViewController.Listener
        {

    private final Stage stage;

    private final ControllerListener controllerListener;

    private final DeckMenuViewController deckMenuViewController;
    private final MainWindowViewController mainWindowViewController;








    private final ErrorHandler errorHandler;

    private final DeckDAO deckDAO;
    private final LeaderboardDAO leaderboardDAO;
    private final UserSessionDAO userSessionDAO;
    private final ImageLoader imageLoader = new ImageLoader();

    /* ====================================================================== */
    /*                              Constructor                               */
    /* ====================================================================== */

    public DeckMenuController(Stage stage,
                              ErrorHandler errorHandler,
                              ControllerListener controllerListener,
                              MainWindowViewController mainWindowViewController,
                              DeckDAO deckDAO, UserSessionDAO userSessionDAO,
                              LeaderboardDAO leaderboardDAO) throws IOException, InterruptedException {

        this.stage = stage;

        this.errorHandler = errorHandler;

        this.controllerListener = controllerListener;
        this.mainWindowViewController = mainWindowViewController;


        this.deckDAO = deckDAO;
        this.userSessionDAO = userSessionDAO;
        this.deckDAO.setToken(userSessionDAO.getToken());

        this.leaderboardDAO = leaderboardDAO;
        this.leaderboardDAO.setToken(userSessionDAO.getToken());

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

        if (userSessionDAO.isLoggedIn()) {
            deckDAO.setToken(userSessionDAO.getToken());
            showDecks();
            mainWindowViewController.setDeckMenuViewVisible();
            mainWindowViewController.makebottomNavigationBarVisible();
            mainWindowViewController.makeTopNavigationBarVisible();
        }

        else {
            mainWindowViewController.setUserAuthViewController();
            mainWindowViewController.makebottomNavigationBarInvisible();
            mainWindowViewController.makeTopNavigationBarInvisible();
            mainWindowViewController.setInfoAppViewVisible();

            //mainWindowViewController.setUserAuthViewController();
        }

        mainWindowViewController.makeGoBackIconInvisible();
        //mainWindowViewController.m
        stage.show();
    }

    private void showDecks() throws IOException, InterruptedException {
        deckMenuViewController.setDecks(loadDecks(deckDAO.getAllDecksMetadata()));
    }


    /* ====================================================================== */
    /*                          Database Access                               */
    /* ====================================================================== */

    /**
     * @return List of loaded nodes representing decks
     * @throws IOException if FXMLLoader.load() fails
     */
    private List<Node> loadDecks(List<DeckMetadata> decks) throws IOException, InterruptedException {
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
            Score bestScore = leaderboardDAO.getBestScoreForDeck(deck.id());
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
            if (!isDeckNameValid(name) || deckDAO.deckExists(name))
                return;

            deckDAO.saveDeck(new Deck(name));
            showDecks();

        } catch (IOException e) {
            errorHandler.failedLoading(e);

        } catch (InterruptedException e) {
            errorHandler.savingError(e);
        }

    }

    @Override
    public void searchDeckClicked(String name) {
        try {
            List<DeckMetadata> decks = null;
            if (deckMenuViewController.getSearchType().equals(SearchType.Name)) {
                decks = deckDAO.searchDecks(name);
            } else if (deckMenuViewController.getSearchType().equals(SearchType.Tag)) {
                decks = deckDAO.searchDecksByTags(name);
            }
            assert decks != null;
            deckMenuViewController.setDecks(loadDecks(decks));

        } catch (IOException e) {
            errorHandler.failedLoading(e);

        } catch (InterruptedException e) {
            errorHandler.savingError(e);
        }
    }

    @Override
    public void deckRemoved(DeckMetadata deck) {
        try {
            deckDAO.deleteDeck(deck);
            showDecks();

        } catch (IOException e) {
            errorHandler.failedLoading(e);

        } catch (InterruptedException e) {
            errorHandler.savingError(e);
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
            errorHandler.failedDeckImportError(e);

        } catch (IOException e) {
            errorHandler.failedLoading(e);

        } catch (InterruptedException e) {
            errorHandler.savingError(e);
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

            Deck deck = deckDAO.getDeck(deckMetadata).orElse(null);

            assert deck != null;

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

        } catch (IOException | InterruptedException e) {
            errorHandler.failedDeckExportError(e);
        }
    }


    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void editDeckClicked(DeckMetadata deck);
        void playDeckClicked(DeckMetadata deck);
    }
}
