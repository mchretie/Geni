import 'package:flutter/material.dart';
import 'package:mobile_deckz/http_dao/deck_dao.dart';

import '../../model/deck/deck.dart';

class PlayDeckView extends StatelessWidget {
  final Deck deck;

  const PlayDeckView({super.key, required this.deck});

  @override
  Widget build(BuildContext context) {
    DeckDao.getDeck(deck.id);

    return Scaffold(
      appBar: AppBar(
        title: Text(deck.name),
      ),
      body: Center(
          child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          const Text(
            'PLAY CARD',
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            children: [
              IconButton(
                icon: const Icon(Icons.arrow_back),
                onPressed: () => Navigator.pop(context),
              ),
              IconButton(
                icon: const Icon(Icons.arrow_forward),
                onPressed: () => Navigator.pop(context),
              ),
            ],
          )
        ],
      )),
    );
  }
}
