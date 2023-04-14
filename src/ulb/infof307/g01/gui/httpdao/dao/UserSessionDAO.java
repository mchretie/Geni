package ulb.infof307.g01.gui.httpdao.dao;

import com.google.gson.Gson;
import ulb.infof307.g01.gui.httpdao.exceptions.AuthenticationFailedException;
import ulb.infof307.g01.shared.constants.ServerPaths;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Optional;
import java.util.prefs.Preferences;


public class UserSessionDAO extends HttpDAO {

    private String username;
    private final Preferences prefs = Preferences.userNodeForPackage(UserSessionDAO.class);


    /* ====================================================================== */
    /*                               Getters Setters                          */
    /* ====================================================================== */

    public String getUsername() {
        return username;
    }

    /* ====================================================================== */
    /*                               DAO methods                              */
    /* ====================================================================== */

    public void login(String username, String password)
            throws IOException, InterruptedException {

        String credentials = "?username=" + username + "&password=" + password;
        HttpResponse<String> response
                = get(ServerPaths.LOGIN_PATH + credentials);

        checkResponseCode(response.statusCode());
        checkResponseBody(response.body(), "Login");

        saveCredentials(username, password);
        this.username = username;

        Optional<String> authorization
                = response.headers().firstValue("Authorization");

        authorization.ifPresent(this::setToken);
    }

    public void register(String username, String password)
            throws IOException, InterruptedException {

        String credentials = "?username=" + username + "&password=" + password;
        HttpResponse<String> response
                = post(ServerPaths.REGISTER_PATH + credentials, "");

        checkResponseCode(response.statusCode());
        checkResponseBody(response.body(), "Registration");
    }

    private void checkResponseBody(String response, String type) throws AuthenticationFailedException {
        Map<String, String> responseMap
                = new Gson().fromJson(response, Map.class);

        if (!Boolean.parseBoolean(responseMap.get("success"))) {
            throw new AuthenticationFailedException(type + " failed");
        }
    }
    /* ====================================================================== */
    /*                             Auto-login                                 */
    /* ====================================================================== */

    public boolean isLoggedIn() {
        return !getToken().isEmpty();
    }

    private void removeToken() {
        setToken("");
    }

    public void removeCredentials() {
        this.username = "";
        this.prefs.remove("localUsername");
        this.prefs.remove("localPassword");
    }

    public void saveCredentials(String username, String password) {
        this.prefs.put("localUsername", username);
        this.prefs.put("localPassword", password);
    }

    public void attemptAutologin() throws IOException, InterruptedException {

        String username = this.prefs.get("localUsername", null);
        String password = this.prefs.get("localPassword", null);

        if (username != null && password != null) {
            login(username, password);
        }
    }

    public void logout() {
        removeCredentials();
        removeToken();
    }
}
