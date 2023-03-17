package ulb.infof307.g01.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.components.HomeDeckComponentController;
import ulb.infof307.g01.database.DeckDAO;
import ulb.infof307.g01.model.Deck;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomeViewController implements Initializable {
    @FXML
    public BorderPane homeViewPane;
    @FXML
    public TextField createDeckField;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    private FlowPane deckPane;
    @FXML
    private Button createDeckButton;
    @FXML
    private FontIcon searchIcon;
    @FXML
    private FontIcon createDeckIcon;

    private final DeckDAO dm = DeckDAO.singleton();

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

    public void handleCreateDeck(MouseEvent mouseEvent) {
        createDeck();
    }

    public void loadDecks() {
        deckPane.getChildren().remove(1, deckPane.getChildren().size());
        List<Deck> allDecks = dm.getAllDecks();
        for (Deck allDeck : allDecks) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/infof307/g01/components/HomeDeckComponent.fxml"));
            try {
                deckPane.getChildren().add(loader.load());
                HomeDeckComponentController controller = loader.getController();
                controller.setDeck(allDeck);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        scrollPane.setFitToWidth(true);
        deckPane.setUserData(this);
        this.loadDecks();
    }

    public void createDeck() {
        String deckName = createDeckField.getText();
        if (deckName.length() > 0) {
            dm.saveDeck(new Deck(deckName));
            createDeckField.setText("");
            loadDecks();
        }
    }

    public void handleCreateDeckEnter(ActionEvent actionEvent) {
        handleCreateDeck(null);
    }
}
