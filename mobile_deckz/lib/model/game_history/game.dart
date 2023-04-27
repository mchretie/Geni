import 'dart:core';
import 'package:intl/intl.dart';


class Game {

  DateTime dateTime;
  String deckName;
  int score;

  Game({required this.dateTime, required this.deckName, required this.score});

  factory Game.fromJson(Map<String, dynamic> json) {
    return Game(
      dateTime : DateTime.fromMillisecondsSinceEpoch(json['timestamp']),
      deckName: json['deckName'].toString(),
      score: int.parse(json['score']),
    );

  }

  String getFormattedTimestamp() {
    return DateFormat('dd MMMM yyyy').format(dateTime);
  }

  String getDeckName() {
    return deckName;
  }

  int getScore() {
    return score;
  }
}