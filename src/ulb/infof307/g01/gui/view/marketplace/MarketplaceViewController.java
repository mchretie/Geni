package ulb.infof307.g01.gui.view.marketplace;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;

public class MarketplaceViewController {
    public enum SearchType {
        Name,
        Creator,
        Tag
    }

    public enum SortType {
        Name,
        Rating,
        Discover
    }

    /* ====================================================================== */
    /*                              FXML Attributes                           */
    /* ====================================================================== */

    @FXML
    private TextField searchBar;

    @FXML
    private FontIcon searchIcon;


    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private ComboBox<SortType> sortChoiceBox;

    @FXML
    private FlowPane userDecksContainer;
    @FXML
    private FlowPane decksContainer;


    /* ====================================================================== */
    /*                                Listener                                */
    /* ====================================================================== */

    private Listener listener;


    /* ====================================================================== */
    /*                                 Setters                                */
    /* ====================================================================== */

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void initialize() {
        initComboBox();
        initSortChoiceBox();
    }

    private void initComboBox() {
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Nom",
                        "Creator",
                        "Tag"
                );
        comboBox.setItems(options);
        comboBox.setValue("Nom");

    }

    private void initSortChoiceBox() {
        this.sortChoiceBox.getItems().addAll(SortType.values());
        this.sortChoiceBox.setValue(SortType.Name);
    }


    /* ====================================================================== */
    /*                                Getters                                 */
    /* ====================================================================== */
    public SearchType getSearchType() {
        SearchType searchType = null;
        String searchTypeText = comboBox.getValue();

        if (searchTypeText.equals("Nom")) {
            searchType = MarketplaceViewController.SearchType.Name;
        } else if (searchTypeText.equals("Username")) {
            searchType = MarketplaceViewController.SearchType.Creator;
        } else if (searchTypeText.equals("Tag")) {
            searchType = MarketplaceViewController.SearchType.Tag;
        }

        return searchType;
    }

    public SortType getSortType() {
        return sortChoiceBox.getValue();
    }


    /* ====================================================================== */
    /*                           Deck Displaying                              */
    /* ====================================================================== */

    public void setDecksMarketplace(List<Node> decksMarketplace) {
        decksContainer.getChildren().clear();

        for (Node deck : decksMarketplace) {
            decksContainer.getChildren().add(deck);
        }
    }

    public void setDecksUser(List<Node> decksUser) {
        userDecksContainer.getChildren().clear();

        for (Node deck : decksUser) {
            userDecksContainer.getChildren().add(deck);
        }
    }

    /* ====================================================================== */
    /*                             Click handlers                             */
    /* ====================================================================== */

    @FXML
    private void handleSearchDeckEvent(Event event) {
        if (event instanceof MouseEvent) {
            searchBar.requestFocus();
        }

        String searchText = searchBar.getText();
        listener.searchDeckClicked(searchText);
    }

    @FXML
    private void handleSortChoiceBoxChanged() {
        listener.sortChoiceBoxChanged(this.getSortType());
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


    /* ====================================================================== */
    /*                          Listener interface                            */
    /* ====================================================================== */

    public interface Listener {
        void searchDeckClicked(String name);
        void sortChoiceBoxChanged(SortType sortType);
    }
}
