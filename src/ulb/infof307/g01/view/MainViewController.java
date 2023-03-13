package ulb.infof307.g01.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;

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
    public Button user;

    public FontIcon goBackIcon;
    public FontIcon homeIcon;
    public FontIcon cardsIcon;

    public Pane homeView;
    public Pane editDeckView;
    public BorderPane mainBorderPain;
    public BorderPane playDeckView;
    // public AnchorPane playDeckFront;

    public PlayDeckViewController playDeckViewController;

    private List<Pane> views;
    private MainViewListener listener;

    public void setListener(MainViewListener listener) {
        System.out.println("setListener");
        this.listener = listener;
    }

    private void hideViewsExcept(Pane view) {
        for (Pane v : views)
            v.setVisible(v == view);
    }

    public void setMainView() {
        topBar.setText("Main View");
        goBack.setVisible(false);
        homeIcon.setIconColor(Color.web("#FFFFFF"));
        hideViewsExcept(homeView);
    }

    public void setEditDeckView() {
        topBar.setText("Edit Deck");
        goBack.setVisible(true);
        homeIcon.setIconColor(Color.web("#000000"));
        hideViewsExcept(editDeckView);
    }

    public void setPlayDeckView(Deck deck) {
        topBar.setText("Jouer");
        goBack.setVisible(true);
        homeIcon.setIconColor(Color.web("#000000"));
        playDeckViewController.setDeck(deck);
        hideViewsExcept(playDeckView);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        views = new ArrayList<>(Arrays.asList(homeView, editDeckView, playDeckView));
//        setMainView();
        //setEditDeckView();
        Deck testDeck = new Deck("test");
        testDeck.addCard(new Card("front", "back"));
        testDeck.addCard(new Card("front2", "back2"));
        testDeck.addCard(new Card("front3", "back3"));
        setPlayDeckView(testDeck);
        mainBorderPain.setUserData(this);
    }

    public void loadEditDeckView(Deck deck) {
        setEditDeckView();
        EditDeckViewController controller = (EditDeckViewController) editDeckView.getUserData();
        controller.setDeck(deck);
    }

    public void handleGoBack(MouseEvent mouseEvent) {
        setMainView();
    }

    public void handleGoBackHover(MouseEvent mouseEvent) {
        goBackIcon.setIconColor(Color.web("#FFFFFF"));
    }

    public void handleGoBackExitHover(MouseEvent mouseEvent) {
        goBackIcon.setIconColor(Color.web("#000000"));
    }

    public interface MainViewListener {
        void onAddCardButton();
    }
}
