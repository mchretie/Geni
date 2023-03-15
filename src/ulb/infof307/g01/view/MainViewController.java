package ulb.infof307.g01.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.view.deckmenu.DeckMenuViewController;
import ulb.infof307.g01.view.editdeck.EditDeckViewController;
import ulb.infof307.g01.view.playdeck.PlayDeckViewController;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MainViewController {
    @FXML
    public Button goBack;
    public Label topBar;
    public Button deck;

    public FontIcon homeIcon;

    public Pane homeView;
    public Pane editDeckView;
    public BorderPane mainBorderPain;
    public BorderPane playDeckView;
    public PlayDeckViewController playDeckViewController;
    public EditDeckViewController editDeckViewController;
    public DeckMenuViewController homeViewController;

    private List<Pane> views;

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

    public void setEditDeckView(Deck deck) {
        topBar.setText("Modifier deck");
        goBack.setVisible(true);
        homeIcon.setIconColor(Color.web("#000000"));
        editDeckViewController.setDeck(deck);
        hideViewsExcept(editDeckView);
    }

    public void setPlayDeckView(Deck deck) {
        topBar.setText("Jouer");
        goBack.setVisible(true);
        homeIcon.setIconColor(Color.web("#000000"));
        playDeckViewController.setDeck(deck);
        hideViewsExcept(playDeckView);
    }
}
