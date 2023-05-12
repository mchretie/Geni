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
        this.areaChart.getData().clear();

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>(1, 23));
        series.getData().add(new XYChart.Data<>(2, 14));
        series.getData().add(new XYChart.Data<>(3, 15));
        series.getData().add(new XYChart.Data<>(4, 24));
        series.getData().add(new XYChart.Data<>(5, 34));

        this.areaChart.getData().add(series);
    }

    public void setPieChart() {
        // TODO
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
    }
}
