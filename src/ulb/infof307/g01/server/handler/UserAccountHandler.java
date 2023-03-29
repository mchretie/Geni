package ulb.infof307.g01.server.handler;

import spark.Request;
import spark.Response;
import ulb.infof307.g01.server.database.Database;
import ulb.infof307.g01.server.service.JWTService;

import java.util.Map;

import static spark.Spark.*;

@SuppressWarnings("FieldCanBeLocal")
public class UserAccountHandler extends Handler {

    private final String BASE_PATH     = "/api/user";
    private final String REGISTER_PATH = "/register";
    private final String LOGIN_PATH    = "/login";

    private final String AUTH_HEADER = "Authorization";

    private final Database database;
    private final JWTService jwtService;

    public UserAccountHandler(JWTService jwtService, Database database) {
        this.jwtService = jwtService;
        this.database = database;
    }

    @Override
    public void init() {
        path(BASE_PATH, () ->{
            post(REGISTER_PATH, this::registerUser);
            get(LOGIN_PATH, this::loginUser);
        });
    }

    private Map<String, String> loginUser(Request request, Response response) {
        try {
            String username = request.queryParams("username");
            String password = request.queryParams("password");

            boolean isValidLogin = database.loginUser(username, password);

            if (isValidLogin) {
                String token = jwtService.generateToken(username);
                response.header(AUTH_HEADER, token);
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
