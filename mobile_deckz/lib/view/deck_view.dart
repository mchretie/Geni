import 'package:flutter/material.dart';
import 'package:mobile_deckz/view/playdeck/playdeck_view.dart';

import '../http_dao/server_path.dart';
import '../model/deck/deck.dart';

class DeckView extends StatelessWidget {
  final Deck deck;

  const DeckView({super.key, required this.deck});

  @override
  Widget build(BuildContext context) {
    return InkWell(
        onTap: () {
          Navigator.of(context).push(
            MaterialPageRoute(
              builder: (context) => PlayDeckView(deck: deck),
            ),
          );
        },
        child: Card(
          elevation: 4,
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: <Widget>[
              Container(
                height: 175,
                decoration: BoxDecoration(
                  image: DecorationImage(
                    image: NetworkImage(deck.image.replaceAll(
                        'http://localhost:8080', ServerPath.baseUrl)),
                    fit: BoxFit.cover,
                  ),
                ),
                child: Align(
                  alignment: Alignment.bottomLeft,
                  child: Container(
                    padding: const EdgeInsets.all(8),
                    color: Colors.black.withOpacity(0.7),
                    child: Text(
                      deck.name,
                      style: const TextStyle(
                        color: Colors.white,
                        fontSize: 16,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
                ),
              ),
              Container(
                color: deck.color == '0x00000000'
                    ? Colors.blue
                    : Color(int.parse(deck.color)),
                padding: const EdgeInsets.symmetric(vertical: 8),
                child: Row(
                  mainAxisSize: MainAxisSize.min,
                  mainAxisAlignment: MainAxisAlignment.spaceAround,
                  children: <Widget>[
                    Column(
                      mainAxisSize: MainAxisSize.min,
                      children: <Widget>[
                        const Icon(Icons.credit_card, color: Colors.white),
                        const SizedBox(height: 4),
                        Text(
                          deck.cardCount,
                          style: const TextStyle(color: Colors.white),
                        ),
                      ],
                    ),
                    Column(
                      mainAxisSize: MainAxisSize.min,
                      children: const <Widget>[
                        Icon(Icons.emoji_events, color: Colors.white),
                        SizedBox(height: 4),
                        Text(
                          '-1',
                          style: TextStyle(color: Colors.white),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ],
          ),
        ));
  }
}
