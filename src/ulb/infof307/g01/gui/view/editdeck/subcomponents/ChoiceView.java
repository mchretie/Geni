package ulb.infof307.g01.gui.view.editdeck.subcomponents;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.MCQCard;


public class ChoiceView extends HBox {
    private final Listener listener;
    private final MCQCard selectedCard;
    private TextField textField;
    private Button selectCorrectChoiceButton;
    private Button removeChoiceButton;

    private int index;
    private boolean isCorrect;


    public ChoiceView(String text,
                      int index,
                      boolean isCorrect,
                      MCQCard selectedCard,
                      Listener listener) {


        this.selectedCard = selectedCard;
        this.listener = listener;
        this.index = index;
        this.isCorrect = isCorrect;

        this.textField = createTextField(text);
        this.selectCorrectChoiceButton = createCorrectChoiceSelector();
        this.removeChoiceButton = createRemoveChoiceButton();

        HBox.setHgrow(this, Priority.ALWAYS);
        setAlignment(Pos.CENTER);
        getChildren().addAll(textField, selectCorrectChoiceButton, removeChoiceButton);
    }

    private TextField createTextField(String text) {
        TextField textField = new TextField(text);
        textField.setPromptText("Réponse");
        textField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(textField, Priority.ALWAYS);

        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue || index >= selectedCard.getNbOfChoices())
                return;

            listener.choiceModified(selectedCard, textField.getText(), index);
        });

        textField.setOnKeyPressed(event -> {
            boolean isTextFieldEmpty = textField.getText().isEmpty();

            switch (event.getCode()) {
                case ENTER, TAB -> {
                    if (isTextFieldEmpty)
                        listener.choiceRemoved(selectedCard, index);

                    listener.focusNextNode(index, event.getCode());
                }
            }
        });

        return textField;
    }

    private Button createCorrectChoiceSelector() {
        Button selectCorrectChoiceButton = new Button();
        FontIcon checkIcon = new FontIcon("mdi2c-check");

        if (isCorrect) {
            checkIcon.setIconColor(Color.WHITE);
            selectCorrectChoiceButton.setStyle("-fx-background-color: green;");
        }

        selectCorrectChoiceButton.setGraphic(checkIcon);

        selectCorrectChoiceButton
                .setOnAction(event
                        -> listener.correctChoiceChanged(selectedCard, index));

        return selectCorrectChoiceButton;
    }

    private Button createRemoveChoiceButton() {
        removeChoiceButton = new Button();
        removeChoiceButton.setStyle("-fx-background-color: red;");

        FontIcon trashIcon = new FontIcon("mdi2t-trash-can-outline");
        trashIcon.setIconColor(Color.WHITE);
        removeChoiceButton.setGraphic(trashIcon);

        if (selectedCard.isCardMin())
            removeChoiceButton.setDisable(true);

        removeChoiceButton
                .setOnAction(event
                        -> listener.choiceRemoved(selectedCard, index));

        return removeChoiceButton;
    }

    public void requestFocus() {
        textField.requestFocus();
        textField.selectAll();
    }

    public interface Listener {
        void choiceModified(MCQCard selectedCard, String text, int index);
        void correctChoiceChanged(MCQCard selectedCard, int index);
        void choiceRemoved(MCQCard selectedCard, int index);
        void focusNextNode(int index, KeyCode keyCode);
    }
}
