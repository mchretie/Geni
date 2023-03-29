package ulb.infof307.g01.gui.httpclient.dao;

import ulb.infof307.g01.gui.httpclient.HttpClientAPI;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.UUID;

public class UserDAO extends HttpClientAPI {

    /* ====================================================================== */
    /*                               DAO methods                              */
    /* ====================================================================== */

    public void login(String username, String password)
            throws IOException, InterruptedException {

        System.out.println("login");

        HttpResponse<String> response = get("/api/user/login"
                + "?username=" + username + "&password=" + password);

        checkResponseCode(response.statusCode());

        // TODO : throw exception if no token is found
        Optional<String> authorization
                = response.headers().firstValue("Authorization");

        System.out.println(authorization);

        authorization.ifPresent(this::setToken);
    }

    public void register(String username, String password)
            throws IOException, InterruptedException {

        HttpResponse<String> response = post("/api/user/register"
                + "?username=" + username + "&password=" + password, "");

        checkResponseCode(response.statusCode());
    }
}
