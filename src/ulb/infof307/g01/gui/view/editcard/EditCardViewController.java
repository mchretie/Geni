package ulb.infof307.g01.gui.view.editcard;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.HTMLEditor;

public class EditCardViewController {
    @FXML
    public HTMLEditor htmlEditor;
    public Button saveButton;

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }


    @FXML
    public void onSaveButtonClicked(MouseEvent mouseEvent) {
        listener.saveButtonClicked(htmlEditor.getHtmlText());
    }

    public interface Listener {
        void saveButtonClicked(String html);
    }
}
