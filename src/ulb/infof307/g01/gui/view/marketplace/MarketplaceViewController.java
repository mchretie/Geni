package ulb.infof307.g01.gui.view.marketplace;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.gui.view.deckmenu.DeckMenuViewController;
import ulb.infof307.g01.gui.view.playdeck.PlayDeckViewController;

public class MarketplaceViewController {
    public enum SearchType {
        Name,
        Username
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
    }

    private void initComboBox() {
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Nom",
                        "Username"
                );
        comboBox.setItems(options);
        comboBox.setValue("Nom");
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
            searchType =  MarketplaceViewController.SearchType.Username;
        }

        return searchType;
    }

    /* ====================================================================== */
    /*                             Click handlers                             */
    /* ====================================================================== */

    @FXML
    private void handleSearchDeckEvent(Event event) {
        //TODO
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


    /* ====================================================================== */
    /*                          Listener interface                            */
    /* ====================================================================== */

    public interface Listener {
        void searchDeckClicked(String name);
    }
}
