import 'package:flutter/material.dart';
import 'package:mobile_deckz/http_dao/deck_dao.dart';
import 'package:mobile_deckz/model/card/abstract_card.dart';
import 'package:mobile_deckz/view/playdeck/card/flash_card_view.dart';
import 'package:mobile_deckz/view/playdeck/card/input_card_view.dart';
import 'package:mobile_deckz/view/playdeck/card/mcq_card_view.dart';
import 'package:mobile_deckz/view/playdeck/score_result_view.dart';

import '../../model/card/flash_card.dart';
import '../../model/card/input_card.dart';
import '../../model/card/mcq_card.dart';
import '../../model/deck/deck.dart';
import '../../model/deck/score.dart';

class PlayDeckView extends StatelessWidget {
  final Deck deck;
  final Score score;

  const PlayDeckView({super.key, required this.deck, required this.score});

  Widget getCorrectCardView(AbstractCard card) {
    switch (card.runtimeType) {
      case FlashCard:
        return FlashcardView(card: card as FlashCard);
      case MCQCard:
        return MCQCardView(card: card as MCQCard, score: score);
      case InputCard:
        return InputCardView(card: card as InputCard, score: score);
      default:
        return const Text('Error');
    }
  }

  List<Widget> loadCardViews(List<AbstractCard> cards) {
    return deck
        .getCardRandomOrder()
        .map((card) => getCorrectCardView(card))
        .toList();
  }

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
                    : PlayCardView(
                        cardViews: loadCardViews(deck.cards), score: score)),
          );
        } else {
          return const Center(child: CircularProgressIndicator());
        }
      },
    );
  }
}

class PlayCardView extends StatefulWidget {
  // final List<AbstractCard> cards;
  final List<Widget> cardViews;
  final Score score;

  const PlayCardView({super.key, required this.cardViews, required this.score});

  @override
  State<PlayCardView> createState() => _PlayCardViewState();
}

class _PlayCardViewState extends State<PlayCardView> {
  int _currentCardIndex = 0;
  bool _showResult = false;
  late PageController pageController;

  @override
  void initState() {
    super.initState();
    pageController =
        PageController(initialPage: 0, keepPage: true);
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: _showResult
              ? [ScoreResultView(finalScore: widget.score.score)]
              : [
                  Expanded(
                    child: PageView(
                      controller: pageController,
                      children: widget.cardViews,
                    ),
                  ),
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
                          pageController.previousPage(
                              duration: const Duration(milliseconds: 200),
                              curve: Curves.easeInOut);
                        },
                      ),
                      IconButton(
                        icon: const Icon(Icons.arrow_forward),
                        onPressed: () {
                          pageController.nextPage(
                              duration: const Duration(milliseconds: 200),
                              curve: Curves.easeInOut);
                          if (_currentCardIndex <  - 1) {
                            setState(() {
                              _currentCardIndex++;
                            });
                          } else {
                            setState(() {
                              _showResult = true;
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
