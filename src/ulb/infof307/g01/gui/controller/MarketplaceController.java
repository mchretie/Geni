package ulb.infof307.g01.gui.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.stage.Stage;
import ulb.infof307.g01.gui.controller.errorhandler.ErrorHandler;
import ulb.infof307.g01.gui.util.ImageLoader;
import ulb.infof307.g01.gui.view.deckmenu.DeckMenuViewController;
import ulb.infof307.g01.gui.view.deckmenu.DeckViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.gui.view.marketplace.DeckMarketplaceViewController;
import ulb.infof307.g01.gui.view.marketplace.MarketplaceViewController;
import ulb.infof307.g01.model.deck.DeckMetadata;
import ulb.infof307.g01.model.deck.Score;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MarketplaceController implements MarketplaceViewController.Listener {
    private final Stage stage;
    private final ControllerListener controllerListener;
    private final MarketplaceViewController marketplaceViewController;
    private final MainWindowViewController mainWindowViewController;
    private final ErrorHandler errorHandler;
    private final ImageLoader imageLoader = new ImageLoader();

    public MarketplaceController(Stage stage,
                                 MainWindowViewController mainWindowViewController,
                                 ControllerListener controllerListener,
                                 ErrorHandler errorHandler){
        this.stage = stage;
        this.mainWindowViewController = mainWindowViewController;
        this.controllerListener = controllerListener;
        this.errorHandler = errorHandler;

        this.marketplaceViewController = mainWindowViewController.getMarketplaceViewController();

        marketplaceViewController.setListener(this);
    }


    /* ====================================================================== */
    /*                         Stage Manipulation                             */
    /* ====================================================================== */

    public void show() throws IOException, InterruptedException {
        //TODO
        mainWindowViewController.setMarketplaceViewVisible();
        marketplaceViewController.setDecks(loadDecks());
        stage.show();
    }


    /* ====================================================================== */
    /*                          Database Access                               */
    /* ====================================================================== */
    private List<Node> loadDecks() throws IOException, InterruptedException {
        List<Node> decksLoaded = new ArrayList<>();

        //decks.sort(Comparator.comparing(DeckMetadata::name));
        for (int x = 0; x < 10; x++) {
            URL resource = MarketplaceViewController
                    .class
                    .getResource("DeckMarketplaceView.fxml");

            FXMLLoader loader = new FXMLLoader(resource);

            Node node = loader.load();

            DeckMarketplaceViewController controller = loader.getController();
            controller.setImageLoader(imageLoader);

            // Score bestScore = scoreDAO.getBestScoreForDeck(deck.id()); //TODO
            //controller.setDeck(deck, bestScore);
            //controller.setListener(this);

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


    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {

    }
}
