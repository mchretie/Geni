import 'dart:convert';

import 'package:http/http.dart' as http;
import 'package:mobile_deckz/http_dao/auth_dao.dart';
import 'package:mobile_deckz/http_dao/server_path.dart';

class Score {
  final String username;
  final String score;
  final String rank;

  Score({required this.username, required this.score, required this.rank});

  factory Score.fromJson(Map<String, dynamic> json) {
    return Score(
      username: json['username'],
      score: json['totalScore'].toString(),
      rank: json['rank'].toString(),
    );
  }
}

class Leaderboard {
  final List<Score> leaderboard;
  final Score userScore;

  Leaderboard({required this.leaderboard, required this.userScore});

  List<Score> getLeaderboard() {
    return leaderboard;
  }

  Score getUserScore() {
    return userScore;
  }

  factory Leaderboard.fromJson(Map<String, dynamic> json, String username) {
    List<Score> leaderboard = [];
    for (var json in json['leaderboardEntries']) {
      leaderboard.add(Score.fromJson(json));
    }
    return Leaderboard(
      leaderboard: leaderboard,
      userScore: Score(
        username: username,
        score: json['leaderboard'][username]['totalScore'].toString(),
        rank: json['leaderboard'][username]['rank'].toString(),
      ),
    );
  }
}

class LeaderboardDao {
  static Future<Leaderboard> getGlobalLeaderboard() async {
    http.Response response = await http.get(ServerPath.getBestScoreUserIdPath);
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
