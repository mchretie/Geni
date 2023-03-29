package ulb.infof307.g01.server.handler;

import spark.Request;
import spark.Response;
import ulb.infof307.g01.server.database.Database;
import ulb.infof307.g01.server.service.JWTService;

import java.util.Map;

import static spark.Spark.*;


@SuppressWarnings("FieldCanBeLocal")
public class UserAccountHandler extends Handler {

    private final String REGISTER_PATH = "/api/user/register";
    private final String LOGIN_PATH    = "/api/user/login";
    private final String AUTH          = "/api/user/auth";

    private final Database database;
    private final JWTService jwtService;

    public UserAccountHandler(JWTService jwtService, Database database) {
        this.jwtService = jwtService;
        this.database = database;
    }

    @Override
    public void init() {
        post(REGISTER_PATH, this::registerUser);
        get(LOGIN_PATH, this::loginUser);
    }

    private Map<String, String> loginUser(Request request, Response response) {
        try {
            String username = request.queryParams("username");
            String password = request.queryParams("password");

            boolean isValidLogin = database.loginUser(username, password);

            if (isValidLogin) {
                String token = jwtService.generateToken(username);
                response.header(AUTH, token);
            }

            else {
                logger.info("Invalid login");
            }

            return isValidLogin ? successfulResponse : failedResponse;

        } catch (Exception e) {
            logger.warning("Error while logging in: " + e);
            return failedResponse;
        }
    }

    private Map<String, String> registerUser(Request request, Response response) {
        String username = request.queryParams("username");
        String password = request.queryParams("password");

        boolean isRegistered = database.registerUser(username, password);
        return isRegistered ? successfulResponse : failedResponse;
    }
}
