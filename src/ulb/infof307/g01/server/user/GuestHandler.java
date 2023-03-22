package ulb.infof307.g01.server.user;

import spark.Request;
import spark.Response;
import ulb.infof307.g01.server.Handler;

import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import static spark.Spark.*;
import static ulb.infof307.g01.server.utils.JsonUtil.json;

public class GuestHandler implements Handler {
    private static final Logger logger = Logger.getLogger(GuestHandler.class.getName());

    @Override
    public void init() {
        logger.info("Starting guest handler");
        path("/api", () -> {
            path("/guest", () -> {
                get("/register", this::registerGuest, json());
            });
        });
        logger.info("Guest handler started");
    }

    private Map<String, String> registerGuest(Request req, Response res) {
        UUID uuid = UUID.randomUUID();
        logger.info("Registered guest with uuid " + uuid);
        res.type("application/json");
        return Map.of("uuid", uuid.toString());
    }

}
