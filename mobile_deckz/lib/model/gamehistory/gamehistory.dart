import 'dart:core';

import 'package:mobile_deckz/model/gamehistory/game.dart';

class GameHistory extends Iterable<Game> {
  final List<Game> games;

  GameHistory({required this.games}) {
    games.sort();
  }

  factory GameHistory.fromJson(Map<String, dynamic> json, String username) {
    List<Game> games = [];
    for (var json in json['games']) {
      games.add(Game.fromJson(json));
    }
    return GameHistory(games: games);
  }

  int getNumberOfGames() {
    return games.length;
  }

  int totalScore() {
    return games.map((game) => game.getScore()).reduce((a, b) => a + b);

  }

  @override
  // TODO: implement iterator
  Iterator<Game> get iterator => throw UnimplementedError();
}
