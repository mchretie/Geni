package ulb.infof307.g01.gui.view.deckmenu;

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
import ulb.infof307.g01.gui.util.GridPosIterator;
import ulb.infof307.g01.gui.util.Pos2D;

import java.io.File;
import java.nio.file.Path;
import java.util.Iterator;
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

    private FileChooser fileChooser;

    private int lastColCount = 2;  // default

    /* ====================================================================== */
    /*                                Listener                                */
    /* ====================================================================== */

    private Listener listener;

    public void initialize() {
        initWidthListener();
        initComboBox();
        initFileChooser();
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
        final ChangeListener<Number> listener =
                (observable, oldValue, newValue) -> widthChangeHandler();

        gridPane.widthProperty().addListener(listener);
    }

    // TODO: find way to take extension from DeckIO
    private void initFileChooser() {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Fichiers deck",
                        "*.deck")
        );
    }

    /* ====================================================================== */
    /*                                Utils                                   */
    /* ====================================================================== */

    public Path pickOpenFile() {
        File file = fileChooser
                .showOpenDialog(borderPane.getScene().getWindow());
        return file == null ? null : file.toPath();
    }

    public Path pickSaveFile() {
        File file = fileChooser
                .showSaveDialog(borderPane.getScene().getWindow());
        return file == null ? null : file.toPath();
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

    private void initGrid(int rows, int columns) {
        while (gridPane.getRowCount() < rows) {
            addRow();
        }
        while (gridPane.getColumnCount() < columns) {
            addColumn();
        }
    }

    private int expectedRowCountFor(int cellsCount, int columnCount) {
        return cellsCount / columnCount + cellsCount % columnCount;
    }

    private int getColCount() {
        int deckWidth = 400;
        return (int) (gridPane.getWidth() / deckWidth);
    }

    /**
     * Redistribute the grid’s children in the grid
     * <p>
     *     From top left through bottom right.
     * </p>
     */
    private void arrange() {
        List<Node> nodes = gridPane.getChildren();
        int colCount = getColCount();
        int rowCount = expectedRowCountFor(nodes.size(), colCount);

        resetGrid();
        initGrid(rowCount, colCount);

        Iterator<Pos2D> positions = new GridPosIterator(colCount, rowCount);

        for (Node node : nodes) {
            var pos = positions.next();
            GridPane.setColumnIndex(node, pos.col);
            GridPane.setRowIndex(node, pos.row);
        }
    }

    /* ====================================================================== */
    /*                             Resize handlers                            */
    /* ====================================================================== */

    private void widthChangeHandler() {
        int newColCount = getColCount();

        if (newColCount != lastColCount) {
            lastColCount = newColCount;
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
        listener.deckImportClicked();
    }

    /* ====================================================================== */
    /*                             Hover handlers                             */
    /* ====================================================================== */

    @FXML
    private void handleSearchHover() { searchIcon.setIconColor(Color.web("#FFFFFF")); }

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

        void deckImportClicked();
    }
}