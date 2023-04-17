package ulb.infof307.g01.gui.view.deckmenu;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.beans.value.ChangeListener;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.util.List;

public class DeckMenuViewController {

    public enum SearchType {
        Name,
        Tag
    }

    /* ====================================================================== */
    /*                              FXML Attributes                           */
    /* ====================================================================== */

    @FXML
    private BorderPane borderPane;

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

    @FXML
    private FontIcon importDeck;

    @FXML
    private ComboBox<String> comboBox;

    private int colCount = 2;  // default

    /* ====================================================================== */
    /*                                Listener                                */
    /* ====================================================================== */

    private Listener listener;

    public void initialize() {
        initWidthListener();
        initComboBox();
    }

    private void initComboBox() {
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Nom",
                        "Tag"
                );
        comboBox.setItems(options);
        comboBox.setValue("Nom");
    }

    private void initWidthListener() {
        final ChangeListener<Number> listener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                widthChangeHandler();
            }
        };

        gridPane.widthProperty().addListener(listener);
    }

    /* ====================================================================== */
    /*                                Setters                                 */
    /* ====================================================================== */

    public void setListener(Listener listener) {
        this.listener = listener;
    }
    
    /* ====================================================================== */
    /*                                Getters                                 */
    /* ====================================================================== */
    
    public SearchType getSearchType() {
        SearchType searchType = null;
        String searchTypeText = comboBox.getValue();

        if (searchTypeText.equals("Nom")) {
            searchType = SearchType.Name;
        } else if (searchTypeText.equals("Tag")) {
            searchType =  SearchType.Tag;
        }

        return searchType;
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
     * Initialize the grid of decks
     * <p>
     * Nodes are added to the grid at pos (0,0) and then redistributed.
     *
     * @param decks loaded FXML deck files
     */
    public void setDecks(List<Node> decks) {
        clearDecksFromGrid();

        for (Node deck : decks) {
            GridPane.setMargin(deck, new Insets(20));
            gridPane.add(deck, 0, 0);
        }

        arrange();
    }

    /**
     * Remove all row and column constraints from the grid
     */
    private void resetGrid() {
        int rowCount = gridPane.getRowConstraints().size();
        gridPane.getRowConstraints().remove(0, rowCount);

        int columnCount = gridPane.getColumnConstraints().size();
        gridPane.getColumnConstraints().remove(0, columnCount);
    }

    /**
     * Properly add column to gridPane
     */
    private void addColumn() {
        ColumnConstraints cc = new ColumnConstraints();
        cc.setMinWidth(300);  // TODO: change these values
        cc.setMaxWidth(400);

        cc.setHalignment(HPos.CENTER);
        cc.setHgrow(Priority.ALWAYS);
        cc.setFillWidth(true);

        gridPane.getColumnConstraints().add(cc);
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
        return (currentCol + 1) % colCount;
    }

    private int nextRow(int currentRow, int currentCol) {
        return currentCol == 0 ? currentRow + 1 : currentRow;
    }

    private void initGrid(int rows, int columns) {
        while (gridPane.getRowCount() < rows) {
            addRow();
        }
        while (gridPane.getColumnCount() < columns) {
            addColumn();
        }
    }

    /**
     * Initialize the grid for a number of cells given the column count
     */
    private void initGridFor(int cellsCount, int columnCount) {
        int expectedRows = cellsCount / columnCount + cellsCount % columnCount;
        initGrid(expectedRows, columnCount);
    }

    /**
     * Redistribute the grid’s children in the grid
     * <p>
     *     From top left through bottom right.
     * </p>
     */
    private void arrange() {
        List<Node> nodes = gridPane.getChildren();

        resetGrid();
        initGridFor(nodes.size(), colCount);

        int row = 0;
        int col = 0;

        for (Node node : nodes) {
            GridPane.setColumnIndex(node, col);
            GridPane.setRowIndex(node, row);
            col = nextCol(col);
            row = nextRow(row, col);
        }
    }

    /* ====================================================================== */
    /*                             Resize handlers                            */
    /* ====================================================================== */

    private void widthChangeHandler() {
        double newWidth = gridPane.getWidth();
        int newColumnCount = (int) (newWidth / 400);  // TODO: change this magic

        if (newColumnCount != colCount) {
            colCount = newColumnCount;
            arrange();
        }
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

        String searchText = searchBar.getText();
        listener.searchDeckClicked(searchText);
    }

    @FXML
    private void handleImportDeck() {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(borderPane.getScene().getWindow());

        listener.deckImported(file);
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

    @FXML
    private void handleImportDeckHover() {
        importDeck.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    private void handleImportDeckExit() {
        importDeck.setIconColor(Color.web("#000000"));
    }

    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface Listener {
        void createDeckClicked(String name);

        void searchDeckClicked(String name);

        void deckImported(File file);
    }
}
