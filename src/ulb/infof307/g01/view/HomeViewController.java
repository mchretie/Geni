package ulb.infof307.g01.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.components.HomeDeckComponentController;
import ulb.infof307.g01.database.DeckManager;
import ulb.infof307.g01.model.Deck;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomeViewController implements Initializable {
    @FXML
    Button createDeckButton;
    @FXML
    FontIcon searchIcon;
    @FXML
    FontIcon createDeckIcon;
    @FXML
    HomeDeckComponentController deck1Controller;
    @FXML
    HomeDeckComponentController deck2Controller;
    @FXML
    HomeDeckComponentController deck3Controller;

    DeckManager dm = DeckManager.singleton();

    @FXML
    public void handleSearchHover() {
        searchIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    public void handleSearchExit() {
        searchIcon.setIconColor(Color.web("#000000"));
    }

    public void handleCreateDeckHover(MouseEvent mouseEvent) {
        createDeckIcon.setIconColor(Color.web("#FFFFFF"));
    }

    public void handleCreateDeckExit(MouseEvent mouseEvent) {
        createDeckIcon.setIconColor(Color.web("#000000"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        List<Deck> allDecks = dm.getAllDecks();



        deck1Controller.setDeckName("Deck 1");
        deck2Controller.setDeckName("Deck 2");
        deck3Controller.setDeckName("Deck 3");
    }
}
