import 'package:flutter/material.dart';
import 'package:mobile_deckz/http_dao/server_path.dart';

import 'deck_view.dart';

class UserDeckView extends StatefulWidget {
  const UserDeckView({super.key});

  @override
  State<UserDeckView> createState() => _UserDeckViewState();
}

class _UserDeckViewState extends State<UserDeckView> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: Center(
            child: Column(mainAxisSize: MainAxisSize.min, children: [
      Expanded(
          child: ListView.builder(
              itemCount: 10,
              itemBuilder: (context, index) {
                return const Padding(
                    padding: EdgeInsets.all(4.0),
                    child: DeckView(
                      name: 'Test Deck',
                      imageUrl:
                          '${ServerPath.baseUrl}/backgrounds/default_background.jpg',
                      score: 4,
                      cardValue: 10,
                      deckColor: '0xff123456',
                    ));
              }))
    ])));
  }
}
