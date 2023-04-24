import 'dart:core';

class Game {

  final DateTime timestamp;
  final String deckName;
  final int score;

  Game({required this.timestamp, required this.deckName, required this.score});

  factory Game.fromJson(Map<String, dynamic> json) {
    return Game(
      timestamp: json['timestamp'],
      deckName: json['deckName'].toString(),
      score: json['score'],
    );
  }

  String getTimestamp() {
    return timestamp.toString();
  }

  String getDeckName() {
    return deckName;
  }

  int getScore() {
    return score;
  }
}