package ulb.infof307.g01.server.handler;

import spark.Request;
import spark.Response;
import ulb.infof307.g01.server.database.dao.UserDAO;
import ulb.infof307.g01.server.service.JWTService;

import java.util.Map;

import static spark.Spark.*;


public class UserAccountHandler extends Handler {

    private final UserDAO userDAO = new UserDAO();
    private final JWTService jwtService = new JWTService();

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
        boolean isTokenValid = jwtService.isTokenValid(token);
        if (isTokenValid) {
            String username = jwtService.getUsernameFromToken(token);
            System.out.println(username);
            return successfulResponse;
        } else {
            System.out.println("Token is not valid");
            return failedResponse;
        }
    }

    private Map<String, String> loginUser(Request request, Response response) {
        String username = request.queryParams("username");
        String password = request.queryParams("password");
        boolean isValidLogin = userDAO.loginUser(username, password);
        if (isValidLogin) {
            String token = jwtService.generateToken(username);
            response.cookie("token", token);
        }
        else {
            logger.info("Invalid login");
        }

        return isValidLogin ? successfulResponse : failedResponse;
    }

    private Map<String, String> registerUser(Request request, Response response) {
        String username = request.queryParams("username");
        String password = request.queryParams("password");
        boolean isRegistered = userDAO.registerUser(username, password);
        return isRegistered ? successfulResponse : failedResponse;
    }
}
