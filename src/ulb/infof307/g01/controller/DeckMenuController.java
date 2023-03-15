package ulb.infof307.g01.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.infof307.g01.database.Database;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.view.MainViewController;
import ulb.infof307.g01.view.deckmenu.DeckMenuViewController;
import ulb.infof307.g01.view.deckmenu.DeckViewController;

import java.io.IOException;

public class DeckMenuController implements DeckMenuViewController.Listener, DeckViewController.Listener {

    private final Stage stage;
    private final ControllerListener controllerListener;

    public DeckMenuController(Stage stage, ControllerListener controllerListener) {
        this.stage = stage;
        this.controllerListener = controllerListener;
    }

    public void show() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainViewController.class.getResource("../view/deckmenu/DeckMenuView.fxml"));

        DeckMenuViewController deckMenuViewController = fxmlLoader.getController();
        deckMenuViewController.setListener(this);

        Parent root = fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void createDeckClicked(String name) {

    }

    @Override
    public void deckRemoved(Deck deck) {

    }

    @Override
    public void deckDoubleClicked(Deck deck) {
        controllerListener.playDeckClicked(deck);
    }

    @Override
    public void editDeckClicked(Deck deck) {
        controllerListener.editDeckClicked(deck);
    }

    public interface ControllerListener {
        void editDeckClicked(Deck deck);
        void playDeckClicked(Deck deck);
    }
}
