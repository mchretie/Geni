package ulb.infof307.g01.gui.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;
import ulb.infof307.g01.gui.controller.errorhandler.ErrorHandler;
import ulb.infof307.g01.gui.httpdao.dao.DeckDAO;
import ulb.infof307.g01.gui.httpdao.dao.MarketplaceDAO;
import ulb.infof307.g01.gui.httpdao.dao.ScoreDAO;
import ulb.infof307.g01.gui.httpdao.dao.UserSessionDAO;
import ulb.infof307.g01.gui.util.ImageLoader;
import ulb.infof307.g01.gui.view.deckmenu.DeckMenuViewController;
import ulb.infof307.g01.gui.view.deckmenu.DeckViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.gui.view.marketplace.DeckMarketplaceViewController;
import ulb.infof307.g01.gui.view.marketplace.DeckMarketplaceViewController.DeckAvailability;
import ulb.infof307.g01.gui.view.marketplace.MarketplaceViewController;
import ulb.infof307.g01.model.deck.DeckMetadata;
import ulb.infof307.g01.model.deck.Score;
import ulb.infof307.g01.model.deck.MarketplaceDeckMetadata;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MarketplaceController implements MarketplaceViewController.Listener, DeckMarketplaceViewController.Listener {
    private final Stage stage;
    private final ControllerListener controllerListener;
    private final MarketplaceViewController marketplaceViewController;
    private final MainWindowViewController mainWindowViewController;
    private final ErrorHandler errorHandler;
    private final DeckDAO deckDAO;
    private final UserSessionDAO userSessionDAO;
    private final ImageLoader imageLoader = new ImageLoader();
    private final MarketplaceDAO marketplaceDAO;
    private final ScoreDAO scoreDAO;

    public MarketplaceController(Stage stage,
                                 MainWindowViewController mainWindowViewController,
                                 ControllerListener controllerListener,
                                 ErrorHandler errorHandler,
                                 DeckDAO deckDAO,
                                 UserSessionDAO userSessionDAO,
                                 MarketplaceDAO marketplaceDAO,
                                 ScoreDAO scoreDAO) throws IOException, InterruptedException {
        this.stage = stage;
        this.mainWindowViewController = mainWindowViewController;
        this.controllerListener = controllerListener;
        this.errorHandler = errorHandler;
        this.deckDAO = deckDAO;
        this.userSessionDAO = userSessionDAO;
        this.marketplaceDAO = marketplaceDAO;
        this.scoreDAO = scoreDAO;

        this.marketplaceViewController = mainWindowViewController.getMarketplaceViewController();

        marketplaceViewController.setListener(this);
    }


    /* ====================================================================== */
    /*                         Stage Manipulation                             */
    /* ====================================================================== */

    public void show() throws IOException, InterruptedException {
        mainWindowViewController.setMarketplaceViewVisible();
        marketplaceViewController
                .setDecks(loadDecksDatabase());
        stage.show();
    }


    /* ====================================================================== */
    /*                          Database Access                               */
    /* ====================================================================== */
    private List<Node> loadDecksDatabase() throws IOException, InterruptedException {
        List<MarketplaceDeckMetadata> marketplaceDecks = marketplaceDAO.getAllMarketplaceDecks();
        List<MarketplaceDeckMetadata> decksSaved = marketplaceDAO.getSavedDecks();
        marketplaceDecks.removeAll(decksSaved);

        List<Node> decksLoaded = new ArrayList<>();
        decksLoaded.addAll(loadDecksView(marketplaceDecks, DeckAvailability.MISSING));
        decksLoaded.addAll(loadDecksView(decksSaved, DeckAvailability.OWNED));

        return decksLoaded;
    }
    private List<Node> loadDecksView(List<MarketplaceDeckMetadata> decks, DeckAvailability deckAvailability) throws IOException, InterruptedException {
        List<Node> decksLoaded = new ArrayList<>();

        for (MarketplaceDeckMetadata deck : decks) {
            URL resource = MarketplaceViewController
                    .class
                    .getResource("DeckMarketplaceView.fxml");

            FXMLLoader loader = new FXMLLoader(resource);

            Node node = loader.load();

            DeckMarketplaceViewController controller = loader.getController();

            controller.setDeck(deck, scoreDAO.getBestScoreForDeck(deck.id()), deckAvailability);
            controller.setImageLoader(imageLoader);
            controller.setListener(this);

            decksLoaded.add(node);
        }

        return decksLoaded;
    }

    /* ====================================================================== */
    /*                        View Listener Method                            */
    /* ====================================================================== */

    @Override
    public void searchDeckClicked(String name) {
        List<DeckMetadata> decks = null;
        if (marketplaceViewController.getSearchType().equals(MarketplaceViewController.SearchType.Name)) {
            //decks = //TODO
        } else if (marketplaceViewController.getSearchType().equals(MarketplaceViewController.SearchType.Creator)) {
            //decks = //TODO
        }
        assert decks != null;
        //deckMenuViewController.setDecks(loadDecks(decks)); //TODO

    }

    @Override
    public void addRemoveDeckClicked(MarketplaceDeckMetadata deck, DeckAvailability deckAvailability) throws IOException, InterruptedException {
        if (deckAvailability.equals(DeckAvailability.MISSING)) {
            deckDAO.addDeckToCollection(deck.id());
        } else {
            deckDAO.removeDeckFromCollection(deck.id());
        }
    }


    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {

    }
}
