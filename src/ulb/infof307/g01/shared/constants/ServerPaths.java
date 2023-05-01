package ulb.infof307.g01.shared.constants;

/**
 * This class contains all the paths used by the server.
 */
public final class ServerPaths {

    private ServerPaths() {}

    public static final String BASE_URL = "http://localhost:8080";

    private static final String BASE = "/api";

    // Used by UserRequestHandler
    public static final String USER_BASE_PATH = BASE + "/user";
    public static final String REGISTER_PATH = USER_BASE_PATH + "/register";
    public static final String LOGIN_PATH = USER_BASE_PATH + "/login";
    public static final String IS_TOKEN_VALID_PATH = USER_BASE_PATH + "/is-token-valid";

    // Used by DeckRequestHandler
    public static final String DECK_BASE_PATH = BASE + "/deck";
    public static final String GET_DECK_PATH = DECK_BASE_PATH + "/get";
    public static final String SAVE_DECK_PATH = DECK_BASE_PATH + "/save";
    public static final String DELETE_DECK_PATH = DECK_BASE_PATH + "/delete";
    public static final String GET_ALL_DECKS_PATH = DECK_BASE_PATH + "/all";
    public static final String SEARCH_DECKS_PATH = DECK_BASE_PATH + "/search";
    public static final String DECK_EXISTS_PATH = DECK_BASE_PATH + "/exists";
    public static final String SAVE_DECK_IMAGE_PATH = DECK_BASE_PATH + "/upload";
    public static final String NUMBER_OF_PUBLIC_PLAYED_DECKS_PATH = DECK_BASE_PATH + "/public-played";

    // Used by LeaderboardRequestHandler
    public static final String LEADERBOARD_BASE_PATH = BASE + "/leaderboard";
    public static final String SAVE_SCORE_PATH = LEADERBOARD_BASE_PATH + "/save";
    public static final String GET_LEADERBOARD_PATH = LEADERBOARD_BASE_PATH + "/get";
    public static final String GET_BEST_SCORE_PATH = GET_LEADERBOARD_PATH + "/best-score";
    public static final String GET_GLOBAL_LEADERBOARD = LEADERBOARD_BASE_PATH + "/global";

    // Used by GameHistoryRequestHandler
    public static final String GAME_HISTORY_BASE_PATH = BASE + "/game-history";
    public static final String GAME_HISTORY_PATH = GAME_HISTORY_BASE_PATH + "/get";
    public static final String SPECIFIC_GAME_HISTORY_PATH = GAME_HISTORY_PATH + "/deck";

    // Used by MarketplaceHandler
    public static final String MARKETPLACE_BASE_PATH = BASE + "/marketplace";
    public static final String GET_MARKETPLACE_PATH = MARKETPLACE_BASE_PATH + "/get";
    public static final String ADD_DECK_TO_MARKETPLACE_PATH = MARKETPLACE_BASE_PATH + "/add";
    public static final String REMOVE_DECK_FROM_MARKETPLACE_PATH = MARKETPLACE_BASE_PATH + "/delete";
    public static final String ADD_DECK_TO_COLLECTION_PATH = MARKETPLACE_BASE_PATH + "/add-collection";
    public static final String REMOVE_DECK_FROM_COLLECTION_PATH = MARKETPLACE_BASE_PATH + "/delete-collection";
    public static final String GET_SAVED_DECKS_FROM_MARKETPLACE = MARKETPLACE_BASE_PATH + "/get-collection";
}
