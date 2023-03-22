package ulb.infof307.g01.backend.user;

import spark.Request;
import spark.Response;
import ulb.infof307.g01.backend.Handler;

import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import static spark.Spark.*;

public class GuestHandler implements Handler {
    private static final Logger logger = Logger.getLogger(GuestHandler.class.getName());

    @Override
    public void init() {
        logger.info("Starting guest handler");
        path("/api", () -> {
            path("/guest", () -> {
                get("/register", this::registerGuest);
            });
        });
        logger.info("Guest handler started");
    }

    private Map<String, String> registerGuest(Request req, Response res) {
        UUID uuid = UUID.randomUUID();
        logger.info("Received request to register guest with uuid " + uuid);
        res.type("text/json");
        return Map.of("uuid", uuid.toString());
    }

}
