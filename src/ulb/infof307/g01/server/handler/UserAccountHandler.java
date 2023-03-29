package ulb.infof307.g01.server.handler;

import spark.Request;
import spark.Response;
import ulb.infof307.g01.server.database.Database;
import ulb.infof307.g01.server.service.JWTService;

import java.util.Map;

import static spark.Spark.*;


public class UserAccountHandler extends Handler {

    private final Database database;
    private final JWTService jwtService;

    public UserAccountHandler(JWTService jwtService, Database database) {
        this.jwtService = jwtService;
        this.database = database;
    }

    @Override
    public void init() {
        logStart();

        before("/api/user/*", (req, res) -> {
            logger.info("Received request: "
                    + req.requestMethod()
                    + " "
                    + req.pathInfo()
                    + " "
                    + req.queryParams());
        });

        path("/api", () -> {
            path("/user", () -> {
                post("/register", this::registerUser);
                get("/login", this::loginUser);
                get("/test", this::testAuth);
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
        try {
            System.out.println("Login request received");
            String username = request.queryParams("username");
            String password = request.queryParams("password");

            System.out.println("Username: " + username);
            System.out.println("Password: " + password);

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
