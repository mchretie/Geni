package ulb.infof307.g01.server.handler;

import com.google.gson.Gson;
import spark.Request;
import spark.ResponseTransformer;
import ulb.infof307.g01.server.database.Database;
import ulb.infof307.g01.server.service.JWTService;

import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import static spark.Spark.halt;

public abstract class Handler {

    protected final JWTService jwtService;
    protected final Database database;

    public Handler(Database database, JWTService jwtService) {
        this.database = database;
        this.jwtService = jwtService;
    }

    protected final Map<String, Boolean> successfulResponse
            = Map.of("success", true);

    protected final Map<String, Boolean> failedResponse
            = Map.of("success", false);

    protected final Logger logger = Logger.getLogger(getClass().getName());

    public ResponseTransformer toJson() {
        return (object) -> new Gson().toJson(object);
    }

    /**
     * Extracts the username from the request's Authorization header.
     * <p>
     * If the token is invalid, the request is halted.
     * Otherwise, the username is returned.
     * </p>
     *
     * @param req the request
     * @return the username
     */
    protected String usernameFromRequest(Request req) {
        String token = req.headers("Authorization");
        checkToken(token);
        return jwtService.getUsernameFromToken(token);
    }

    protected UUID userIdFromRequest(Request req) {
        String token = req.headers("Authorization");
        checkToken(token);
        return UUID.fromString(this.database.getUserId(jwtService.getUsernameFromToken(token)));
    }

    protected void checkToken(String token) {
        if (token == null || !jwtService.isTokenValid(token)) {
            halt(401, "Token is " + (token == null ? "null" : "not valid"));
        }
    }

    public abstract void init();
}
