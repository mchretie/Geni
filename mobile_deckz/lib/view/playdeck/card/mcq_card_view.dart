import 'package:flutter/material.dart';
import 'package:mobile_deckz/model/card/mcq_card.dart';
import 'package:mobile_deckz/view/playdeck/card/front_card_view.dart';

class MCQCardView extends StatelessWidget {
  final MCQCard card;

  const MCQCardView({super.key, required this.card});

  @override
  Widget build(BuildContext context) {
    return FrontCardView(text: card.front);
  }
}