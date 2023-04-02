package ulb.infof307.g01.gui.controller;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.infof307.g01.gui.view.editdeck.EditDeckViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.gui.view.profile.ProfileViewController;
import ulb.infof307.g01.model.Card;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.Tag;

public class ProfileController implements ProfileViewController.Listener {

  private final Stage stage;

  private final MainWindowViewController mainWindowViewController;
  private final ProfileViewController profileViewController;

  private final ControllerListener controllerListener;

  /* ====================================================================== */
  /*                              Constructor                               */
  /* ====================================================================== */

  public ProfileController(Stage stage,
                           MainWindowViewController mainWindowViewController,
                           ControllerListener controllerListener) {

    this.stage = stage;
    this.mainWindowViewController = mainWindowViewController;
    this.controllerListener = controllerListener;
    System.out.println("initialisation de profileViewController");
    this.profileViewController =
        mainWindowViewController.getProfileViewController();
    System.out.println(this.profileViewController);
    this.profileViewController.setListener(this);
  }

  /* ====================================================================== */
  /*                         Stage Manipulation                             */
  /* ====================================================================== */

  /**
   * Loads and displays the Deck Menu onto the main scene
   *
   * @throws IOException if FXMLLoader.load() fails
   */
  public void show() throws IOException {
    // Todo :  tests
    FXMLLoader loader = new FXMLLoader(
        ProfileViewController.class.getResource("ProfileView.fxml"));
    loader.load();

    Parent root = loader.getRoot();
    stage.setScene(new Scene(root));
    stage.show();
  }

  /* ====================================================================== */
  /*                        View Listener Method                            */
  /* ====================================================================== */

  //@Override
  // public void profileClicked() {
  // controllerListener.profileClicked();
  //}

  /* ====================================================================== */
  /*                   Controller Listener Interface                        */
  /* ====================================================================== */

  public interface ControllerListener {
    void handleProfileClicked();
  }
}
