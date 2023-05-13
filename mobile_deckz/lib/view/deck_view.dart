// import 'package:flutter/material.dart';
// import 'package:mobile_deckz/model/deck/hex_color.dart';
// import 'package:mobile_deckz/view/playdeck/playdeck_view.dart';
//
// import '../http_dao/server_path.dart';
// import '../model/deck/deck.dart';
// import '../model/deck/score.dart';


import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:mobile_deckz/view/deck_view_template.dart';
import 'package:mobile_deckz/view/playdeck/playdeck_view.dart';

import '../model/deck/deck.dart';
import '../model/deck/score.dart';

class DeckView extends StatelessWidget {
  final Deck deck;

  const DeckView({super.key, required this.deck});

  @override
  Widget build(BuildContext context) {
    return DeckViewTemplate(deck: deck);
  }
}


//
// class DeckView extends StatelessWidget {
//   final Deck deck;
//
//   const DeckView({super.key, required this.deck});
//
//   @override
//   Widget build(BuildContext context) {
//     return InkWell(
//         onTap: () {
//           Navigator.of(context).push(
//             MaterialPageRoute(
//               builder: (context) => PlayDeckView(deck: deck, score: Score(0)),
//             ),
//           );
//         },
//         child: Card(
//           elevation: 4,
//           child: Column(
//             mainAxisSize: MainAxisSize.min,
//             crossAxisAlignment: CrossAxisAlignment.stretch,
//             children: <Widget>[
//               Container(
//                 height: 175,
//                 decoration: BoxDecoration(
//                   image: DecorationImage(
//                     image: NetworkImage(deck.image.replaceAll(
//                         'http://localhost:8080', ServerPath.baseUrl)),
//                     fit: BoxFit.cover,
//                   ),
//                 ),
//                 child: Align(
//                   alignment: Alignment.bottomLeft,
//                   child: Row(children: [
//                     Align(
//                         alignment: FractionalOffset.bottomCenter,
//                         child: Container(
//                           padding: const EdgeInsets.all(8),
//                           color: Colors.black.withOpacity(0.7),
//                           child: Text(
//                             deck.name,
//                             style: const TextStyle(
//                               color: Colors.white,
//                               fontSize: 20,
//                               fontWeight: FontWeight.bold,
//                             ),
//                           ),
//                         )),
//                     Expanded(
//                         child: Align(
//                             alignment: Alignment.bottomLeft,
//                             child: SizedBox(
//                                 height: 30,
//                                 child: ListView(
//                                     scrollDirection: Axis.horizontal,
//                                     children: [
//                                       for (var tag in deck.tags)
//                                         Container(
//                                           padding: const EdgeInsets.all(8),
//                                           color: HexColor(tag.color)
//                                               .withOpacity(0.9),
//                                           child: Text(
//                                             tag.name,
//                                             style: const TextStyle(
//                                               color: Colors.black,
//                                               fontSize: 12,
//                                               fontWeight: FontWeight.bold,
//                                             ),
//                                           ),
//                                         )
//                                     ])))),
//                   ]),
//                 ),
//               ),
//               Container(
//                 color: deck.color == '#00000000'
//                     ? Colors.purple
//                     : HexColor(deck.color),
//                 padding: const EdgeInsets.symmetric(vertical: 8),
//                 child: Row(
//                   mainAxisSize: MainAxisSize.min,
//                   mainAxisAlignment: MainAxisAlignment.spaceAround,
//                   children: <Widget>[
//                     Column(
//                       mainAxisSize: MainAxisSize.min,
//                       children: <Widget>[
//                         const Icon(Icons.credit_card, color: Colors.white),
//                         const SizedBox(height: 4),
//                         Text(
//                           deck.cardCount,
//                           style: const TextStyle(color: Colors.white),
//                         ),
//                       ],
//                     ),
//                     Column(
//                       mainAxisSize: MainAxisSize.min,
//                       children: <Widget>[
//                         const Icon(Icons.emoji_events, color: Colors.white),
//                         const SizedBox(height: 4),
//                         Text(
//                           deck.score,
//                           style: const TextStyle(color: Colors.white),
//                         ),
//                       ],
//                     ),
//                   ],
//                 ),
//               ),
//             ],
//           ),
//         ));
//   }
// }
