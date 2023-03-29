package ulb.infof307.g01.gui.view.editcard;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;

public class EditCardViewController {
    @FXML
    public HTMLEditor htmlEditor;
    public Button saveButton;
    public WebView webView;

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }


    @FXML
    public void onSaveButtonClicked(MouseEvent mouseEvent) {
        listener.saveButtonClicked(htmlEditor.getHtmlText());
        webView.getEngine().loadContent(htmlEditor.getHtmlText());
    }

    public interface Listener {
        void saveButtonClicked(String html);
    }
}
