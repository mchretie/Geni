package ulb.infof307.g01.views;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;

import java.awt.event.MouseEvent;
import java.beans.EventHandler;

/**
 * View for adding, removing or modifying decks
 */
public class ManageDecksView implements View {

    private BorderPane view;

    public ManageDecksView() {
        view = new BorderPane();
        initButtons();
    }

    private void initButtons() {

        Image plusImage = new Image("img/plus.png");
        ImageView plusImageView = new ImageView(plusImage);
        plusImageView.setFitHeight(50);
        plusImageView.setFitWidth(50);

        Button addDeckButton = new Button();
        addDeckButton.setGraphic(plusImageView);

        final String addDeckButtonStyle =
                "-fx-background-radius: 50%;"
                        + "-fx-pref-width: 110px;"
                        + "-fx-pref-height: 110px;"
                        + "-fx-background-color: grey;"
                        + "-fx-border-radius: 50%;"
                        + "-fx-border-width: 2px;"
                        + "-fx-font-size: 40px;"
                        + "-fx-padding: 20px;"
                        + "-fx-border-insets: 25px;"
                        + "-fx-background-insets: 25px;";

        addDeckButton.setStyle(addDeckButtonStyle + "-fx-border-color: black;");

        addDeckButton.setOnMouseEntered(e ->
                addDeckButton.setStyle(addDeckButtonStyle + "-fx-border-color: white;")
        );

        addDeckButton.setOnMouseExited(e ->
                addDeckButton.setStyle(addDeckButtonStyle + "-fx-border-color: black;")
        );

        HBox hBox = new HBox();
        hBox.getChildren().add(addDeckButton);
        hBox.setAlignment(Pos.CENTER_RIGHT);

        view.setBottom(hBox);
    }

    public Pane getPane() { return view; }
}
