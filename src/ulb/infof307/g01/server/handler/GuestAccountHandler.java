package ulb.infof307.g01.server.handler;

import spark.Request;
import spark.Response;
import ulb.infof307.g01.server.database.Database;
import ulb.infof307.g01.server.database.dao.UserDAO;
import ulb.infof307.g01.server.service.JWTService;

import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import static spark.Spark.*;

public class GuestAccountHandler extends Handler {
    private static final Logger logger = Logger.getLogger(GuestAccountHandler.class.getName());

    private final Database database;
    private final JWTService jwtService;

    public GuestAccountHandler(JWTService jwtService, Database database) {
        this.jwtService = jwtService;
        this.database = database;
    }

    @Override
    public void init() {
        logger.info("Starting guest handler");
        path("/api", () -> {
            path("/guest", () -> {
                get("/register", this::registerGuest, toJson());
            });
        });
        logger.info("Guest handler started");
    }

    private Map<String, String> registerGuest(Request req, Response res) {
        UUID uuid = UUID.randomUUID();
        try {
            // database.registerGuest(uuid);
            logger.info("Registered guest with uuid " + uuid);
        } catch (Exception e) {
            logger.warning("Failed to register guest: " + e.getMessage());
            halt(500, "Failed to register guest");
        }
        res.type("application/json");
        return Map.of("uuid", uuid.toString());
    }

}
