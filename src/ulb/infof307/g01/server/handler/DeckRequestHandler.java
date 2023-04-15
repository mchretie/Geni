package ulb.infof307.g01.server.handler;

import static spark.Spark.*;
import static ulb.infof307.g01.shared.constants.ServerPaths.*;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.gson.JsonObject;
import spark.Request;
import spark.Response;
import ulb.infof307.g01.model.Deck;
import ulb.infof307.g01.model.DeckMetadata;
import ulb.infof307.g01.server.database.Database;
import ulb.infof307.g01.server.service.JWTService;


public class DeckRequestHandler extends Handler {

  private final Database database;
  private final JWTService jwtService;

  public DeckRequestHandler(JWTService jwtService, Database database) {
    this.jwtService = jwtService;
    this.database = database;
  }

  @Override
  public void init() {
      post(SAVE_DECK_PATH, this::saveDeck, toJson());
      delete(DELETE_DECK_PATH, this::deleteDeck, toJson());
      get(GET_ALL_DECKS_PATH, this::getAllDecks, toJson());
      get(SEARCH_DECKS_PATH, this::searchDecks, toJson());
      get(GET_DECK_PATH, this::getDeck, toJson());
      get(DECK_EXISTS_PATH, this::deckExists, toJson());
  }

  private boolean deckExists(Request request, Response response) {
      try {
          String username = usernameFromRequest(request);
          UUID userId = UUID.fromString(database.getUserId(username));

          String deckName = request.queryParams("name");
          deckName = deckName.replace("_", " ");

          return database.deckNameExists(deckName, userId);

      } catch (Exception e) {
          String message = "Failed to check if deck exists: " + e.getMessage();
          logger.warning(message);
          halt(500, message);

          return false;
      }
  }

  private Map<String, Boolean> saveDeck(Request req, Response res) {
    try {
      String username = usernameFromRequest(req);
      UUID userId = UUID.fromString(database.getUserId(username));

      Deck deck = new Deck(new Gson().fromJson(req.body(), JsonObject.class));

      database.saveDeck(deck, userId);
      return successfulResponse;

    } catch (Exception e) {
      String message = "Failed to save deck: " + e.getMessage();
      logger.warning(message);
      halt(500, message);

      return failedResponse;
    }
  }

  private Map<String, Boolean> deleteDeck(Request req, Response res) {
    try {
      String username = usernameFromRequest(req);
      UUID userId = UUID.fromString(database.getUserId(username));

      UUID deckId = UUID.fromString(req.queryParams("deck_id"));

      database.deleteDeck(deckId, userId);
      return successfulResponse;

    } catch (Exception e) {
      String message = "Failed to delete deck: " + e.getMessage();
      logger.warning(message);
      halt(500, message);

      return failedResponse;
    }
  }

  private Deck getDeck(Request req, Response res) {
      String username = usernameFromRequest(req);
      UUID userId = UUID.fromString(database.getUserId(username));
      UUID deckId = UUID.fromString(req.queryParams("deck_id"));
      return database.getDeck(deckId, userId);
  }

  private List<DeckMetadata> getAllDecks(Request req, Response res) {
    try {
      String username = usernameFromRequest(req);
      UUID userId = UUID.fromString(database.getUserId(username));

      return database.getAllUserDecksMetadata(userId);

    } catch (Exception e) {
      String message = "Failed to get all decks: " + e.getMessage();
      logger.warning(message);
      halt(500, message);

      return new ArrayList<>();
    }
  }

  private List<DeckMetadata> searchDecks(Request req, Response res) {
    try {
      String username = usernameFromRequest(req);
      UUID userId = UUID.fromString(database.getUserId(username));

      String userSearch = req.queryParams("name");
      userSearch = userSearch.replace("_", " ");

      return database.searchDecksMetadata(userSearch, userId);

    } catch (Exception e) {
      String message = "Failed to search decks: " + e.getMessage();
      logger.warning(message);
      halt(500, message);

      return new ArrayList<>();
    }
  }

  /**
   * Extracts the username from the request's Authorization header.
   * <p>
   *     If the token is invalid, the request is halted.
   *     Otherwise, the username is returned.
   * </p>
   *
   * @param req the request
   * @return the username
   */
  private String usernameFromRequest(Request req) {
    String token = req.headers("Authorization");
    if (token == null || !jwtService.isTokenValid(token)) {
      halt(401, "Token is " + (token == null ? "null" : "not valid"));
    }
    return jwtService.getUsernameFromToken(token);
  }
}
