package ulb.infof307.g01.server.handler;

import spark.Request;
import spark.Response;
import ulb.infof307.g01.server.database.Database;
import ulb.infof307.g01.server.service.JWTService;

import java.util.Map;

import static spark.Spark.*;


public class UserAccountHandler extends Handler {

    private final Database database;

    public UserAccountHandler(Database database) {
        this.database = database;
    }

    @Override
    public void init() {
        logStart();

        path("/api", () -> {
            path("/user", () -> {
                post("/register", this::registerUser);
                get("/login", this::loginUser);
                get("/test", this::testAuth);
//                get("/logout", this::logoutUser);
//                get("/delete", this::deleteUser);
//                get("/update", this::updateUser);
            });
        });

        logStarted();
    }

    private Map<String, String> testAuth(Request request, Response response) {
        String token = request.cookie("token");
        boolean isTokenValid = JWTService.getInstance().isTokenValid(token);
        if (isTokenValid) {
            String username = JWTService.getInstance().getUsernameFromToken(token);
            System.out.println(username);
            return successfulResponse;
        } else {
            System.out.println("Token is not valid");
            return failedResponse;
        }
    }

    private Map<String, String> loginUser(Request request, Response response) {
        try {
            String username = request.queryParams("username");
            String password = request.queryParams("password");

            JWTService jwtService = JWTService.getInstance();

            boolean isValidLogin = database.loginUser(username, password);
            if (isValidLogin) {
                String token = jwtService.generateToken(username);
                response.header("Authorization", token);
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
