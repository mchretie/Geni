package ulb.infof307.g01.gui.view.userauth;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

public class LoginRegisterViewController {

    @FXML
    private HBox mainHbox;

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
    }
}
