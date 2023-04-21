import 'package:flutter/material.dart';

class DeckView extends StatelessWidget {
  final String name;
  final String imageUrl;
  final int score;
  final int cardValue;
  final String deckColor;

  const DeckView(
      {super.key,
      required this.name,
      required this.imageUrl,
      required this.score,
      required this.cardValue,
      required this.deckColor});

  @override
  Widget build(BuildContext context) {
    return InkWell(
        onTap: () {
          // Navigator.of(context).push(
          //   MaterialPageRoute(
          //     builder: (context) => (),
          //   ),
          // );
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
                    image: NetworkImage(imageUrl),
                    fit: BoxFit.cover,
                  ),
                ),
                child: Align(
                  alignment: Alignment.bottomLeft,
                  child: Container(
                    padding: const EdgeInsets.all(8),
                    color: Colors.black.withOpacity(0.7),
                    child: Text(
                      name,
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
                color: Color(int.parse(deckColor)),
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
                          '$cardValue',
                          style: const TextStyle(color: Colors.white),
                        ),
                      ],
                    ),
                    Column(
                      mainAxisSize: MainAxisSize.min,
                      children: <Widget>[
                        const Icon(Icons.emoji_events, color: Colors.white),
                        const SizedBox(height: 4),
                        Text(
                          '$score',
                          style: const TextStyle(color: Colors.white),
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
