package ulb.infof307.g01.server.handler;

import com.google.gson.Gson;
import spark.ResponseTransformer;

public abstract class Handler {

    public String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public ResponseTransformer toJson() {
        return this::toJson;
    }

    public abstract void init();
}
