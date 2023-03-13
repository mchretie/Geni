package ulb.infof307.g01.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.infof307.g01.view.EditDeckViewController;


import java.io.IOException;

/**
 * Main class of the application which initializes the main view using the main view handler and loads a menu view.
 */
public class EditDeckController extends Application implements EditDeckViewController.EditDeckListener {
    private Stage currentStage;

    @Override
    public void start(Stage primaryStage) {
        currentStage = primaryStage;
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(EditDeckViewController.class.getResource("EditDeckView.fxml"));
            Parent root = fxmlLoader.load();
            EditDeckViewController controller = fxmlLoader.getController();
            controller.setListener(this);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
        catch (IOException e){
            showErrorAlert();
        }
    }

    private void showErrorAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("La fenêtre n'a pas pu être chargée");
        //... TODO
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }

    public void onAddCardButton() {
        //  TODO add Card ...
    }
}
