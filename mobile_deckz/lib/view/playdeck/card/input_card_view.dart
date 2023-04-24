import 'package:flutter/material.dart';
import 'package:mobile_deckz/model/card/input_card.dart';

import 'front_card_view.dart';

class InputCardView extends StatelessWidget {
  final InputCard card;

  const InputCardView({super.key, required this.card});

  @override
  Widget build(BuildContext context) {
    return FrontCardView(text: card.front);
  }
}
