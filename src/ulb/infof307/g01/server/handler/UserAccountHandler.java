package ulb.infof307.g01.server.handler;

import spark.Request;
import spark.Response;
import ulb.infof307.g01.server.database.dao.UserDAO;

import java.util.Map;

import static spark.Spark.*;


public class UserAccountHandler extends Handler {

    private final UserDAO userDAO = new UserDAO();

    @Override
    public void init() {
        logStart();

        path("/api", () -> {
            path("/user", () -> {
                post("/register", this::registerUser);
                get("/login", this::loginUser);
//                get("/logout", this::logoutUser);
//                get("/delete", this::deleteUser);
//                get("/update", this::updateUser);
            });
        });

        logStarted();
    }

    private Map<String, String> loginUser(Request request, Response response) {
        return null;
    }

    private Map<String, String> registerUser(Request request, Response response) {
        String username = request.queryParams("username");
        String password = request.queryParams("password");
        boolean isRegistered = userDAO.registerUser(username, password);
        return isRegistered ? successfulResponse : failedResponse;
    }
}
