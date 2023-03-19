package ulb.infof307.g01.view.deckmenu;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;

public class DeckMenuViewController {

    @FXML
    private GridPane gridPane;

    @FXML
    private TextField createDeckField;

    @FXML
    private FontIcon searchIcon;
    @FXML
    private FontIcon createDeckIcon;

    private Listener listener;


    /* ====================================================================== */
    /*                                Setters                                 */
    /* ====================================================================== */

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    /* ====================================================================== */
    /*                           Deck Displaying                              */
    /* ====================================================================== */

    /**
     * Checks if the node is to be removed from the grid or not
     *
     * @param node Candidate for removal from grid pane
     * @return true if to be removed or false otherwise
     */
    private boolean clearNodeFromGridCondition(Node node) {
        return GridPane.getRowIndex(node) != null
                && GridPane.getColumnIndex(node) != null
                && (GridPane.getRowIndex(node) > 0
                || GridPane.getColumnIndex(node) > 0);
    }

    /**
     * Clears the whole grid pane of decks. This circumvents a visual bug.
     */
    private void clearDecksFromGrid() {
        gridPane.getChildren().removeIf(this::clearNodeFromGridCondition);
    }

    /**
     * Initialises the grid of decks.
     *
     * @param decks loaded FXML deck files
     */
    public void setDecks(List<Node> decks) {

        clearDecksFromGrid();

        int col = 1;
        int row = 0;

        for (Node deck : decks) {
            gridPane.add(deck, col, row);

            col = nextCol(col);
            row = nextRow(row, col);
        }
    }

    private int nextCol(int currentCol) {
        return (currentCol + 1) % 3;
    }

    private int nextRow(int currentRow, int currentCol) {
        return currentCol == 0 ? currentRow + 1 : currentRow;
    }


    /* ====================================================================== */
    /*                             Click handlers                             */
    /* ====================================================================== */

    @FXML
    private void handleCreateDeckClicked() {
        listener.createDeckClicked(createDeckField.getText());
        createDeckField.clear();
    }


    /* ====================================================================== */
    /*                             Hover handlers                             */
    /* ====================================================================== */

    @FXML
    private void handleSearchHover() {
        searchIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    private void handleSearchExit() {
        searchIcon.setIconColor(Color.web("#000000"));
    }

    @FXML
    private void handleCreateDeckHover() {
        createDeckIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    private void handleCreateDeckExit() {
        createDeckIcon.setIconColor(Color.web("#000000"));
    }


    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface Listener {
        void createDeckClicked(String name);
    }
}
