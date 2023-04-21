class ServerPath {
  static const String baseUrl = "http://10.0.2.2:8080";
  static const String base = "$baseUrl/api";

  // Used by UserRequestHandler
  static final Uri userBasePath = Uri.parse("$base/user");
  static final Uri registerPath = Uri.parse("$userBasePath/register");
  static final Uri loginPath = Uri.parse("$userBasePath/login");
  static final Uri isTokenValidPath = Uri.parse("$userBasePath/is-token-valid");

  // Used by TagRequestHandler
  static final Uri tagBasePath = Uri.parse("$base/tag");
  static final Uri saveTagPath = Uri.parse("$tagBasePath/save");
  static final Uri getTagPath = Uri.parse("$tagBasePath/get");
  static final Uri deleteTagPath = Uri.parse("$tagBasePath/delete");
  static final Uri getAllTagsPath = Uri.parse("$tagBasePath/all");
  static final Uri searchTagPath = Uri.parse("$tagBasePath/search");

  // Used by DeckRequestHandler
  static final Uri deckBasePath = Uri.parse("$base/deck");
  static final Uri saveDeckPath = Uri.parse("$deckBasePath/save");
  static final Uri deleteDeckPath = Uri.parse("$deckBasePath/delete");
  static final Uri getAllDecksPath = Uri.parse("$deckBasePath/all");
  static final Uri searchDecksPath = Uri.parse("$deckBasePath/search");
  static final Uri deckExistsPath = Uri.parse("$deckBasePath/exists");
  static final Uri saveDeckImagePath = Uri.parse("$deckBasePath/upload");
  static final Uri getDeckPath = Uri.parse("$deckBasePath/get");

  // Used by LeaderboardRequestHandler
  static final Uri leaderboardBasePath = Uri.parse("$base/leaderboard");
  static final Uri saveScorePath = Uri.parse("$leaderboardBasePath/save");
  static final Uri getLeaderboardPath = Uri.parse("$leaderboardBasePath/get");
  static final Uri getBestScorePath = Uri.parse("${getLeaderboardPath.path}/best-score");
  static final Uri getBestScoreUserIdPath = Uri.parse("$leaderboardBasePath/best-score-user");

  // Used by GameHistoryRequestHandler
  static final Uri gameHistoryBasePath = Uri.parse("$base/game-history");
  static final Uri gameHistoryPath = Uri.parse("$gameHistoryBasePath/get");

  ServerPath._();
}
