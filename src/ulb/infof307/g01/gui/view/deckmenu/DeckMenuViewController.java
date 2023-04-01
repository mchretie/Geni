package ulb.infof307.g01.gui.view.deckmenu;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;

public class DeckMenuViewController {

    @FXML
    private GridPane gridPane;

    @FXML
    private TextField createDeckField;

    @FXML
    private TextField searchBar;

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
        resetGrid();
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
            if (row == gridPane.getRowCount()) {
                addRow();
            }

            GridPane.setMargin(deck, new Insets(20));
            gridPane.add(deck, col, row);

            col = nextCol(col);
            row = nextRow(row, col);
        }
    }

    /**
     * removes all rows from the gridPane except the first one
     */
    private void resetGrid() {
        for (int i=1; i<gridPane.getRowCount(); i++) {
            gridPane.getRowConstraints().remove(i);
        }
    }

    /**
     * properly add row to the gridPane
     */
    private void addRow() {
        RowConstraints rc = new RowConstraints();
        rc.setMinHeight(220);
        rc.setMaxHeight(300);
        gridPane.getRowConstraints().add(rc);
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

    @FXML
    private void handleSearchDeckEvent(Event event) {
        if (event instanceof MouseEvent) {
            searchBar.requestFocus();
        }
        listener.searchDeckClicked(searchBar.getText());
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
        void searchDeckClicked(String name);
    }
}
