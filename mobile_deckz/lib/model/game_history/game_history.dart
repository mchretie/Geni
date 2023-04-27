import 'dart:core';

import 'package:mobile_deckz/model/game_history/game.dart';

class GameHistory {
  final List<Game> games;

  GameHistory({required this.games}) {
    games.sort((a, b) => a.timestamp.compareTo(b.timestamp));
  }

  factory GameHistory.fromJson(Map<String, dynamic> json) {
    List<Game> games = [];
    for (var game in json['games']) {
      Game gameTest = Game.fromJson(game);
      games.add(gameTest);
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
