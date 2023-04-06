package ulb.infof307.g01.gui.view.editdeck;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
    private BorderPane editChoiceField;

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

        BorderPane editChoiceField = createEditChoiceField();
        choicesGrid.setConstraints(editChoiceField, currentCol ,currentRow);
        choicesGrid.getChildren().add(editChoiceField);

        currentCol = nextCol; currentRow = nextRow;

        //TODO listener.addNewChoiceToCard(Card card);
    }

    private BorderPane createEditChoiceField(){
        Button check = setCheckButton();

        TextField textField = new TextField();
        textField.setStyle("-fx-background-color: #e4e4e4;");
        textField.setPrefWidth(160);

        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(textField);
        borderPane.setRight(check);

        borderPane.setAlignment(textField, Pos.CENTER);

        borderPane.setStyle("-fx-background-color: #f0f0f0;" +
                            "-fx-border-color: #b2b2b2;" +
                            "-fx-padding: 1px 2px;" +
                            "-fx-border-insets: 0px;" +
                            "-fx-background-insets: 1px 3px;");
        return borderPane;
    }


    private Button setCheckButton(){
        FontIcon checkIcon = new FontIcon("mdi2c-check-circle-outline");
        checkIcon.setIconSize(19);

        Button checkButton = new Button();
        checkButton.setGraphic(checkIcon);
        checkButton.setStyle("-fx-background-color: transparent;");
        checkButton.setAlignment(Pos.CENTER_RIGHT);

        checkButton.setOnMouseClicked(event ->{
            handleCorrectAnswer();
        });

        checkButton.getGraphic().setOnMouseEntered(event -> {
            checkIcon.setIconColor(Color.web("#7f8281"));
        });

        checkButton.getGraphic().setOnMouseExited(event ->{
            checkIcon.setIconColor(Color.web("#000000"));
        });

        return checkButton;
    }

    private void handleCorrectAnswer(){
        return;
        //TODO set the correctAnswer to the card
    }

    /* ====================================================================== */
    /*                             Hover handlers                             */
    /* ====================================================================== */

    @FXML
    private void handleAddChoiceHover(){ addChoiceIcon.setIconColor(Color.web("#7f8281"));  }

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

    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface Listener {
        void frontOfCardModified(Card card, String newFront);
        void editCardClicked(Card card);
    }
}
