package ulb.infof307.g01.gui.view.editdeck.subcomponents;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import org.kordamp.ikonli.javafx.FontIcon;
import ulb.infof307.g01.model.Tag;

public class TagView extends BorderPane {
    private final Listener listener;
    private final TextField tagNameField;
    private final Tag tag;

    public TagView(Tag tag,
                   Listener listener) {
        this.tag = tag;
        this.listener = listener;

        this.setStyle("""
            -fx-border-color: #000000;
            -fx-border-insets: 3px 5px;
            -fx-border-radius: 15px;
            -fx-background-radius: 15px;
            -fx-background-insets: 3px 5px;
            """);

        this.tagNameField = createTextField(tag.getName());
        this.setCenter(tagNameField);
        this.setPrefWidth(tagNameField.getText().length() * 10 + 50);
        setTagColor(tag.getColor());

        Button deleteButton = createDeleteButton();
        this.setRight(deleteButton);
    }

    private void setTagColor(String color) {
        this.setStyle(this.getStyle() + "-fx-background-color: " + color + ";");
    }

    private TextField createTextField(String tagName) {
        TextField textField = new TextField(tagName);
        textField.setStyle("-fx-background-color: transparent;");
        textField.setAlignment(Pos.CENTER);
        textField.setMinHeight(10);


        textField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER))
                listener.tagNameChanged(tag, this.tagNameField.getText());
        });

        return textField;
    }

    private Button createDeleteButton() {
        Button button = new Button();
        button.setStyle("-fx-background-color: transparent;");

        FontIcon trashIcon = new FontIcon("mdi2t-trash-can-outline");
        button.setGraphic(trashIcon);

        button.setOnAction(actionEvent -> {
            listener.tagDeleted(this.tag);
        });

        return button;
    }

    public interface Listener {
        void tagNameChanged(Tag tag, String tagName);
        void tagDeleted(Tag tag);
    }
}
