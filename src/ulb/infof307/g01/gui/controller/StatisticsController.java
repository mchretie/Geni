package ulb.infof307.g01.gui.controller;

import javafx.stage.Stage;
import ulb.infof307.g01.gui.controller.errorhandler.ErrorHandler;
import ulb.infof307.g01.gui.httpdao.dao.UserSessionDAO;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.gui.view.statistics.StatisticsViewController;

public class StatisticsController implements StatisticsViewController.Listener {
    private final Stage stage;
    private final ErrorHandler errorHandler;
    private final MainWindowViewController mainWindowViewController;
    private final GlobalLeaderboardController.ControllerListener controllerListener;
    private final UserSessionDAO userSessionDAO;

    private final StatisticsViewController statisticsViewController;

    /* ====================================================================== */
    /*                              Constructor                               */
    /* ====================================================================== */

    public StatisticsController(Stage stage,
                                ErrorHandler errorHandler,
                                MainWindowViewController mainWindowViewController,
                                GlobalLeaderboardController.ControllerListener controllerListener,
                                UserSessionDAO userSessionDAO) {
        this.stage = stage;
        this.errorHandler = errorHandler;
        this.mainWindowViewController = mainWindowViewController;
        this.controllerListener = controllerListener;
        this.userSessionDAO = userSessionDAO;

        this.statisticsViewController = mainWindowViewController.getStatisticsViewController();
        statisticsViewController.setListener(this);
    }


    /* ====================================================================== */
    /*                         Stage Manipulation                             */
    /* ====================================================================== */

    public void show() {
        mainWindowViewController.setStatisticsViewVisible();
        //TODO: implement
        stage.show();
    }


    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void StatisticsClicked();
    }

}
