package ulb.infof307.g01.gui.view.result;

import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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

    public void setAreaChart() {
        listener.setAreaChart(this.areaChart);
    }

    public void setPieChart() {
        listener.setPieChart(this.pieChart);
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
        void setAreaChart(AreaChart<Number, Number> areaChart);
        void setPieChart(PieChart pieChart);
    }
}
