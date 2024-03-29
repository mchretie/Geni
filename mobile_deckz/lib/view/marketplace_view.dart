import 'package:flutter/material.dart';
import 'package:mobile_deckz/model/deck/marketplace_deck.dart';
import 'package:mobile_deckz/view/deck_view.dart';
import '../http_dao/marketplace_dao.dart';

class MarketPlaceView extends StatefulWidget {
  const MarketPlaceView({Key? key}) : super(key: key);

  @override
  State<MarketPlaceView> createState() => MarketPlaceViewState();
}

enum MarketplaceFilter { starASC, starDESC, alphabetASC, alphabetDESC, none }

class MarketPlaceViewState extends State<MarketPlaceView> {
  String dropdownValue = 'Name';
  List<String> dropdownItems = ['Name', 'Tag', 'Owner'];
  MarketplaceFilter? _marketplaceFilter = MarketplaceFilter.none;
  bool _showSavedDecks = true;

  Future<List<MarketplaceDeck>> _decksFuture =
      MarketPlaceDao.getAllMarketplaceDecks();

  final textEditController = TextEditingController();

  Future<void> _onSearchTextChanged(String text) async {
    _decksFuture = MarketPlaceDao.getAllMarketplaceDecks();

    await _sortDecks();
    List<MarketplaceDeck> decks = await _decksFuture;
    setState(() {
      if (dropdownValue == 'Name') {
        _decksFuture = MarketPlaceDao.searchDecks(decks, text);
      } else if (dropdownValue == 'Tag') {
        _decksFuture = MarketPlaceDao.searchDecksByTags(decks, text);
      } else if (dropdownValue == 'Owner') {
        _decksFuture = MarketPlaceDao.searchDecksByOwner(decks, text);
      }
    });
  }

  Future<void> reloadDecks() async {
    reloadDecks();
  }

  Future<void> _reloadDecks() async {
    setState(() {
      _decksFuture = MarketPlaceDao.getAllMarketplaceDecks();
    });
  }

