import 'package:flutter/material.dart';

import 'package:mobile_deckz/http_dao/game_history_dao.dart';

import '../model/game_history/game_history.dart';
import '../model/game_history/game.dart';

class GameHistoryView extends StatefulWidget {
  const GameHistoryView({super.key});

  @override
  State<GameHistoryView> createState() => _GameHistoryViewState();
}

class _GameHistoryViewState extends State<GameHistoryView> {
  @override
  Widget build(BuildContext context) {
    final Future<GameHistory> gameHistory =
        GameHistoryDao.getGameHistory();

    return FutureBuilder<GameHistory>(
        future: gameHistory,
        builder: (BuildContext context, AsyncSnapshot<GameHistory> snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return const Text(
                'An error occurred while loading the game history',
                style: TextStyle(color: Colors.red));
          } else {
            List<Game> games = snapshot.data?.games ?? [];
            return Scaffold(
                body: SingleChildScrollView(
                  scrollDirection: Axis.vertical,
                  child: DataTable(
                    columns: const <DataColumn>[
                      DataColumn(
                        label: Expanded(
                          child: Text(
                            'Date',
                          ),
                        ),
                      ),
                      DataColumn(
                        label: Expanded(
                          child: Text(
                            'Paquet',
                          ),
                        ),
                      ),
                      DataColumn(
                        numeric: true,
                        label: Expanded(
                          child: Text(
                            'Score',
                          ),
                        ),
                      ),
                    ],
                    rows: games
                        .map(
                          (game) =>
                          DataRow(cells: [
                            DataCell(Text(game.getTimestamp())),
                            DataCell(Text(game.getDeckName())),
                            DataCell(Text(game.getScore().toString())),

                          ]),
                    )
                        .toList(),
                  ),
                )
            );
          }
        }
    );
  }
}
