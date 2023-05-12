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
        resultViewController.setAreaChart(score);
        resultViewController.setPieChart(score);

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
    public void setAreaChart(AreaChart<Number, Number> areaChart, Score score) {
        areaChart.getData().clear();

        XYChart.Series<Number, Number> scores = new XYChart.Series<>();

        // begin with a score of 0
        scores.getData().add(new XYChart.Data<>(0, 0));

        for (int i = 0; i < score.getAmountScores(); i++) {
            scores.getData().add(new XYChart.Data<>(i + 1, score.getScoreAt(i)));
        }

        areaChart.getData().add(scores);
    }

    @Override
    public void setPieChart(PieChart pieChart, Score score) {
        // add data to pie chart
        PieChart.Data slice1 = new PieChart.Data("Desktop", 213);
        PieChart.Data slice2 = new PieChart.Data("Phone"  , 67);

        pieChart.getData().add(slice1);
        pieChart.getData().add(slice2);
    }


    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void goBackToMenu();
    }
}
