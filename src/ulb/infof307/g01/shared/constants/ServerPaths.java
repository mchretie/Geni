package ulb.infof307.g01.shared.constants;

/**
 * This class contains all the paths used by the server.
 */
public final class ServerPaths {

  private ServerPaths() {}

  private static final String BASE = "/api";

  // Used by UserRequestHandler
  public static final String USER_BASE_PATH = BASE + "/user";
  public static final String REGISTER_PATH = "/register";
  public static final String LOGIN_PATH = "/login";

  // Used by TagRequestHandler
  private static final String TAG = "/tag";
  public static final String SAVE_TAG_PATH = BASE + TAG + "/save";
  public static final String GET_TAG_PATH = BASE + TAG + "/get";
  public static final String DELETE_TAG_PATH = BASE + TAG + "/delete";
  public static final String GET_ALL_TAGS_PATH = BASE + TAG + "/all";
  public static final String SEARCH_TAG_PATH = BASE + TAG + "/search";

  // Used by DeckRequestHandler
  public static final String DECK_BASE_PATH = BASE + "/deck";
  public static final String SAVE_DECK_PATH = "/save";
  public static final String DELETE_DECK_PATH = "/delete";
  public static final String GET_ALL_DECKS_PATH = "/all";
  public static final String SEARCH_DECKS_PATH = "/search";
}
