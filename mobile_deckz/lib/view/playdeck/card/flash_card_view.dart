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

  @override
  Widget build(BuildContext context) {
    return AnimatedSwitcher(
      duration: const Duration(seconds: 1),
      child: Container(
          key: ValueKey<bool>(_showFront),
          child: GestureDetector(
            onTap: () {
              setState(() {
                _showFront = !_showFront;
              });
            },
            child: _showFront
                ? FrontCardView(text: widget.card.front)
                : FrontCardView(text: widget.card.back),
          )),
      transitionBuilder: (child, animation) {
        return RotationYTransition(
          animation: animation,
          child: child,
        );
        // final rotateAnim = Tween(begin: pi, end: 0.0).animate(animation);
        // return AnimatedBuilder(
        //   animation: rotateAnim,
        //   child: widget,
        //   builder: (context, widget) {
        //     // final isUnder = ValueKey<bool>(_showFront);
        //     final value = _showFront ? min(rotateAnim.value, pi / 2) : rotateAnim.value;
        //     return Transform(
        //       transform: Matrix4.rotationY(value),
        //       alignment: Alignment.center,
        //       child: widget,
        //     );
        //   },
        // );
      },
    );
  }
}

class RotationYTransition extends AnimatedWidget {
  final Widget child;

  const RotationYTransition({
    super.key,
    required this.child,
    required Animation<double> animation,
  }) : super(listenable: animation);

  @override
  Widget build(BuildContext context) {
    final animation = listenable as Animation<double>;

    return Transform(
      alignment: Alignment.center,
      transform: Matrix4.rotationY(animation.value),
      // transform: Matrix4.identity()
      //   ..setEntry(3, 2, 0.001) // perspective
      //   ..rotateY(animation.value),
      child: child,
    );
  }
}
