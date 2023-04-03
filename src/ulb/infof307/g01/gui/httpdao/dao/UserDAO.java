package ulb.infof307.g01.gui.httpdao.dao;

import ulb.infof307.g01.shared.constants.ServerPaths;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Optional;

public class UserDAO extends HttpDAO {

    /* ====================================================================== */
    /*                               DAO methods                              */
    /* ====================================================================== */

    public void login(String username, String password)
            throws IOException, InterruptedException {

        String credentials = "?username=" + username + "&password=" + password;
        HttpResponse<String> response
                = get(ServerPaths.LOGIN_PATH + credentials);

        checkResponseCode(response.statusCode());

        // TODO : throw exception if no token is found
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
    }
}
