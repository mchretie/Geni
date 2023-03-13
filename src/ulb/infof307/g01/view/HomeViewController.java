package ulb.infof307.g01.view;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

public class HomeViewController {
    @FXML
    public FontIcon searchIcon;
    public FontIcon createDeckIcon;

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
}
