import 'dart:core';

class Game {

  final DateTime timestamp;
  final String deckName;
  final String score;

  Game({required this.timestamp, required this.deckName, required this.score});

  factory Game.fromJson(Map<String, dynamic> json) {
    return Game(
      timestamp: json['timestamp'],
      deckName: json['deckName'].toString(),
      score: json['score'].toString(),
    );
  }

  String getFormattedTimestamp() {
    return timestamp.toString();
  }

  int getScore() {
    return int.parse(score);
  }

  int compareTo(Game o) {
    return o.timestamp.compareTo(timestamp);
  }
}