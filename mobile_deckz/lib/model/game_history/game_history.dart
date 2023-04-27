import 'dart:core';

import 'package:mobile_deckz/model/game_history/game.dart';

class GameHistory {
  final List<Game> games;

  GameHistory({required this.games}) {
    games.sort((a, b) => b.timestamp.compareTo(a.timestamp));
  }

  factory GameHistory.fromJson(Map<String, dynamic> json) {
    List<Game> games = [];
    for (var game in json['games']) {
      games.add(Game.fromJson(game));
    }
    return GameHistory(games: games);
  }

  int getNumberOfGames() {
    return games.length;
  }

  int totalScore() {
    return games.map((game) => game.getScore()).reduce((a, b) => a + b);

  }
}
