import 'dart:convert';

import 'package:http/http.dart' as http;
import 'package:mobile_deckz/http_dao/auth_dao.dart';
import 'package:mobile_deckz/http_dao/server_path.dart';

import '../model/leaderboard/leaderboard.dart';
import '../model/leaderboard/score.dart';

class LeaderboardDao {
  static Future<Leaderboard> getGlobalLeaderboard() async {
    http.Response response = await http.get(ServerPath.getGlobalLeaderboard);
    String username = await AuthDao.getUsername();
    if (response.statusCode == 200) {
      Map<String, dynamic> json = jsonDecode(response.body);
      return Leaderboard.fromJson(json, username);
    }
    return Leaderboard(
        leaderboard: [],
        userScore: Score(username: username, score: 'N/A', rank: 'N/A'));
  }
}
