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
    private final int amountCompetitiveCards;


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
                            Score score,
                            int amountCompetitiveCards) {
        this.stage = stage;
        this.mainWindowViewController = mainWindowViewController;
        this.controllerListener = controllerListener;
        this.resultViewController = mainWindowViewController.getResultViewController();

        this.score = score;
        this.amountCompetitiveCards = amountCompetitiveCards;
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

        int previousScore = 0;
        for (int i = 0; i < score.getAmountScores(); i++) {
            int currentScore = previousScore + score.getScoreAt(i);
            scores.getData().add(new XYChart.Data<>(i + 1, currentScore));
            previousScore = currentScore;
        }

        areaChart.getData().add(scores);
    }

    @Override
    public void setPieChart(PieChart pieChart, Score score) {
        // clear chart
        pieChart.getData().clear();

        int amountCorrect = score.getAmountCorrectAnswers();
        int amountWrong = this.amountCompetitiveCards - amountCorrect;

        PieChart.Data correctData = new PieChart.Data("Bon", amountCorrect);
        PieChart.Data wrongData = new PieChart.Data("Mauvais", amountWrong);

        pieChart.getData().add(correctData);
        pieChart.getData().add(wrongData);

        // set the color of the pie chart
        correctData.getNode().setStyle("-fx-pie-color: green");
        wrongData.getNode().setStyle("-fx-pie-color: red");

        // set colors of the legend of the pie chart
        // https://stackoverflow.com/questions/61836519/javafx-piechart-legend-color-change

    }


    /* ====================================================================== */
    /*                   Controller Listener Interface                        */
    /* ====================================================================== */

    public interface ControllerListener {
        void goBackToMenu();
    }
}
