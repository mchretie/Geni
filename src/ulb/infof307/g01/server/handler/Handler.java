package ulb.infof307.g01.server.handler;

import com.google.gson.Gson;
import spark.ResponseTransformer;
import ulb.infof307.g01.server.database.Database;
import ulb.infof307.g01.server.service.JWTService;

import java.util.Map;
import java.util.logging.Logger;

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

    public abstract void init();
}
