package ulb.infof307.g01.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
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
    private FlowPane deckPane;
    @FXML
    private Button createDeckButton;
    @FXML
    private FontIcon searchIcon;
    @FXML
    private FontIcon createDeckIcon;

    private final DeckManager dm = DeckManager.singleton();

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

    private void loadDecks(){
        List<Deck> allDecks = dm.getAllDecks();
        for (int i = 0; i < 3; i++) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/infof307/g01/components/HomeDeckComponent.fxml"));
            try {
                deckPane.getChildren().add(loader.load());
                HomeDeckComponentController controller = loader.getController();
                controller.setDeck(allDecks.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.loadDecks();
    }
}
