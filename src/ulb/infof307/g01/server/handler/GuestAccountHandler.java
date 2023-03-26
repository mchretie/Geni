package ulb.infof307.g01.server.handler;

import spark.Request;
import spark.Response;
import ulb.infof307.g01.server.database.dao.UserDAO;

import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import static spark.Spark.*;

public class GuestAccountHandler extends Handler {
    private static final Logger logger = Logger.getLogger(GuestAccountHandler.class.getName());

    private final UserDAO userDAO = new UserDAO();

    @Override
    public void init() {
        logStart();

        path("/api", () -> {
            path("/guest", () -> {
                get("/register", this::registerGuest, toJson());
            });
        });

        logStarted();
    }

    private Map<String, String> registerGuest(Request req, Response res) {
        UUID guestId;
        try {
            guestId = UUID.fromString(req.queryParams("guest_id"));

            if (!userDAO.userExists(guestId))
                userDAO.registerGuest(guestId);

            logger.info("Registered guest with uuid " + guestId);

            return successfulResponse;

        } catch (Exception e) {
            logger.warning("Failed to register guest: " + e.getMessage());
            halt(500, "Failed to register guest");

            return failedResponse;
        }
    }

}
