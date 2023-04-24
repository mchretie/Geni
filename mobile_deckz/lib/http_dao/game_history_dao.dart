import 'dart:convert';

import 'package:http/http.dart' as http;
import 'package:mobile_deckz/http_dao/server_path.dart';

import '../model/game_history/game.dart';
import '../model/game_history/game_history.dart';

class GameHistoryDao {
  static Future<GameHistory> getGameHistory() async {
    http.Response response = await http.get(ServerPath.gameHistoryPath);
    print("response : " + response.body);
    if (response.statusCode == 200) {
      List<dynamic> json = jsonDecode(response.body);
      List<Game> games = [];
      for (var game in json) {
        games.add(Game.fromJson(game));
      }
      return GameHistory(games: games);
    }
    return GameHistory(games: []);
  }
}
