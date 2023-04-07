package ulb.infof307.g01.gui.httpdao.dao;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.prefs.Preferences;
import ulb.infof307.g01.gui.httpdao.dao.UserDAO;

public class UserDAO extends HttpDAO {

  private String username;
  private String password;

  /* ====================================================================== */
  /*                               DAO methods                              */
  /* ====================================================================== */

  public void login(String username, String password)
      throws IOException, InterruptedException {

    HttpResponse<String> response =
        get("/api/user/login"
            + "?username=" + username + "&password=" + password);

    checkResponseCode(response.statusCode());

    // TODO : throw exception if no token is found
    Optional<String> authorization =
        response.headers().firstValue("Authorization");

    authorization.ifPresent(this::setToken);

    if (!username.equals("guest") && !password.equals("guest")) {
      // Saves de values LOCALLY on the machine
      Preferences prefs = Preferences.userNodeForPackage(UserDAO.class);
      prefs.put("localUsername", username);
      prefs.put("localPassword", password);
    }
  }

    public void register(String username, String password)
            throws IOException, InterruptedException {

        HttpResponse<String> response = post("/api/user/register"
                + "?username=" + username + "&password=" + password, "");

        checkResponseCode(response.statusCode());
    }

    public boolean userCredentialsExist() {
      // Checks if the user has already logged in
      Preferences prefs = Preferences.userNodeForPackage(UserDAO.class);
      this.username = prefs.get("localUsername", null);
      this.password = prefs.get("localPassword", null);
      return username != null && password != null;
    }

    public void loginWithCredentials() {
      // Checks if the user has already logged in
      Preferences prefs = Preferences.userNodeForPackage(UserDAO.class);

      try {
        login(this.username, this.password);
      } catch (IOException | InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    public void loginAsGuest() {
      // TODO : in future guest will not contact server ?
      try {
        // default guest login
        login("guest", "guest");
      } catch (IOException | InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    public void logout() {
      // Removes local credentials
      Preferences prefs = Preferences.userNodeForPackage(UserDAO.class);
      prefs.remove("localUsername");
      prefs.remove("localPassword");
      try {
        login("guest", "guest");
      } catch (IOException | InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
}
