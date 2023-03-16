package ulb.infof307.g01.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.infof307.g01.database.Database;
import ulb.infof307.g01.database.DatabaseScheme;
import ulb.infof307.g01.database.OpenedDatabaseException;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.view.deckmenu.DeckMenuViewController;
import ulb.infof307.g01.view.mainwindow.MainWindowViewController;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Main class of the application which initializes the main view using the main view handler and loads a menu view.
 */
public class MainFxController extends Application implements MainWindowViewController.NavigationListener,
                                                                DeckMenuController.ControllerListener {

    DeckMenuController deckMenuController;

    MainWindowViewController mainWindowViewController;
    private Stage stage;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {

        this.stage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(MainWindowViewController.class.getResource("MainWindowView.fxml"));
        Parent root = fxmlLoader.load();
        stage.setScene(new Scene(root));

        try {
            initDatabase();

        } catch (SQLException e) {
            e.printStackTrace();

        } catch (OpenedDatabaseException e) {
            // display error message
        }

        mainWindowViewController = fxmlLoader.getController();
        mainWindowViewController.setListener(this);

        deckMenuController = new DeckMenuController(stage, this, mainWindowViewController);
        deckMenuController.show();
    }

    private void initDatabase() throws SQLException, OpenedDatabaseException {
        File dbfile = new File("demo.db");
        Database db = Database.singleton();
        db.open(dbfile);
        db.initTables(DatabaseScheme.CLIENT);
    }

    @Override
    public void editDeckClicked(Deck deck) {
        EditDeckController editDeckController = new EditDeckController(stage, deck, mainWindowViewController);

        try {
            editDeckController.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void playDeckClicked(Deck deck) {
        PlayDeckController playDeckController = new PlayDeckController(stage, deck, mainWindowViewController);
        playDeckController.show();
    }

    @Override
    public void goBackClicked() {
        try {
            deckMenuController.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Display error
        }
    }

    @Override
    public void goToHomeClicked() {

    }

    @Override
    public void goToCurrentDeckClicked() {

    }

    @Override
    public void goToAboutClicked() {

    }
}
