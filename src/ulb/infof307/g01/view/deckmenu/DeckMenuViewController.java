package ulb.infof307.g01.view.deckmenu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.Deck;

import java.io.IOException;
import java.util.List;

public class DeckMenuViewController {

    @FXML
    public BorderPane homeViewPane;
    public ScrollPane scrollPane;

    @FXML
    public TextField createDeckField;

    @FXML
    public Button createDeckButton;

    @FXML
    public FontIcon searchIcon;
    public FontIcon createDeckIcon;

    @FXML
    public HBox topBar;
    public HBox bottomBar;
    public GridPane gridPane;

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    /* ============================================================================================================== */
    /*                                                  Deck displaying                                               */
    /* ============================================================================================================== */

    private int nextCol(int currentCol) {
        return (currentCol + 1) % 3;
    }

    private int nextRow(int currentRow, int currentCol) {
        return currentCol == 0 ? currentRow + 1 : currentRow;
    }

    public void setDecks(List<Node> decks) {
        int col = 1;
        int row = 0;

        for (Node deck : decks) {
            gridPane.add(deck, col, row);

            col = nextCol(col);
            row = nextRow(row, col);
        }
    }

    /* ============================================================================================================== */
    /*                                                  Click handlers                                                */
    /* ============================================================================================================== */

    @FXML
    public void handleCreateDeckClicked() {
        listener.createDeckClicked(createDeckField.getText());
        createDeckField.clear();
    }


    /* ============================================================================================================== */
    /*                                                  Hover handlers                                                */
    /* ============================================================================================================== */

    @FXML
    public void handleSearchHover() {
        searchIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    public void handleSearchExit() {
        searchIcon.setIconColor(Color.web("#000000"));
    }

    @FXML
    public void handleCreateDeckHover() { createDeckIcon.setIconColor(Color.web("#FFFFFF")); }

    @FXML
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
