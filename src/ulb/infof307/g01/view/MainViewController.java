package ulb.infof307.g01.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    @FXML
    public Button goBack;
    public Label topBar;
    public Button home;
    public Button deck;
    public Button about;
    public Pane homeView;
    public Pane editDeckView;

    private List<Pane> views;

    private MainViewListener listener;

    public void setListener(MainViewListener listener) {
        System.out.println("setListener");
        this.listener = listener;
    }

    private void hideViewsExcept(Pane view) {
        for (Pane v : views) {
            v.setVisible(v == view);
        }
    }

    public void setMainView() {
        topBar.setText("Main View");
        goBack.setVisible(false);
        hideViewsExcept(homeView);
    }

    public void setEditDeckView() {
        topBar.setText("Edit Deck");
        goBack.setVisible(true);
        hideViewsExcept(editDeckView);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        views = new ArrayList<>(Arrays.asList(homeView, editDeckView));
        setMainView();
//        setEditDeckView();
    }



    public interface MainViewListener {
        void onAddCardButton();
    }
}
