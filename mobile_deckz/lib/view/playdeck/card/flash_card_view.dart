import 'dart:math';

import 'package:flutter/material.dart';
import 'package:mobile_deckz/model/card/flash_card.dart';

import 'front_card_view.dart';

class FlashcardView extends StatefulWidget {
  final FlashCard card;

  const FlashcardView({
    super.key,
    required this.card,
  });

  @override
  State<FlashcardView> createState() => _FlashcardViewState();
}

class _FlashcardViewState extends State<FlashcardView> {
  bool _showFront = true;
  double y = 0;
  bool _isFlippingFront = false;
  bool _isFlippingBack = false;

  slowFlipY() async {
    bool previousShowFront = _showFront;
    if (!previousShowFront) {
      setState(() {
        _isFlippingFront = true;
      });
    } else {
      setState(() {
        _isFlippingBack = true;
      });
    }
    while (y < 3.14) {
      await Future.delayed(const Duration(milliseconds: 10));
      setState(() {
        y += 0.10;
      });
      if (y > 1.55) {
        setState(() {
          _showFront = !previousShowFront;
        });
      }
    }
    setState(() {
      y = 0;
    });
    setState(() {
      _isFlippingFront = false;
      _isFlippingBack = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Transform(
        transform: Matrix4.identity()
          ..setEntry(3, 2, 0.003)
          ..rotateY(y),
        alignment: FractionalOffset.center,
        child: GestureDetector(
            onTap: () {
              slowFlipY();
            },
            child: _showFront
                ? Transform(
                    alignment: Alignment.center,
                    transform: Matrix4.rotationY(_isFlippingFront ? pi : 0),
                    child: FrontCardView(text: widget.card.front))
                : Transform(
                    alignment: Alignment.center,
                    transform: Matrix4.rotationY(_isFlippingBack ? pi : 0),
                    child: FrontCardView(text: widget.card.back))),
      ),
    );
  }
}