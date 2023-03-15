package ulb.infof307.g01.view.deckmenu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.Deck;

import java.io.IOException;
import java.util.List;

public class DeckMenuViewController {

    @FXML
    public BorderPane homeViewPane;
    public FlowPane deckPane;
    public ScrollPane scrollPane;

    @FXML
    public TextField createDeckField;

    @FXML
    public Button createDeckButton;

    @FXML
    public FontIcon searchIcon;
    public FontIcon createDeckIcon;

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private void loadDeck(Deck deck) throws IOException {
        String deckViewPath = "/ulb/infof307/g01/view/deckmenu/DeckView.fxml";

        FXMLLoader loader = new FXMLLoader(getClass().getResource(deckViewPath));
        deckPane.getChildren().add(loader.load());

        DeckViewController controller = loader.getController();
        controller.setDeck(deck);
    }

    public void loadDecks(List<Deck> decks) {
        for (Deck deck : decks) {
            try {
                loadDeck(deck);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleCreateDeckClicked() {
        listener.createDeckClicked(createDeckField.getText());
    }


    /* ============================================================================================================== */
    /*                                                  Hover handlers                                                */
    /* ============================================================================================================== */

    public void handleSearchHover() {
        searchIcon.setIconColor(Color.web("#FFFFFF"));
    }

    public void handleSearchExit() {
        searchIcon.setIconColor(Color.web("#000000"));
    }

    public void handleCreateDeckHover() { createDeckIcon.setIconColor(Color.web("#FFFFFF")); }

    public void handleCreateDeckExit() {
        createDeckIcon.setIconColor(Color.web("#000000"));
    }

    /* ============================================================================================================== */
    /*                                                Listener interface                                              */
    /* ============================================================================================================== */

    public interface Listener {
        void createDeckClicked(String name);
    }

}
