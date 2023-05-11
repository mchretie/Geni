package ulb.infof307.g01.gui.http.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ulb.infof307.g01.model.UserAuth;
import ulb.infof307.g01.gui.http.exceptions.AuthenticationFailedException;
import ulb.infof307.g01.shared.constants.ServerPaths;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Optional;
import java.util.prefs.Preferences;


public class UserSessionDAO extends HttpDAO {

    private String username = "Guest";
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
            throws IOException, InterruptedException, AuthenticationFailedException {

        UserAuth userAuth = new UserAuth(username, password);
        HttpResponse<String> response = post(ServerPaths.LOGIN_PATH, new Gson().toJson(userAuth));

        checkResponseCode(response.statusCode());
        checkResponseBody(response.body(), "Login");

        saveCredentials(username, password);
        this.username = username;

        Optional<String> authorization
                = response.headers().firstValue("Authorization");

        authorization.ifPresent(this::setJWT);
    }

    public void register(String username, String password)
            throws IOException, InterruptedException, AuthenticationFailedException {

        UserAuth userAuth = new UserAuth(username, password);
        HttpResponse<String> response = post(ServerPaths.REGISTER_PATH, new Gson().toJson(userAuth));


        checkResponseCode(response.statusCode());
        checkResponseBody(response.body(), "Registration");
    }

    private void checkResponseBody(String response, String type) throws AuthenticationFailedException {

        Type stringBooleanMap
                = new TypeToken<Map<String, Boolean>>() {
        }.getType();

        Map<String, Boolean> responseMap
                = new Gson().fromJson(response, stringBooleanMap);

        if (Boolean.FALSE.equals(responseMap.get("success"))) {
            throw new AuthenticationFailedException(type + " failed");
        }
    }
    /* ====================================================================== */
    /*                             Auto-login                                 */
    /* ====================================================================== */

    public boolean isLoggedIn() {
        return !getJWT().isEmpty();
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

    public void attemptAutoLogin()
            throws IOException, InterruptedException, AuthenticationFailedException {

        String localUsername = this.prefs.get("localUsername", null);
        String localPassword = this.prefs.get("localPassword", null);


        if (localUsername == null || localPassword == null) {
            throw new AuthenticationFailedException("No credentials found");
        }

        login(localUsername, localPassword);
    }

    public void logout() {
        removeCredentials();
        removeJWT();
    }
}
