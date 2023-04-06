package ulb.infof307.g01.gui.controller;

import java.io.IOException;
import javafx.stage.Stage;
import ulb.infof307.g01.gui.httpdao.dao.UserDAO;
import ulb.infof307.g01.gui.view.login.LoginViewController;
import ulb.infof307.g01.gui.view.mainwindow.MainWindowViewController;

public class LoginController implements LoginViewController.ViewListener {

  private final Stage stage;

  private final MainWindowViewController mainWindowViewController;

  private final LoginViewController loginViewController;

  private final ControllerListener controllerListener;

  private final UserDAO userDAO = new UserDAO();

  /* ====================================================================== */
  /*                              Constructor                               */
  /* ====================================================================== */

  public LoginController(Stage stage,
                         MainWindowViewController mainWindowViewController,
                         ControllerListener controllerListener) {

    this.stage = stage;
    this.mainWindowViewController = mainWindowViewController;
    this.controllerListener = controllerListener;

    this.loginViewController =
        mainWindowViewController.getLoginViewController();

    System.out.println(
        "initialisation de loginViewController setting listener de :");
    System.out.println(this.loginViewController);
    System.out.println(this);

    this.loginViewController.setListener(this);
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
    System.out.println("showing login");

    mainWindowViewController.setLoginViewVisible();
    mainWindowViewController.makeGoBackIconVisible();
    // Todo :  tests
    /*
    FXMLLoader loader = new FXMLLoader(
        ProfileViewController.class.getResource("LoginView.fxml"));
    loader.load();

    ProfileViewController controller = loader.getController();

    Parent root = loader.getRoot();
    stage.setScene(new Scene(root));*/
    stage.show();
  }

  /* ====================================================================== */
  /*                        View Listener Method                            */
  /* ====================================================================== */

  @Override
  public void loginClicked() {
    System.out.println("login clicked");
    // Todo :  tests
    // userDAO.login("test", "test"); !! see mainFX for use example
    // get information from the text fields
    // String pswd = this.passwordField.getText();
    // String username = this.usernameField.getText();
    // use dao to login
  }

  /* ====================================================================== */
  /*                   Controller Listener Interface                        */
  /* ====================================================================== */

  public interface ControllerListener {
    ;
  }
}
