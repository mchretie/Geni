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
import ulb.infof307.g01.model.deck.Score;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MarketplaceController implements
        MarketplaceViewController.Listener,
        DeckMarketplaceViewController.Listener,
        DeckUserMarketplaceViewController.Listener {
    private final Stage stage;

    private final ErrorHandler errorHandler;
    private final MarketplaceViewController marketplaceViewController;
    private final MainWindowViewController mainWindowViewController;
    private ImageLoader imageLoader;

    private final ServerCommunicator serverCommunicator;

    public MarketplaceController(Stage stage,
                                 ErrorHandler errorHandler,
                                 MainWindowViewController mainWindowViewController,
                                 ServerCommunicator serverCommunicator) throws IOException, InterruptedException {

        this.stage = stage;
        this.errorHandler = errorHandler;
        this.mainWindowViewController = mainWindowViewController;
        this.serverCommunicator = serverCommunicator;

        this.marketplaceViewController = mainWindowViewController.getMarketplaceViewController();

        marketplaceViewController.setListener(this);
    }

    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }


    /* ====================================================================== */
    /*                         Stage Manipulation                             */
    /* ====================================================================== */

    public void show() throws ServerCommunicationFailedException, IOException, InterruptedException {
        mainWindowViewController.setMarketplaceViewVisible();

        List<MarketplaceDeckMetadata> marketplaceDecks = sortMarketplaceDecks(
                serverCommunicator.getAllMarketplaceDecks(),
                marketplaceViewController.getSortType());

        List<Node> decksMarketplace = loadDecksMarketplaceDatabase(marketplaceDecks);
        List<Node> decksUser = getMarketplaceDecksUser();

        marketplaceViewController.setDecksMarketplace(decksMarketplace);
        marketplaceViewController.setDecksUser(decksUser);
        stage.show();
    }

    private List<Node> getMarketplaceDecksUser() throws ServerCommunicationFailedException, IOException {
        return loadDecksUserView(serverCommunicator.searchDecksMarketplaceByCreator(serverCommunicator.getSessionUsername()));
    }

    // this method will take a list of marketplaceMetadata and sort it by the sortType
    private List<MarketplaceDeckMetadata> sortMarketplaceDecks(
            List<MarketplaceDeckMetadata> marketplaceDecks,
            MarketplaceViewController.SortType sortType)
                throws ServerCommunicationFailedException {

        switch (sortType) {
            case Nom:
                marketplaceDecks.sort((o1, o2) -> o1.name().compareToIgnoreCase(o2.name()));
                break;
            case Note:
                marketplaceDecks.sort((o1, o2) -> {
                    if (o1.rating() == o2.rating()) {
                        return 0;
                    }
                    return o1.rating() > o2.rating() ? -1 : 1;
                });
                break;
            case Découvrir:
                List<MarketplaceDeckMetadata> decksSaved = serverCommunicator.getSavedDecks();
                marketplaceDecks.removeAll(decksSaved);     // remove all the decks that are already saved
                marketplaceDecks.addAll(decksSaved);        // add them back at the end
                break;
        }

        return marketplaceDecks;
    }


    /* ====================================================================== */
    /*                          Database Access                               */
    /* ====================================================================== */
    private List<Node> loadDecksMarketplaceDatabase(List<MarketplaceDeckMetadata> marketplaceDecks)
            throws ServerCommunicationFailedException, IOException {

        List<MarketplaceDeckMetadata> decksSaved = serverCommunicator.getSavedDecks();
        decksSaved.retainAll(marketplaceDecks);

        List<Node> decksLoaded = new ArrayList<>();

        // use of a for loop to load the decks one by one, to avoid breaking the sorting
        for (MarketplaceDeckMetadata deck : marketplaceDecks) {
            List<MarketplaceDeckMetadata> singleDeckList = new ArrayList<>();
            singleDeckList.add(deck);

            if (decksSaved.contains(deck)) {
                decksLoaded.addAll(loadDecksViewMarketplace(singleDeckList, DeckAvailability.OWNED));
            } else {
                decksLoaded.addAll(loadDecksViewMarketplace(singleDeckList, DeckAvailability.MISSING));
            }
        }

        return decksLoaded;
    }

    private List<Node> loadDecksUserView(List<MarketplaceDeckMetadata> decksUser)
            throws ServerCommunicationFailedException, IOException {

        List<Node> decksLoaded = new ArrayList<>();

        HashMap<UUID, Score> bestScores = serverCommunicator.getBestScoreForMarketplaceDecks(decksUser);

        for (MarketplaceDeckMetadata deck : decksUser) {

            URL resource = MarketplaceViewController
                    .class
                    .getResource("DeckUserMarketplaceView.fxml");

            FXMLLoader loader = new FXMLLoader(resource);

            Node node = loader.load();

            DeckUserMarketplaceViewController controller = loader.getController();
            controller.setImageLoader(imageLoader);

            controller.setDeck(deck, bestScores.get(deck.id()));
            controller.setImageLoader(imageLoader);
            controller.setListener(this);

            decksLoaded.add(node);
        }

        return decksLoaded;
    }

    private List<Node> loadDecksViewMarketplace(List<MarketplaceDeckMetadata> decks, DeckAvailability deckAvailability)
            throws IOException, ServerCommunicationFailedException {
        List<Node> decksLoaded = new ArrayList<>();

        HashMap<UUID, Score> bestScores = serverCommunicator.getBestScoreForMarketplaceDecks(decks);

        for (MarketplaceDeckMetadata deck : decks) {
            URL resource = MarketplaceViewController
                    .class
                    .getResource("DeckMarketplaceView.fxml");

            FXMLLoader loader = new FXMLLoader(resource);

            Node node = loader.load();

            DeckMarketplaceViewController controller = loader.getController();
            controller.setImageLoader(imageLoader);

            controller.setDeck(deck, bestScores.get(deck.id()), deckAvailability);
            controller.setImageLoader(imageLoader);
            controller.setListener(this);

            controller.setRating(deck.rating()-1);

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
            } else if (marketplaceViewController.getSearchType().equals(MarketplaceViewController.SearchType.Tag)) {
                decks = serverCommunicator.searchDecksMarketplaceByTag(name);
            }
            assert decks != null;

            List<MarketplaceDeckMetadata> marketplaceDecks = sortMarketplaceDecks(
                    decks, marketplaceViewController.getSortType());

            marketplaceViewController.setDecksMarketplace(loadDecksMarketplaceDatabase(marketplaceDecks));

        } catch (IOException e) {
            errorHandler.failedLoading(e);

        } catch (ServerCommunicationFailedException e) {
            errorHandler.severConnectionError();
        }
    }

    @Override
    public void sortChoiceBoxChanged(MarketplaceViewController.SortType sortType) {
        try {
            this.show();
        } catch (IOException e) {
            errorHandler.failedLoading(e);
        } catch (ServerCommunicationFailedException | InterruptedException e) {
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

        List<MarketplaceDeckMetadata> marketplaceDecks = sortMarketplaceDecks(
                                                            serverCommunicator.getAllMarketplaceDecks(),
                                                            marketplaceViewController.getSortType());

        List<Node> decksMarketplace = loadDecksMarketplaceDatabase(marketplaceDecks);
        List<Node> decksUser = getMarketplaceDecksUser();
        marketplaceViewController.setDecksMarketplace(decksMarketplace);
        marketplaceViewController.setDecksUser(decksUser);
    }
}
