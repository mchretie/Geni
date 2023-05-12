import 'package:flutter/material.dart';
import 'package:mobile_deckz/model/deck/hex_color.dart';
import 'package:mobile_deckz/model/deck/marketplace_deck.dart';

import '../../http_dao/deck_dao.dart';
import '../../http_dao/server_path.dart';

class DeckMarketplaceView extends StatefulWidget {
  final MarketplaceDeck deck;

  const DeckMarketplaceView({super.key, required this.deck});

  @override
  State<DeckMarketplaceView> createState() => _DeckMarketplaceViewState();
}

class _DeckMarketplaceViewState extends State<DeckMarketplaceView> {
  bool deckDownloaded = false;

  @override
  void initState() {
    super.initState();
    DeckDao.isDeckDownloaded(widget.deck.id).then((value) {
      setState(() {
        deckDownloaded = value;
      });
    });
  }

  void _downloadDeck() {
    DeckDao.addDeckToCollection(widget.deck.id);
    setState(() {
      deckDownloaded = true;
    });
  }

  void _deleteDeck() {
    DeckDao.removeDeckFromCollection(widget.deck.id);
    setState(() {
      deckDownloaded = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisSize: MainAxisSize.min,
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: <Widget>[
        Container(
          height: 175,
          decoration: BoxDecoration(
            image: DecorationImage(
              image: NetworkImage(widget.deck.image
                  .replaceAll('http://localhost:8080', ServerPath.baseUrl)),
              fit: BoxFit.cover,
            ),
          ),
          child: Stack(children: [
            Align(
              alignment: Alignment.topRight,
              child: GestureDetector(
                onTap: () {
                  if (!deckDownloaded) {
                    _downloadDeck();
                  } else {
                    _deleteDeck();
                  }
                },
                child: Container(
                  padding: const EdgeInsets.all(8),
                  color: Colors.black.withOpacity(0.3),
                  child: deckDownloaded
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
                                      color:
                                          HexColor(tag.color).withOpacity(0.9),
                                      child: Text(
                                        tag.name,
                                        style: const TextStyle(
                                          color: Colors.black,
                                          fontSize: 12,
                                          fontWeight: FontWeight.bold,
                                        ),
                                      ),
                                    )
                                ]
                            )
                        )
                    )
                ),
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
              Column(
                mainAxisSize: MainAxisSize.min,
                children: <Widget>[
                  const Icon(Icons.account_circle_rounded, color: Colors.white),
                  const SizedBox(height: 4),
                  Text(
                    widget.deck.owner,
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
    );
  }
}
