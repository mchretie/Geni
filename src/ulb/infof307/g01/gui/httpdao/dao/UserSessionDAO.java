package ulb.infof307.g01.gui.httpdao.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ulb.infof307.g01.gui.httpdao.exceptions.AuthenticationFailedException;
import ulb.infof307.g01.model.UserAuth;
import ulb.infof307.g01.shared.constants.ServerPaths;

import java.io.IOException;
import java.lang.reflect.Type;
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

        UserAuth userAuth = new UserAuth(username, password);
        HttpResponse<String> response = bodyRequest(ServerPaths.LOGIN_PATH, new Gson().toJson(userAuth));

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

        UserAuth userAuth = new UserAuth(username, password);
        HttpResponse<String> response = bodyRequest(ServerPaths.REGISTER_PATH, new Gson().toJson(userAuth));


        checkResponseCode(response.statusCode());
        checkResponseBody(response.body(), "Registration");
    }

    private void checkResponseBody(String response, String type) throws AuthenticationFailedException {

        Type stringBooleanMap
                = new TypeToken<Map<String, Boolean>>() {
        }.getType();

        Map<String, Boolean> responseMap
                = new Gson().fromJson(response, stringBooleanMap);

        if (!responseMap.get("success")) {
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
