package ulb.infof307.g01.gui.controller;

import javafx.stage.Stage;
import ulb.infof307.g01.gui.controller.errorhandler.ErrorHandler;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.gui.view.marketplace.MarketplaceViewController;

import java.io.IOException;

public class MarketplaceController implements MarketplaceViewController.Listener {
    private final Stage stage;
    private final ControllerListener controllerListener;
    private final MarketplaceViewController marketplaceViewController;
    private final MainWindowViewController mainWindowViewController;
    private final ErrorHandler errorHandler;

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
        stage.show();
    }


    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {

    }
}
