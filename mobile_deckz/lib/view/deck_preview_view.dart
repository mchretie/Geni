import 'package:flutter/material.dart';

import '../model/deck/deck.dart';
import '../model/deck/score.dart';
import '../http_dao/game_history_dao.dart';

import '../model/game_history/game.dart';
import '../model/game_history/game_history.dart';
import 'playdeck/playdeck_view.dart';

class DeckPreviewView extends StatelessWidget {
  final Deck deck;

  const DeckPreviewView({
    super.key,
    required this.deck,
  });

  void playButtonPressed(BuildContext context) {
    Navigator.of(context).pushReplacement(
      MaterialPageRoute(
        builder: (context) =>
            PlayDeckView(deck: deck, score: Score(0, deck.id)),
      ),
    );
  }

  Widget buildHistory() {
    return FutureBuilder<GameHistory>(
      future: GameHistoryDao.getGameHistory(),
      builder: (context, snapshot) {
        if (snapshot.hasData) {
          final List<Game>? games = snapshot.data?.games
              .where(
                (element) => element.getDeckName() == deck.name,
              )
              .toList();
          return ListView.builder(
            padding: const EdgeInsets.all(8),
            itemCount: games?.length,
            itemBuilder: (BuildContext context, int index) {
              return SizedBox(
                height: 20,
                child: Center(
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      Text(games![index].getFormattedTimestamp()),
                      SizedBox(width: 20),
                      Text(games[index].getScore().toString()),
                      Text(' Pts'),
                    ],
                  ),
                ),
              );
            },
          );
        } else {
          return const CircularProgressIndicator();
        }
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          const SizedBox(height: 20),
          Text(
            deck.name,
            style: const TextStyle(
              fontWeight: FontWeight.bold,
              fontSize: 26,
            ),
          ),
          const SizedBox(height: 20),
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const Text('Nombre de cartes : '),
              Text(
                deck.cardCount,
                style: const TextStyle(fontWeight: FontWeight.bold),
              ),
            ],
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const Text('Highscore : '),
              Text(
                deck.score,
                style: const TextStyle(fontWeight: FontWeight.bold),
              ),
              const Text(
                ' Pts',
                style: TextStyle(fontWeight: FontWeight.bold),
              ),
            ],
          ),
          const SizedBox(height: 20),
          const Text(
            'Historique de jeu',
            style: TextStyle(fontSize: 20),
          ),
          Expanded(
            child: buildHistory(),
          ),
          const SizedBox(height: 20),
          ElevatedButton(
            child: Wrap(
              children: [
                const Icon(Icons.play_arrow),
                const SizedBox(
                  width: 10,
                ),
                const Text(
                  'Jouer',
                  style: TextStyle(
                    fontSize: 20,
                  ),
                ),
              ],
            ),
            onPressed: () => playButtonPressed(context),
          ),
          const SizedBox(height: 20),
        ],
      ),
    );
  }
}
