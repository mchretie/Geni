import 'dart:convert';

import 'package:http/http.dart' as http;
import 'package:mobile_deckz/http_dao/http_dao.dart';
import 'package:mobile_deckz/http_dao/server_path.dart';

import '../model/game_history/game_history.dart';

class GameHistoryDao {
  static Future<GameHistory> getGameHistory() async {

    http.Response response = await HttpDao.get(ServerPath.gameHistoryPath);
    if (response.statusCode == 200) {
      Map<String, dynamic> json = jsonDecode(response.body);
      return GameHistory.fromJson(json);
    }
    return GameHistory(games: []);
  }
}
