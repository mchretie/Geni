import 'package:flutter_dotenv/flutter_dotenv.dart';

class ServerPath {
  static final String baseUrl = dotenv.env['SERVER_URL']!;
  static final String base = "$baseUrl/api";


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
  static final String getDeckPath = "$deckBasePath/get";

  // Used by LeaderboardRequestHandler
  static final Uri leaderboardBasePath = Uri.parse("$base/leaderboard");
  static final Uri saveScorePath = Uri.parse("$leaderboardBasePath/save");
  static final Uri getGlobalLeaderboard =
      Uri.parse("$leaderboardBasePath/global");
  static final Uri getBestScoresPath =
      Uri.parse("$leaderboardBasePath/best-scores");

  // Used by GameHistoryRequestHandler
  static final Uri gameHistoryBasePath = Uri.parse("$base/game-history");
  static final Uri gameHistoryPath = Uri.parse("$gameHistoryBasePath/get");

  // Used by MatchRequestHandler
  static final Uri marketplaceBasePath = Uri.parse("$base/marketplace");
  static final Uri getMarketplacePath = Uri.parse("$marketplaceBasePath/get");
  static final Uri addDeckToMarketplacePath =
      Uri.parse("$marketplaceBasePath/add");
  static final Uri removeDeckFromMarketplacePath =
      Uri.parse("$marketplaceBasePath/delete");
  static final Uri addDeckToCollectionPath =
      Uri.parse("$marketplaceBasePath/add-collection");
  static final Uri removeDeckFromCollectionPath =
      Uri.parse("$marketplaceBasePath/delete-collection");
  static final Uri getSavedDecksFromMarketplacePath =
      Uri.parse("$marketplaceBasePath/get-collection");

  ServerPath._();
}
