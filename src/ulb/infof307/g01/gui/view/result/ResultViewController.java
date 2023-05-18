package ulb.infof307.g01.gui.view.result;

import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import ulb.infof307.g01.model.deck.Score;

public class ResultViewController {
    /* ====================================================================== */
    /*                               FXML Attributes                          */
    /* ====================================================================== */
    @FXML
    private Label scoreLabel;

    @FXML
    public Label avgTimeLabel;

    @FXML
    public Label totalTimeLabel;

    @FXML
    private AreaChart<Number, Number> areaChart;

    @FXML
    private PieChart pieChart;


    /* ====================================================================== */
    /*                                Listener                                */
    /* ====================================================================== */

    private Listener listener;


    /* ====================================================================== */
    /*                                   Setters                              */
    /* ====================================================================== */

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setScore(int score) {
        scoreLabel.setText(String.valueOf(score));
    }

    public void setTotalTime(double totalTime) {
        totalTimeLabel.setText(totalTime + "s");
    }

    public void setAverageTime(double averageTime) {
        avgTimeLabel.setText(averageTime + "s");
    }

    public void setAreaChart(Score score) {
        listener.setAreaChart(this.areaChart, score);
    }

    public void setPieChart(Score score) {
        listener.setPieChart(this.pieChart, score);
    }

    /* ====================================================================== */
    /*                              Click handlers                            */
    /* ====================================================================== */

    @FXML
    private void onGoToMenuClicked() {
        listener.goToMenuButtonClicked();
    }


    /* ====================================================================== */
    /*                          Listener interface                            */
    /* ====================================================================== */

    public interface Listener {
        void goToMenuButtonClicked();
        void setAreaChart(AreaChart<Number, Number> areaChart, Score score);
        void setPieChart(PieChart pieChart, Score score);
    }
}
