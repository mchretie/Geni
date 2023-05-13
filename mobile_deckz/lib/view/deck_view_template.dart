import 'package:flutter/material.dart';
import 'package:mobile_deckz/model/deck/hex_color.dart';
import 'package:mobile_deckz/model/deck/marketplace_deck.dart';
import 'package:mobile_deckz/view/playdeck/playdeck_view.dart';

import '../../http_dao/deck_dao.dart';
import '../../http_dao/server_path.dart';
import '../model/deck/deck.dart';
import '../model/deck/score.dart';

class DeckViewTemplate extends StatefulWidget {
  final Deck deck;

  const DeckViewTemplate({super.key, required this.deck});

  @override
  State<DeckViewTemplate> createState() => _DeckViewTemplateState();
}

class _DeckViewTemplateState extends State<DeckViewTemplate> {
  bool _deckDownloaded = false;
  bool _isMarketplaceDeck = false;
  late MarketplaceDeck _marketplaceDeck;

  @override
  void initState() {
    super.initState();
    if (widget.deck.runtimeType == MarketplaceDeck) {
      _isMarketplaceDeck = true;
      _marketplaceDeck = widget.deck as MarketplaceDeck;
      DeckDao.isDeckDownloaded(widget.deck.id).then((value) {
        setState(() {
          _deckDownloaded = value;
        });
      });
    }
  }

  void _downloadDeck() {
    DeckDao.addDeckToCollection(widget.deck.id);
    setState(() {
      _deckDownloaded = true;
    });
  }

  void _deleteDeck() {
    DeckDao.removeDeckFromCollection(widget.deck.id);
    setState(() {
      _deckDownloaded = false;
    });
  }

  void _onMarketplaceDeckTap() {
    if (!_deckDownloaded) {
      _downloadDeck();
    } else {
      _deleteDeck();
    }
  }

  void _onPlayDeckTap() {
    Navigator.of(context).push(
      MaterialPageRoute(
        builder: (context) => PlayDeckView(deck: widget.deck, score: Score(0)),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: () {
        if (_isMarketplaceDeck) {
          _onMarketplaceDeckTap();
        } else {
          _onPlayDeckTap();
        }
      },
      child: Card(
          elevation: 4,
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: <Widget>[
              Container(
                height: 175,
                decoration: BoxDecoration(
                  image: DecorationImage(
                    image: NetworkImage(widget.deck.image.replaceAll(
                        'http://localhost:8080', ServerPath.baseUrl)),
                    fit: BoxFit.cover,
                  ),
                ),
                child: Stack(children: [
                  if (_isMarketplaceDeck)
                    Align(
                      alignment: Alignment.topRight,
                      child: GestureDetector(
                        onTap: () {
                          _onMarketplaceDeckTap();
                        },
                        child: Container(
                          padding: const EdgeInsets.all(8),
                          color: Colors.black.withOpacity(0.3),
                          child: _deckDownloaded
                              ? const Icon(Icons.delete, color: Colors.white)
                              : const Icon(Icons.download, color: Colors.white),
                        ),
                      ),
                    ),
                  Align(
                    alignment: Alignment.bottomLeft,
                    child: Row(children: [
                      Align(
                          alignment: FractionalOffset.bottomCenter,
                          child: Container(
                            padding: const EdgeInsets.all(8),
                            color: Colors.black.withOpacity(0.7),
                            child: Text(
                              widget.deck.name,
                              style: const TextStyle(
                                color: Colors.white,
                                fontSize: 20,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                          )),
                      Expanded(
                          child: Align(
                              alignment: Alignment.bottomLeft,
                              child: SizedBox(
                                  height: 30,
                                  child: ListView(
                                      scrollDirection: Axis.horizontal,
                                      children: [
                                        for (var tag in widget.deck.tags)
                                          Container(
                                            padding: const EdgeInsets.all(8),
                                            color: HexColor(tag.color)
                                                .withOpacity(0.9),
                                            child: Text(
                                              tag.name,
                                              style: const TextStyle(
                                                color: Colors.black,
                                                fontSize: 12,
                                                fontWeight: FontWeight.bold,
                                              ),
                                            ),
                                          )
                                      ])))),
                    ]),
                  ),
                ]),
              ),
              Container(
                color: widget.deck.color == '#00000000'
                    ? Colors.purple
                    : HexColor(widget.deck.color),
                padding: const EdgeInsets.symmetric(vertical: 8),
                child: Row(
                  mainAxisSize: MainAxisSize.min,
                  mainAxisAlignment: MainAxisAlignment.spaceAround,
                  children: <Widget>[
                    Column(
                      mainAxisSize: MainAxisSize.min,
                      children: <Widget>[
                        const Icon(Icons.credit_card, color: Colors.white),
                        const SizedBox(height: 4),
                        Text(
                          widget.deck.cardCount,
                          style: const TextStyle(color: Colors.white),
                        ),
                      ],
                    ),
                    if (_isMarketplaceDeck)
                      Column(
                        mainAxisSize: MainAxisSize.min,
                        children: <Widget>[
                          const Icon(Icons.account_circle_rounded,
                              color: Colors.white),
                          const SizedBox(height: 4),
                          Text(
                            _marketplaceDeck.owner,
                            style: const TextStyle(color: Colors.white),
                          ),
                        ],
                      ),
                    Column(
                      mainAxisSize: MainAxisSize.min,
                      children: <Widget>[
                        const Icon(Icons.emoji_events, color: Colors.white),
                        const SizedBox(height: 4),
                        Text(
                          widget.deck.score,
                          style: const TextStyle(color: Colors.white),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ],
          )),
    );
  }
}
