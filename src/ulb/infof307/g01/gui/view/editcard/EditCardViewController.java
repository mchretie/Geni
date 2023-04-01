package ulb.infof307.g01.gui.view.editcard;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import ulb.infof307.g01.model.Card;

public class EditCardViewController {
    @FXML
    private HTMLEditor htmlEditor;
    @FXML
    private Button saveButton;


    private Card selectedCard;
    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }


    @FXML
    public void onSaveButtonClicked(MouseEvent mouseEvent) {
        listener.saveButtonClicked(selectedCard, htmlEditor.getHtmlText());
    }

    public void setCard(Card selectedCard) {
        this.selectedCard = selectedCard;
        htmlEditor.setHtmlText(selectedCard.getFront());
    }

    public interface Listener {
        void saveButtonClicked(Card card, String html);
    }
}
