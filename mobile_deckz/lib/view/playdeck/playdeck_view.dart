import 'package:flutter/material.dart';
import 'package:mobile_deckz/http_dao/deck_dao.dart';
import 'package:mobile_deckz/http_dao/score_dao.dart';
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

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<void>(
      future: DeckDao.loadDeck(deck),
      builder: (BuildContext context, AsyncSnapshot<void> snapshot) {
        if (snapshot.connectionState == ConnectionState.done) {
          return WillPopScope(
              onWillPop: () async {
                if (score.isFinal) {
                  return true;
                }
                return await showDialog(
                      context: context,
                      builder: (BuildContext context) => AlertDialog(
                        title: const Text('Are you sure you wish to exit?'),
                        content:
                            const Text('Your progress and score will be lost'),
                        actions: <Widget>[
                          TextButton(
                            onPressed: () {
                              Navigator.of(context).pop(false);
                            },
                            child: const Text('Cancel'),
                          ),
                          TextButton(
                            onPressed: () {
                              Navigator.of(context).pop(true);
                            },
                            child: const Text('Exit'),
                          ),
                        ],
                      ),
                    ) ??
                    false;
              },
              child: Scaffold(
                appBar: AppBar(
                  title: Text(deck.name),
                ),
                body: Center(
                    child: deck.cards.isEmpty
                        ? const Text('No cards in this deck loser')
                        : PlayCardView(deck: deck, score: score)),
              ));
        } else {
          return const Center(child: CircularProgressIndicator());
        }
      },
    );
  }
}

class PlayCardView extends StatefulWidget {
  final Deck deck;
  final Score score;

  const PlayCardView({super.key, required this.deck, required this.score});

  @override
  State<PlayCardView> createState() => _PlayCardViewState();
}

class _PlayCardViewState extends State<PlayCardView> {
  late List<Widget> cardViews;
  late PageController pageController;
  late ScrollPhysics physics;
  late bool switchPageAllow;

  Widget getCorrectCardView(AbstractCard card) {
    switch (card.runtimeType) {
      case FlashCard:
        return FlashcardView(card: card as FlashCard);
      case MCQCard:
        return MCQCardView(
            card: card as MCQCard,
            score: widget.score,
            onCardAnswered: onCardAnswered,
            onCardLoaded: onCardLoaded);
      case InputCard:
        return InputCardView(
            card: card as InputCard,
            score: widget.score,
            onCardAnswered: onCardAnswered,
            onCardLoaded: onCardLoaded);
      default:
        return const Text('Error');
    }
  }

  List<Widget> loadCardViews(List<AbstractCard> cards) {
    return widget.deck
        .getCardRandomOrder()
        .map((card) => getCorrectCardView(card))
        .toList();
  }

  void setSwitchPageAllow(bool allow) {
    if (allow) {
      setState(() {
        physics = const PageScrollPhysics();
        switchPageAllow = true;
      });
    } else {
      setState(() {
        physics = const NeverScrollableScrollPhysics();
        switchPageAllow = false;
      });
    }
  }

  void onCardAnswered() {
    setSwitchPageAllow(true);
  }

  void onCardLoaded() {
    setSwitchPageAllow(false);
  }

  @override
  void initState() {
    super.initState();
    cardViews = loadCardViews(widget.deck.cards);
    cardViews.add(ScoreResultView(score: widget.score));
    pageController = PageController(initialPage: 0, keepPage: true);
    setSwitchPageAllow(true);
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Expanded(
                child: PageView.builder(
              controller: pageController,
              itemCount: cardViews.length,
              physics: physics,
              onPageChanged: (int index) {
                if (index == cardViews.length - 1) {
                  widget.score.setFinal();
                  ScoreDAO.addScore(widget.score);
                }
              },
              itemBuilder: (BuildContext context, int index) =>
                  cardViews[index],
            )),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: switchPageAllow
                  ? [
                      IconButton(
                        icon: const Icon(Icons.arrow_back),
                        onPressed: () {
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
                        },
                      ),
                    ]
                  : [],
            )
          ],
        ));
  }
}
