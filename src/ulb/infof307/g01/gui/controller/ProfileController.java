package ulb.infof307.g01.gui.controller;

import java.io.IOException;
import javafx.stage.Stage;
import ulb.infof307.g01.gui.httpdao.dao.UserDAO;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;
import ulb.infof307.g01.gui.view.profile.ProfileViewController;

public class ProfileController implements ProfileViewController.ViewListener {

  private final Stage stage;

  private final MainWindowViewController mainWindowViewController;

  private final ProfileViewController profileViewController;

  private final ControllerListener controllerListener;

  private boolean loggedIn = false;

  private final UserDAO userDAO = new UserDAO();

  /* ====================================================================== */
  /*                              Constructor                               */
  /* ====================================================================== */
  public ProfileController(
      Stage stage, MainWindowViewController mainWindowViewController,
      ProfileController.ControllerListener controllerListener) {

    this.stage = stage;
    this.mainWindowViewController = mainWindowViewController;
    this.controllerListener = controllerListener;

    this.profileViewController =
        mainWindowViewController.getProfileViewController();

    System.out.println(
        "initialisation de profileViewController setting listener de :");
    System.out.println(this.profileViewController);
    System.out.println(this);

    this.profileViewController.setListener(this);
  }

  /* ====================================================================== */
  /*                              Getters                                   */
  /* ====================================================================== */
  public boolean isLoggedIn() { return loggedIn; }
  /* ====================================================================== */
  /*                              Setters                                   */
  /* ====================================================================== */
  public void setLoggedIn(boolean loggedInVal) { this.loggedIn = loggedInVal; }
  /* ====================================================================== */
  /*                         Stage Manipulation                             */
  /* ====================================================================== */

  /**
   * Loads and displays the Deck Menu onto the main scene
   *
   * @throws IOException if FXMLLoader.load() fails
   */
  public void show() throws IOException {
    System.out.println("showing profile");

    mainWindowViewController.setProfileViewVisible();
    mainWindowViewController.makeGoBackIconVisible();

    stage.show();
  }

  /* ====================================================================== */
  /*                        View Listener Method                            */
  /* ====================================================================== */

  @Override
  public void logoutButtonClicked() {
    System.out.println("logoutButton clicked clicked");
    controllerListener.handleLogout();
  }

  /* ====================================================================== */
  /*                   Controller Listener Interface                        */
  /* ====================================================================== */

  public interface ControllerListener {
    void handleLogout();
  }
}
