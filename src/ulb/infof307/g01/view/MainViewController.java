package ulb.infof307.g01.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;

import java.io.IOException;
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
    public AnchorPane mainAnchorPane;
    public BorderPane playDeckBack;
    public PlayDeckBackViewController playDeckBackController;
    // public AnchorPane playDeckFront;

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

    public void setPlayDeckBackView(Card card) {
        topBar.setText("Testing");
        goBack.setVisible(true);

        playDeckBackController.setCard(card);

        hideViewsExcept(playDeckBack);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        views = new ArrayList<>(Arrays.asList(homeView, editDeckView, playDeckBack));
        setMainView();
//        setEditDeckView();
        setPlayDeckBackView(new Card("Hi", "Hello"));

        mainAnchorPane.setUserData(this);
    }

    public void loadEditDeckView(Deck deck) {
        setEditDeckView();
        EditDeckViewController controller = (EditDeckViewController) editDeckView.getUserData();
        controller.setDeck(deck);
    }


    public interface MainViewListener {
        void onAddCardButton();
    }
}
