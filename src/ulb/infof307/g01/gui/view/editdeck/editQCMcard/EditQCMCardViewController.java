package ulb.infof307.g01.gui.view.editdeck.editQCMcard;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import org.kordamp.ikonli.javafx.FontIcon;

import ulb.infof307.g01.model.Card;

public class EditQCMCardViewController {
    @FXML
    private WebView frontCardWebView;
    @FXML
    public FontIcon frontCardEditIcon;

    @FXML
    private GridPane choicesGrid;

    @FXML
    private BorderPane addChoiceField;

    @FXML
    private FontIcon addChoiceIcon;

    private int currentCol = 0;

    private int currentRow = 0;

    private Card card;

    private Listener listener;

    /* ====================================================================== */
    /*                                Setters                                 */
    /* ====================================================================== */

    public void setListener(EditQCMCardViewController.Listener listener) {
        this.listener = listener;
    }

    public void setCard(Card card) {
        this.card = card;
        //TODO this.correctAnswer = ...
    }

    /* ====================================================================== */
    /*                            card Click handlers                         */
    /* ====================================================================== */
    @FXML
    private void handleFrontEdit(KeyEvent keyEvent) {
        if (!keyEvent.getCode().equals(KeyCode.ENTER))
            return;
        String newFront
                = frontCardWebView.getEngine()
                .executeScript("document.body.innerHTML")
                .toString();

        listener.frontOfCardModified(card, newFront);
    }

    @FXML
    private void handleFrontEditClicked() {
        listener.editCardClicked(card);
    }

    /* ====================================================================== */
    /*                            grid Click handlers                         */
    /* ====================================================================== */

    @FXML
    private void handleAddNewChoice() {
        int nextCol = currentCol ^ 1;
        int nextRow = currentRow;
        if (nextCol==0) nextRow +=1;

        choicesGrid.getChildren().remove(addChoiceField);
        choicesGrid.setConstraints(addChoiceField, nextCol, nextRow);
        choicesGrid.getChildren().add(addChoiceField);

        currentCol = nextCol; currentRow = nextRow;

        //TODO listener.addNewChoiceToCard(Card card);
    }


    /* ====================================================================== */
    /*                             Hover handlers                             */
    /* ====================================================================== */

    @FXML
    private void handleAddChoiceHoverExit() {addChoiceIcon.setIconColor(Color.web("#000000"));}

    @FXML
    private void handleFrontCardEditHover() {
        frontCardEditIcon.setIconColor(Color.web("#FFFFFF"));
    }

    @FXML
    private void handleFrontCardEditHoverExit() {
        frontCardEditIcon.setIconColor(Color.web("#000000"));
    }

    @FXML
    private void handleAddChoiceHover(){ addChoiceIcon.setIconColor(Color.web("#7f8281"));  }


    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface Listener {
        void frontOfCardModified(Card card, String newFront);
        void editCardClicked(Card card);
    }
}
