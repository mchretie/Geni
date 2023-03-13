package ulb.infof307.g01.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    @FXML
    public Label topBar;

    @FXML
    public Button goBack;

    private MainViewListener listener;

    public void setListener(MainViewListener listener){
        System.out.println("setListener");
        this.listener = listener;
    }

    public void setMainView() {
        topBar.setText("Main Deck");
    }

    public void setEditDeckView() {
        topBar.setText("Edit Deck");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setMainView();
    }


    public interface MainViewListener{
        void onAddCardButton();
    }
}
