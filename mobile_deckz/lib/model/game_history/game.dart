import 'dart:core';
import 'package:mobile_deckz/utils/timestamp_to_datetime_convertor.dart';

class Game {

  DateTime timestamp;
  String deckName;
  int score;

  Game({required this.timestamp, required this.deckName, required this.score});

  factory Game.fromJson(Map<String, dynamic> json) {
    return Game(
      timestamp : timestampToDateTime(json['timestamp']),
      deckName: json['deckName'].toString(),
      score: int.parse(json['score']),
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