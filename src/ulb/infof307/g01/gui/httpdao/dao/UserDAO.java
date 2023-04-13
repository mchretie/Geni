package ulb.infof307.g01.gui.httpdao.dao;

import com.google.gson.Gson;
import ulb.infof307.g01.shared.constants.ServerPaths;
import ulb.infof307.g01.gui.httpdao.exceptions.ServerRequestFailed;


import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Map;
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
        System.out.println("check login response code");
        checkResponseCode(response.statusCode());

        System.out.println(response.body());


        Gson gson = new Gson();
        Map<String, String> attributes = gson.fromJson(response.body(), Map.class);

        if ( attributes.get("success").equals("false") ) {
            throw new ServerRequestFailed("Login failed");
        }

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

        System.out.println("check signup response code");

        checkResponseCode(response.statusCode());

        Gson gson = new Gson();
        Map<String, String> attributes = gson.fromJson(response.body(), Map.class);

        if ( attributes.get("success").equals("false") ) {
            throw new ServerRequestFailed("Register failed");
        }

        System.out.println(response.body());
    }
}
