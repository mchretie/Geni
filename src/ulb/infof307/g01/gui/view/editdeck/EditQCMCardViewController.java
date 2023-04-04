package ulb.infof307.g01.gui.view.editdeck;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.Card;

public class EditQCMCardViewController {
    @FXML
    private StackPane frontCard;
    @FXML
    private TextField frontCardText;

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

    private Listener listener;

    /* ====================================================================== */
    /*                                Setters                                 */
    /* ====================================================================== */

    public void setListener(EditQCMCardViewController.Listener listener) {
        this.listener = listener;
    }

    /* ====================================================================== */
    /*                             Click handlers                             */
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
        FontIcon checkIcon = new FontIcon("mdi2c-check-circle-outline");
        checkIcon.setIconSize(19);

        Button check = new Button();
        check.setGraphic(checkIcon);
        check.setStyle("-fx-background-color: transparent;");
        check.setAlignment(Pos.CENTER_RIGHT);

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

        setCheckButton(check);
        return borderPane;
    }

    private void setCheckButton(Button button){
        FontIcon fontIcon = (FontIcon) button.getGraphic();
        button.setOnMouseClicked(event ->{
            handleCorrectChoice(fontIcon);
        });

        button.getGraphic().setOnMouseEntered(event -> {
            fontIcon.setIconColor(Color.web("#7f8281"));
        });

        button.getGraphic().setOnMouseExited(event ->{
            fontIcon.setIconColor(Color.web("#000000"));
        });
    }

    private void handleCorrectChoice(FontIcon fontIcon){
        return;
        //TODO set the correctAnswer to the card
    }

    @FXML
    private void handleAddChoiceHover(){ addChoiceIcon.setIconColor(Color.web("#7f8281"));  }

    @FXML
    private void handleAddChoiceHoverExit() {addChoiceIcon.setIconColor(Color.web("#000000"));}

    /* ====================================================================== */
    /*                           Listener Interface                           */
    /* ====================================================================== */

    public interface Listener {
        void frontOfCardModified(Card card, String newFront);
    }
}
