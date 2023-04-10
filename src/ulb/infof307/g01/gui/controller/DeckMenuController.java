package ulb.infof307.g01.gui.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;
import ulb.infof307.g01.gui.httpdao.dao.DeckDAO;
import ulb.infof307.g01.gui.httpdao.dao.UserDAO;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.gui.view.deckmenu.DeckMenuViewController;
import ulb.infof307.g01.gui.view.deckmenu.DeckViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;

import java.io.IOException;
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

        // Guest Check
        if (!controllerListener.isGuestSession()) {
            this.deckDAO.setToken(userDAO.getToken());
        }

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
        System.out.println("DeckMenuController.show()");

        // Guest Check
        if (!controllerListener.isGuestSession()) {
            System.out.println("DeckMenuController.show() : not guest");
            deckMenuViewController.setDecks(loadDecks(deckDAO.getAllDecks()));
            mainWindowViewController.setEditCardViewVisible();
        }
        // Guest Check
        deckMenuViewController.setGuestMode(controllerListener.isGuestSession());

        mainWindowViewController.setDeckMenuViewVisible();
        mainWindowViewController.makeGoBackIconInvisible();

        stage.show();
        }


        /* ====================================================================== */
        /*                          Database Access                               */
        /* ====================================================================== */

        /**
         * @return List of loaded nodes representing decks
         * @throws IOException if FXMLLoader.load() fails
         */
        private List<Node> loadDecks (List < Deck > decks) throws IOException {
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
        public void createDeckClicked (String name){

            if (name.isEmpty())
                return;

            try {
                deckDAO.saveDeck(new Deck(name));
                deckMenuViewController.setDecks(loadDecks(deckDAO.getAllDecks()));

            } catch (IOException e) {
                controllerListener.fxmlLoadingError(e);

            } catch (InterruptedException e) {
                controllerListener.savingError(e);
            }

        }

        @Override
        public void searchDeckClicked (String name){
            try {
                deckMenuViewController.setDecks(loadDecks(deckDAO.searchDecks(name)));

            } catch (IOException e) {
                controllerListener.fxmlLoadingError(e);

            } catch (InterruptedException e) {
                controllerListener.savingError(e);
            }
        }

        @Override
        public void deckRemoved (Deck deck){
            try {
                deckDAO.deleteDeck(deck);
                deckMenuViewController.setDecks(loadDecks(deckDAO.getAllDecks()));

            } catch (IOException e) {
                controllerListener.fxmlLoadingError(e);

            } catch (InterruptedException e) {
                controllerListener.savingError(e);
            }
        }

        @Override
        public void deckDoubleClicked (Deck deck){
            controllerListener.playDeckClicked(deck);
        }

        @Override
        public void editDeckClicked (Deck deck){
            controllerListener.editDeckClicked(deck);
        }

        /* ====================================================================== */
        /*                   Controller Listener Interface                        */
        /* ====================================================================== */

        public interface ControllerListener {
            void editDeckClicked(Deck deck);

            void playDeckClicked(Deck deck);

            void fxmlLoadingError(IOException e);

            void savingError(Exception e);

            boolean isGuestSession();
        }
    }
