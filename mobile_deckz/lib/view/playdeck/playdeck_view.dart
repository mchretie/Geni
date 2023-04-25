import 'package:flutter/material.dart';
import 'package:mobile_deckz/http_dao/deck_dao.dart';
import 'package:mobile_deckz/model/card/abstract_card.dart';
import 'package:mobile_deckz/view/playdeck/card/flash_card_view.dart';
import 'package:mobile_deckz/view/playdeck/card/input_card_view.dart';
import 'package:mobile_deckz/view/playdeck/card/mcq_card_view.dart';

import '../../model/card/flash_card.dart';
import '../../model/card/input_card.dart';
import '../../model/card/mcq_card.dart';
import '../../model/deck/deck.dart';

class PlayDeckView extends StatelessWidget {
  final Deck deck;

  const PlayDeckView({super.key, required this.deck});

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<void>(
      future: DeckDao.loadDeck(deck),
      builder: (BuildContext context, AsyncSnapshot<void> snapshot) {
        if (snapshot.connectionState == ConnectionState.done) {
          return Scaffold(
            appBar: AppBar(
              title: Text(deck.name),
            ),
            body: Center(
                child: deck.cards.isEmpty
                    ? const Text('No cards in this deck loser')
                    : PlayCardView(cards: deck.getCardRandomOrder())),
          );
        } else {
          return const Center(child: CircularProgressIndicator());
        }
      },
    );
  }
}

class PlayCardView extends StatefulWidget {
  final List<AbstractCard> cards;

  const PlayCardView({super.key, required this.cards});

  @override
  State<PlayCardView> createState() => _PlayCardViewState();
}

class _PlayCardViewState extends State<PlayCardView> {
  int _currentCardIndex = 0;

  Widget getCorrectCardView(AbstractCard card) {
    switch (card.runtimeType) {
      case FlashCard:
        return FlashcardView(card: card as FlashCard);
      case MCQCard:
        return MCQCardView(card: card as MCQCard);
      case InputCard:
        return InputCardView(card: card as InputCard);
      default:
        return const Text('Error');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
    padding: const EdgeInsets.all(16.0),
        child: Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: <Widget>[
        getCorrectCardView(widget.cards[_currentCardIndex]),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceAround,
          children: [
            IconButton(
              icon: const Icon(Icons.arrow_back),
              onPressed: () {
                if (_currentCardIndex > 0) {
                  setState(() {
                    _currentCardIndex--;
                  });
                }
              },
            ),
            IconButton(
              icon: const Icon(Icons.arrow_forward),
              onPressed: () {
                if (_currentCardIndex < widget.cards.length - 1) {
                  setState(() {
                    _currentCardIndex++;
                  });
                }
              },
            ),
          ],
        )
      ],
    ));
  }
}
