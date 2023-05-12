package ulb.infof307.g01.gui.controller;

import javafx.scene.chart.AreaChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.gui.view.result.ResultViewController;
import ulb.infof307.g01.model.deck.Score;


public class ResultController implements ResultViewController.Listener {

    /* ====================================================================== */
    /*                             Model Attributes                           */
    /* ====================================================================== */

    private final Score score;


    /* ====================================================================== */
    /*                         Controller Listener                            */
    /* ====================================================================== */

    private final ControllerListener controllerListener;


    /* ====================================================================== */
    /*                        View Controller References                      */
    /* ====================================================================== */

    private final MainWindowViewController mainWindowViewController;
    private final ResultViewController resultViewController;


    /* ====================================================================== */
    /*                             Stage Attributes                           */
    /* ====================================================================== */

    private final Stage stage;


    /* ====================================================================== */
    /*                              Constructor                               */
    /* ====================================================================== */

    public ResultController(Stage stage,
                            MainWindowViewController mainWindowViewController,
                            ControllerListener controllerListener,
                            Score score) {
        this.stage = stage;
        this.mainWindowViewController = mainWindowViewController;
        this.controllerListener = controllerListener;
        this.resultViewController = mainWindowViewController.getResultViewController();

        this.score = score;
        resultViewController.setListener(this);
    }


    /* ====================================================================== */
    /*                         Stage Manipulation                             */
    /* ====================================================================== */

    public void show() {
        mainWindowViewController.setResultViewVisible();

        resultViewController.setScore(score.getScore());
        resultViewController.setTotalTime(score.getTotalTime());
        resultViewController.setAverageTime(score.getAvgTime());
        resultViewController.setAreaChart();
        resultViewController.setPieChart();

        stage.show();
    }


    /* ====================================================================== */
    /*                        View Listener Method                            */
    /* ====================================================================== */

    @Override
    public void goToMenuButtonClicked() {
        controllerListener.goBackToMenu();
    }

    @Override
    public void setAreaChart(AreaChart<Number, Number> areaChart) {
        areaChart.getData().clear();

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>(1, 23));
        series.getData().add(new XYChart.Data<>(2, 14));
        series.getData().add(new XYChart.Data<>(3, 15));
        series.getData().add(new XYChart.Data<>(4, 24));
        series.getData().add(new XYChart.Data<>(5, 34));

        areaChart.getData().add(series);
    }
    
    @Override
    public void setPieChart(PieChart pieChart) {
        // TODO: Implement this method
    }


    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void goBackToMenu();
    }
}
