import 'package:flutter/material.dart';
import 'package:mobile_deckz/model/card/input_card.dart';

import 'front_card_view.dart';

class InputCardView extends StatelessWidget {
  final InputCard card;

  const InputCardView({super.key, required this.card});

  @override
  Widget build(BuildContext context) {
    return Column(children: [
      FrontCardView(text: card.front),
      Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          const SizedBox(
            width: 300,
              child: TextField(
            decoration: InputDecoration(
              border: OutlineInputBorder(),
              labelText: 'Answer',
            ),
          )),
          SizedBox(
            width: 60,
            height: 60,
            child: ElevatedButton(
              onPressed: () {},
              child: const Icon(Icons.check),
            ),
          )
        ],
      ),
    ]);
  }
}
