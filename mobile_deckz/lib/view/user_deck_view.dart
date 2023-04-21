import 'package:flutter/material.dart';

import '../http_dao/deck_dao.dart';
import 'deck_view.dart';

class UserDeckView extends StatefulWidget {
  const UserDeckView({super.key});

  @override
  State<UserDeckView> createState() => _UserDeckViewState();
}

class _UserDeckViewState extends State<UserDeckView> {
  @override
  Widget build(BuildContext context) {
    final Future<List<Deck>> decks = DeckDao.getAllDecks();

    return FutureBuilder<List<Deck>>(
        future: decks,
        builder: (BuildContext context, AsyncSnapshot<List<Deck>> snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Text('Error: ${snapshot.error}');
          } else {
            List<Deck> deckList = snapshot.data ?? [];
            return Scaffold(
                body: Center(
                    child: ListView.builder(
                        itemCount: deckList.length,
                        itemBuilder: (context, index) {
                          final Deck deck = deckList[index];
                          return Padding(
                              padding: const EdgeInsets.all(4.0),
                              child: DeckView(deck: deck));
                        })));
          }
        });
  }
// Scaffold(
//   body: Center(
//       child: Column(mainAxisSize: MainAxisSize.min, children: [
// Expanded(
//     child: ListView.builder(
//         itemCount: 10,
//         itemBuilder: (context, index) {
//           return const Padding(
//               padding: EdgeInsets.all(4.0),
//               child: DeckView(
//                 name: 'Test Deck',
//                 imageUrl:
//                     '${ServerPath.baseUrl}/backgrounds/default_background.jpg',
//                 score: 4,
//                 cardValue: 10,
//                 deckColor: '0xff123456',
//               ));
//         }))
// ])));
// }
}
