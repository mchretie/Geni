package ulb.infof307.g01.server.handler;

import com.google.gson.Gson;
import spark.ResponseTransformer;

import java.util.logging.Logger;

public abstract class Handler {

    protected final Logger logger = Logger.getLogger(getClass().getName());

    public String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public ResponseTransformer toJson() {
        return this::toJson;
    }

    public abstract void init();
}
