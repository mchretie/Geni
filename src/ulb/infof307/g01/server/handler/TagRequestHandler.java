package ulb.infof307.g01.server.handler;

import spark.Request;
import spark.Response;

import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import static spark.Spark.*;

public class TagRequestHandler extends Handler {

    @Override
    public void init() {
        logger.info("Starting tag handler");
        path("/api", () -> {
            path("/tag", () -> {
                post("/save", this::saveTag);
                get("/get", this::getTagFor);
                delete("/delete", this::deleteTag);
                get("/all", this::getAllTags);
                get("/search", this::searchTag);
            });
        });
        logger.info("Tag handler started");
    }

    private Map<String, String> saveTag(Request req, Response res) {
        return null;
    }

    private Map<String, String> getTagFor(Request req, Response res) {
        UUID deckId = UUID.fromString(req.queryParams("deck_id"));
        return null;
    }

    private Map<String, String> deleteTag(Request req, Response res) {
        UUID tagId = UUID.fromString(req.queryParams("tag_id"));
        return null;
    }


    private Map<String, String> getAllTags(Request req, Response res) {
        return null;
    }

    private Map<String, String> searchTag(Request req, Response res) {
        String userSearch = req.queryParams("user_search");
        return null;
    }
}
