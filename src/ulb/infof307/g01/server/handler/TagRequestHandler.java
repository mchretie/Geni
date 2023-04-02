package ulb.infof307.g01.server.handler;

import spark.Request;
import spark.Response;

import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import static spark.Spark.*;

@SuppressWarnings("FieldCanBeLocal")
public class TagRequestHandler extends Handler {

    private final String SAVE_TAG_PATH = "/api/tag/save";
    private final String GET_TAG_PATH = "/api/tag/get";
    private final String DELETE_TAG_PATH = "/api/tag/delete";
    private final String GET_ALL_TAGS_PATH = "/api/tag/all";
    private final String SEARCH_TAG_PATH = "/api/tag/search";

    @Override
    public void init() {
        post(SAVE_TAG_PATH, this::saveTag, toJson());
        get(GET_TAG_PATH, this::getTagFor, toJson());
        delete(DELETE_TAG_PATH, this::deleteTag, toJson());
        get(GET_ALL_TAGS_PATH, this::getAllTags, toJson());
        get(SEARCH_TAG_PATH, this::searchTag, toJson());
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
