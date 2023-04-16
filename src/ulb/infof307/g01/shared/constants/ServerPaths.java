package ulb.infof307.g01.shared.constants;

/**
 * This class contains all the paths used by the server.
 */
public final class ServerPaths {

  private ServerPaths() {}

  private static final String BASE = "/api";

  // Used by UserRequestHandler
  public static final String USER_BASE_PATH = BASE + "/user";
  public static final String REGISTER_PATH = USER_BASE_PATH + "/register";
  public static final String LOGIN_PATH = USER_BASE_PATH + "/login";

  // Used by TagRequestHandler
  private static final String TAG_BASE_PATH = BASE + "/tag";
  public static final String SAVE_TAG_PATH = TAG_BASE_PATH + "/save";
  public static final String GET_TAG_PATH = TAG_BASE_PATH + "/get";
  public static final String DELETE_TAG_PATH = TAG_BASE_PATH + "/delete";
  public static final String GET_ALL_TAGS_PATH = TAG_BASE_PATH + "/all";
  public static final String SEARCH_TAG_PATH = TAG_BASE_PATH + "/search";

  // Used by DeckRequestHandler
  public static final String DECK_BASE_PATH = BASE + "/deck";
  public static final String SAVE_DECK_PATH = DECK_BASE_PATH +  "/save";
  public static final String DELETE_DECK_PATH = DECK_BASE_PATH + "/delete";
  public static final String GET_ALL_DECKS_PATH = DECK_BASE_PATH + "/all";
  public static final String SEARCH_DECKS_PATH = DECK_BASE_PATH + "/search";
  public static final String DECK_EXISTS_PATH = DECK_BASE_PATH + "/exists";
  public static final String SAVE_DECK_IMAGE_PATH = DECK_BASE_PATH +  "/upload";

  public static final String GET_DECK_PATH = DECK_BASE_PATH + "/get";

  // Used by LeaderboardRequestHandler
  public static final String LEADERBOARD_BASE_PATH = BASE + "/leaderboard";
  public static final String SAVE_SCORE_PATH = LEADERBOARD_BASE_PATH + "/save";
  public static final String GET_LEADERBOARD_PATH = LEADERBOARD_BASE_PATH + "/get";
  public static final String GET_BEST_SCORE_PATH = GET_LEADERBOARD_PATH + "/best-score";
  public static final String GET_BEST_SCORE_USER_ID_PATH = LEADERBOARD_BASE_PATH + "/best-score-user";
}
