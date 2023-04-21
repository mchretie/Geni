package ulb.infof307.g01.server.handler;

import static spark.Spark.*;
import static ulb.infof307.g01.shared.constants.ServerPaths.*;

import java.util.Map;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import ulb.infof307.g01.model.UserAuth;
import ulb.infof307.g01.server.database.Database;
import ulb.infof307.g01.server.service.JWTService;


@SuppressWarnings("FieldCanBeLocal")
public class UserAccountHandler extends Handler {

    private final String AUTH_HEADER = "Authorization";

    public UserAccountHandler(JWTService jwtService, Database database) {
        super(database, jwtService);
    }

    @Override
    public void init() {
        post(REGISTER_PATH, this::registerUser, toJson());
        post(LOGIN_PATH, this::loginUser, toJson());
        get(IS_TOKEN_VALID_PATH, this::isTokenValid, toJson());
    }

    private Map<String, Boolean> loginUser(Request request, Response response) {
        try {
            UserAuth userAuth = new Gson().fromJson(request.body(), UserAuth.class);
            String username = userAuth.getUsername();
            String password = userAuth.getPassword();

            boolean isValidLogin = database.loginUser(username, password);

            if (isValidLogin) {
                String token = jwtService.generateToken(username);
                response.header(AUTH_HEADER, token);
            } else {
                logger.info("Invalid login");
            }

            return isValidLogin ? successfulResponse : failedResponse;

        } catch (Exception e) {
            logger.warning("Error while logging in: " + e);
            return failedResponse;
        }
    }

    private Map<String, Boolean> registerUser(Request request, Response response) {
        UserAuth userAuth = new Gson().fromJson(request.body(), UserAuth.class);
        String username = userAuth.getUsername();
        String password = userAuth.getPassword();

        boolean isRegistered = database.registerUser(username, password);
        return isRegistered ? successfulResponse : failedResponse;
    }

    private Map<String, Boolean> isTokenValid(Request request, Response response) {
        String token = request.headers(AUTH_HEADER);
        if (jwtService.isTokenValid(token)) {
            return successfulResponse;
        }
        halt(401, "Token is " + (token == null ? "null" : "not valid"));
        return null;
    }
}
