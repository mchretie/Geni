package ulb.infof307.g01.server.handler;

import com.google.gson.Gson;
import spark.ResponseTransformer;

import java.util.logging.Logger;

public abstract class Handler {

    protected final Logger logger = Logger.getLogger(getClass().getName());

    public ResponseTransformer toJson() {
        return (object) -> new Gson().toJson(object);
    }

    public abstract void init();
}