  Future<void> _sortDecks() async {
    List<MarketplaceDeck> decks = await _decksFuture;
    setState(() {
      switch (_marketplaceFilter) {
        case MarketplaceFilter.starASC:
          _decksFuture =
              MarketPlaceDao.sortDecksByStars(decks, true, _showSavedDecks);
          break;
        case MarketplaceFilter.starDESC:
          _decksFuture =
              MarketPlaceDao.sortDecksByStars(decks, false, _showSavedDecks);
          break;
        case MarketplaceFilter.alphabetASC:
          _decksFuture =
              MarketPlaceDao.sortDecksByAlphabet(decks, true, _showSavedDecks);
          break;
        case MarketplaceFilter.alphabetDESC:
          _decksFuture =
              MarketPlaceDao.sortDecksByAlphabet(decks, false, _showSavedDecks);
          break;
        case MarketplaceFilter.none:
          _decksFuture =
              MarketPlaceDao.sortDecksByNothing(decks, _showSavedDecks);
          break;
        default:
          break;
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return RefreshIndicator(
      onRefresh: _reloadDecks,
      child: FutureBuilder<List<MarketplaceDeck>>(
          future: _decksFuture,
          builder: (BuildContext context,
              AsyncSnapshot<List<MarketplaceDeck>> snapshot) {
            if (snapshot.connectionState == ConnectionState.waiting) {
              return const Center(child: CircularProgressIndicator());
            } else if (snapshot.hasError) {
              return Text('Error: ${snapshot.error}');
            } else {
              List<MarketplaceDeck> deckList = snapshot.data ?? [];
              return Scaffold(
                  body: Center(
                      child: Column(
                          mainAxisSize: MainAxisSize.min,
                          crossAxisAlignment: CrossAxisAlignment.stretch,
                          children: <Widget>[
                    Padding(
                        padding: const EdgeInsets.all(16),
                        child: Row(
                          children: [
                            DropdownButton<String>(
                              value: dropdownValue,
                              onChanged: (String? newValue) {
                                setState(() {
                                  dropdownValue = newValue!;
                                });
                              },
                              items: dropdownItems
                                  .map<DropdownMenuItem<String>>(
                                      (String value) {
                                return DropdownMenuItem<String>(
                                  value: value,
                                  child: Text(value),
                                );
                              }).toList(),
                            ),
                            const SizedBox(width: 20),
                            Expanded(
                              child: TextField(
                                controller: textEditController,
                                onSubmitted: (text) =>
                                    _onSearchTextChanged(text),
                                decoration: const InputDecoration(
                                  hintText: 'Search',
                                  prefixIcon: Icon(Icons.search),
                                ),
                              ),
                            ),
                            IconButton(
                                onPressed: () => showDialog<String>(
                                    context: context,
                                    builder: (BuildContext context) =>
                                        AlertDialog(
                                          title: const Text('Filter by'),
                                          content: StatefulBuilder(builder:
                                              (BuildContext context,
                                                  StateSetter setState) {
                                            return Column(
                                              mainAxisSize: MainAxisSize.min,
                                              children: [
                                                RadioListTile(
                                                  title: Row(
                                                    children: const [
                                                      Text('Stars'),
                                                      Icon(Icons.arrow_downward)
                                                    ],
                                                  ),
                                                  value: MarketplaceFilter
                                                      .starDESC,
                                                  groupValue:
                                                      _marketplaceFilter,
                                                  onChanged: (MarketplaceFilter?
                                                      filter) {
                                                    setState(() {
                                                      _marketplaceFilter =
                                                          filter;
                                                    });
                                                  },
                                                ),
                                                RadioListTile(
                                                  title: Row(
                                                    children: const [
                                                      Text('Stars'),
                                                      Icon(Icons.arrow_upward)
                                                    ],
                                                  ),
                                                  value:
                                                      MarketplaceFilter.starASC,
                                                  groupValue:
                                                      _marketplaceFilter,
                                                  onChanged: (MarketplaceFilter?
                                                      filter) {
                                                    setState(() {
                                                      _marketplaceFilter =
                                                          filter;
                                                    });
                                                  },
                                                ),
                                                RadioListTile(
                                                  title: Row(
                                                    children: const [
                                                      Text('Alphabet'),
                                                      Icon(Icons.arrow_downward)
                                                    ],
                                                  ),
                                                  value: MarketplaceFilter
                                                      .alphabetASC,
                                                  groupValue:
                                                      _marketplaceFilter,
                                                  onChanged: (MarketplaceFilter?
                                                      filter) {
                                                    setState(() {
                                                      _marketplaceFilter =
                                                          filter;
                                                    });
                                                  },
                                                ),
                                                RadioListTile(
                                                  title: Row(
                                                    children: const [
                                                      Text('Alphabet'),
                                                      Icon(Icons.arrow_upward)
                                                    ],
                                                  ),
                                                  value: MarketplaceFilter
                                                      .alphabetDESC,
                                                  groupValue:
                                                      _marketplaceFilter,
                                                  onChanged: (MarketplaceFilter?
                                                      filter) {
                                                    setState(() {
                                                      _marketplaceFilter =
                                                          filter;
                                                    });
                                                  },
                                                ),
                                                RadioListTile(
                                                  title: const Text('None'),
                                                  value: MarketplaceFilter.none,
                                                  groupValue:
                                                      _marketplaceFilter,
                                                  onChanged: (MarketplaceFilter?
                                                      filter) {
                                                    setState(() {
                                                      _marketplaceFilter =
                                                          filter;
                                                    });
                                                  },
                                                ),
                                                CheckboxListTile(
                                                  value: _showSavedDecks,
                                                  title: const Text(
                                                      'Show saved decks'),
                                                  onChanged: (bool? value) {
                                                    setState(() {
                                                      _showSavedDecks = value!;
                                                    });
                                                  },
                                                ),
                                              ],
                                            );
                                          }),
                                          actions: <Widget>[
                                            TextButton(
                                              onPressed: () => Navigator.pop(
                                                  context, 'Cancel'),
                                              child: const Text('Cancel'),
                                            ),
                                            TextButton(
                                              onPressed: () {
                                                _sortDecks();
                                                Navigator.pop(context, 'OK');
                                              },
                                              child: const Text('OK'),
                                            ),
                                          ],
                                        )),
                                icon: const Icon(Icons.filter_list)),
                          ],
                        )),
                    Expanded(
                        child: ListView.builder(
                            itemCount: deckList.length,
                            itemBuilder: (context, index) {
                              final MarketplaceDeck deck = deckList[index];
                              return Padding(
                                  padding: const EdgeInsets.all(4.0),
                                  child: DeckView(deck: deck, callback: () {}));
                            }))
                  ])));
            }
          }),
    );
  }
}
