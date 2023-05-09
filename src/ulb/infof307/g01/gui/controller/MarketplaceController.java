package ulb.infof307.g01.gui.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;
import ulb.infof307.g01.gui.controller.errorhandler.ErrorHandler;
import ulb.infof307.g01.gui.http.ServerCommunicator;
import ulb.infof307.g01.gui.http.exceptions.ServerCommunicationFailedException;
import ulb.infof307.g01.gui.util.ImageLoader;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.gui.view.marketplace.DeckMarketplaceViewController;
import ulb.infof307.g01.gui.view.marketplace.DeckMarketplaceViewController.DeckAvailability;
import ulb.infof307.g01.gui.view.marketplace.DeckUserMarketplaceViewController;
import ulb.infof307.g01.gui.view.marketplace.MarketplaceViewController;
import ulb.infof307.g01.model.deck.DeckMetadata;
import ulb.infof307.g01.model.deck.MarketplaceDeckMetadata;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MarketplaceController implements
        MarketplaceViewController.Listener,
        DeckMarketplaceViewController.Listener,
        DeckUserMarketplaceViewController.Listener  {
    private final Stage stage;

    private final ErrorHandler errorHandler;
    private final MarketplaceViewController marketplaceViewController;
    private final MainWindowViewController mainWindowViewController;
    private final ImageLoader imageLoader = new ImageLoader();

    private final ServerCommunicator serverCommunicator;

    public MarketplaceController(Stage stage,
                                 ErrorHandler errorHandler,
                                 MainWindowViewController mainWindowViewController,
                                 ServerCommunicator serverCommunicator) {

        this.stage = stage;
        this.errorHandler = errorHandler;
        this.mainWindowViewController = mainWindowViewController;
        this.serverCommunicator = serverCommunicator;

        this.marketplaceViewController = mainWindowViewController.getMarketplaceViewController();

        marketplaceViewController.setListener(this);
    }


    /* ====================================================================== */
    /*                         Stage Manipulation                             */
    /* ====================================================================== */

    public void show() throws ServerCommunicationFailedException, IOException {
        mainWindowViewController.setMarketplaceViewVisible();

        List<Node> decksMarketplace = loadDecksMarketplaceDatabase(serverCommunicator.getAllMarketplaceDecks());
        List<Node> decksUser = getMarketplaceDecksUser();
        marketplaceViewController.setDecksMarketplace(decksMarketplace);
        marketplaceViewController.setDecksUser(decksUser);
        stage.show();
    }

    private List<Node> getMarketplaceDecksUser() throws ServerCommunicationFailedException, IOException {
        return loadDecksUserView(serverCommunicator.searchDecksMarketplaceByCreator(serverCommunicator.getSessionUsername()));
    }


    /* ====================================================================== */
    /*                          Database Access                               */
    /* ====================================================================== */
    private List<Node> loadDecksMarketplaceDatabase(List<MarketplaceDeckMetadata> marketplaceDecks)
            throws ServerCommunicationFailedException, IOException {

        List<MarketplaceDeckMetadata> decksSaved = serverCommunicator.getSavedDecks();
        decksSaved.retainAll(marketplaceDecks);
        marketplaceDecks.removeAll(decksSaved);

        List<Node> decksLoaded = new ArrayList<>();
        decksLoaded.addAll(loadDecksViewMarketplace(marketplaceDecks, DeckAvailability.MISSING));
        decksLoaded.addAll(loadDecksViewMarketplace(decksSaved, DeckAvailability.OWNED));

        return decksLoaded;
    }

    private List<Node> loadDecksUserView(List<MarketplaceDeckMetadata> decksUser)
            throws ServerCommunicationFailedException, IOException {

        List<Node> decksLoaded = new ArrayList<>();

        for (MarketplaceDeckMetadata deck : decksUser) {
            URL resource = MarketplaceViewController
                    .class
                    .getResource("DeckUserMarketplaceView.fxml");

            FXMLLoader loader = new FXMLLoader(resource);

            Node node = loader.load();

            DeckUserMarketplaceViewController controller = loader.getController();

            controller.setDeck(deck, serverCommunicator.getBestScoreForDeck(deck.id()));
            controller.setListener(this);

            decksLoaded.add(node);
        }

        return decksLoaded;
    }
    private List<Node> loadDecksViewMarketplace(List<MarketplaceDeckMetadata> decks, DeckAvailability deckAvailability)
            throws IOException, ServerCommunicationFailedException {
        List<Node> decksLoaded = new ArrayList<>();

        for (MarketplaceDeckMetadata deck : decks) {
            URL resource = MarketplaceViewController
                    .class
                    .getResource("DeckMarketplaceView.fxml");

            FXMLLoader loader = new FXMLLoader(resource);

            Node node = loader.load();

            DeckMarketplaceViewController controller = loader.getController();

            controller.setDeck(deck, serverCommunicator.getBestScoreForDeck(deck.id()), deckAvailability);
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
        try {
            List<MarketplaceDeckMetadata> decks = null;
            if (marketplaceViewController.getSearchType().equals(MarketplaceViewController.SearchType.Name)) {
                decks = serverCommunicator.searchDecksMarketplace(name);

            } else if (marketplaceViewController.getSearchType().equals(MarketplaceViewController.SearchType.Creator)) {
                decks = serverCommunicator.searchDecksMarketplaceByCreator(name);
            }
            assert decks != null;
            marketplaceViewController.setDecksMarketplace(loadDecksMarketplaceDatabase(decks));

        } catch (IOException e) {
            errorHandler.failedLoading(e);

        } catch (ServerCommunicationFailedException e) {
            errorHandler.severConnectionError();
        }
    }

    @Override
    public void addRemoveDeckClicked(MarketplaceDeckMetadata deck, DeckAvailability deckAvailability)
            throws ServerCommunicationFailedException {

        var deckMetadata = DeckMetadata.fromMarketplaceDeckMetadata(deck);

        if (deckAvailability.equals(DeckAvailability.MISSING)) {
            serverCommunicator.addMarketplaceDeckToCollection(deckMetadata);
        } else {
            serverCommunicator.removeDeckFromCollection(deckMetadata);
        }
    }

    @Override
    public void removeDeckClicked(MarketplaceDeckMetadata deck) throws ServerCommunicationFailedException, IOException {
        serverCommunicator.removeDeckFromMarketplace(deck);

        List<Node> decksMarketplace = loadDecksMarketplaceDatabase(serverCommunicator.getAllMarketplaceDecks());
        List<Node> decksUser = getMarketplaceDecksUser();
        marketplaceViewController.setDecksMarketplace(decksMarketplace);
        marketplaceViewController.setDecksUser(decksUser);
    }
}
