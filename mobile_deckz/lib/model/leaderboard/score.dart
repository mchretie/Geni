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
