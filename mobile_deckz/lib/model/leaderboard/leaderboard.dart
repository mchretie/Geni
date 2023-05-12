import 'package:mobile_deckz/model/leaderboard/score.dart';

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
