package ulb.infof307.g01.shared.constants;

public final class ServerPaths {
  public ServerPaths() {}

  // Used bby UserAccountHandler
  public static final String BASE_PATH = "/api/user";
  public static final String REGISTER_PATH = "/register";
  public static final String LOGIN_PATH = "/login";

  // Used by TagRequestHandler
  public static final String SAVE_TAG_PATH = "/api/tag/save";
  public static final String GET_TAG_PATH = "/api/tag/get";
  public static final String DELETE_TAG_PATH = "/api/tag/delete";
  public static final String GET_ALL_TAGS_PATH = "/api/tag/all";
  public static final String SEARCH_TAG_PATH = "/api/tag/search";

  // Used by DeckRequestHandler
  public static final String SAVE_DECK_PATH = "/save";
  public static final String DELETE_DECK_PATH = "/delete";
  public static final String GET_ALL_DECKS_PATH = "/all";
  public static final String SEARCH_DECKS_PATH = "/search";
  // ( already defined ) public static final  String BASE_PATH     =
  // "/api/user";
}
