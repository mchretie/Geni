import 'package:flutter/material.dart';
import 'package:mobile_deckz/model/deck/hex_color.dart';
import 'package:mobile_deckz/model/deck/marketplace_deck.dart';
import 'package:mobile_deckz/view/playdeck/playdeck_view.dart';

import '../../http_dao/deck_dao.dart';
import '../../http_dao/server_path.dart';
import '../http_dao/game_history_dao.dart';
import '../model/deck/deck.dart';
import '../model/deck/score.dart';
import '../model/game_history/game.dart';
import '../model/game_history/game_history.dart';

class DeckView extends StatefulWidget {
  final Deck deck;

  const DeckView({super.key, required this.deck});

  @override
  State<DeckView> createState() => _DeckViewState();
}

class _DeckViewState extends State<DeckView> {
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

  Color _getTextColor(String color) {
    return HexColor.isDark(widget.deck.color) ? Colors.white : Colors.black;
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

  Widget _buildHistory() {
    return FutureBuilder<GameHistory>(
      future: GameHistoryDao.getGameHistory(),
      builder: (context, snapshot) {
        if (snapshot.hasData) {
          final List<Game>? games = snapshot.data?.games
              .where(
                (element) => element.getDeckName() == widget.deck.name,
              )
              .toList();
          return SizedBox(
            height: 200,
            width: 200,
            child: ListView.builder(
              padding: const EdgeInsets.all(8),
              itemCount: games?.length,
              itemBuilder: (BuildContext context, int index) {
                return SizedBox(
                  height: 20,
                  child: Center(
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        Text(games![index].getFormattedTimestamp()),
                        SizedBox(width: 20),
                        Text(games[index].getScore().toString()),
                        Text(' Pts'),
                      ],
                    ),
                  ),
                );
              },
            ),
          );
        } else {
          return const CircularProgressIndicator();
        }
      },
    );
  }

  Future<void> _onPlayDeckTap(BuildContext context) {
    return showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text(widget.deck.name),
          content: SingleChildScrollView(
            child: ListBody(
              children: <Widget>[
                _buildHistory(),
              ],
            ),
          ),
          actions: <Widget>[
            ElevatedButton.icon(
              icon: Icon(Icons.play_arrow),
              label: const Text('Jouer'),
              onPressed: () {
                Navigator.of(context).pushReplacement(
                  MaterialPageRoute(
                    builder: (context) => PlayDeckView(
                      deck: widget.deck,
                      score: Score(0, widget.deck.id),
                    ),
                  ),
                );
              },
            ),
          ],
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: () {
        if (_isMarketplaceDeck) {
          _onMarketplaceDeckTap();
        } else {
          _onPlayDeckTap(context);
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
                                              style: TextStyle(
                                                color: _getTextColor(tag.color),
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
                        Icon(Icons.credit_card,
                            color: _getTextColor(widget.deck.color)),
                        const SizedBox(height: 4),
                        Text(
                          widget.deck.cardCount,
                          style: TextStyle(
                              color: _getTextColor(widget.deck.color)),
                        ),
                      ],
                    ),
                    if (_isMarketplaceDeck)
                      Column(
                        mainAxisSize: MainAxisSize.min,
                        children: <Widget>[
                          Icon(Icons.account_circle_rounded,
                              color: _getTextColor(widget.deck.color)),
                          const SizedBox(height: 4),
                          Text(
                            _marketplaceDeck.owner,
                            style: TextStyle(
                                color: _getTextColor(widget.deck.color)),
                          ),
                        ],
                      ),
                    Column(
                      mainAxisSize: MainAxisSize.min,
                      children: <Widget>[
                        Icon(Icons.emoji_events,
                            color: _getTextColor(widget.deck.color)),
                        const SizedBox(height: 4),
                        Text(
                          widget.deck.score,
                          style: TextStyle(
                              color: _getTextColor(widget.deck.color)),
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
