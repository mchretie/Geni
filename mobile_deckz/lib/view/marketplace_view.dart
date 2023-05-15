import 'package:flutter/material.dart';
import 'package:mobile_deckz/model/deck/marketplace_deck.dart';
import 'package:mobile_deckz/view/deck_view.dart';
import '../http_dao/marketplace_dao.dart';

class MarketPlaceView extends StatefulWidget {
  const MarketPlaceView({super.key});

  @override
  State<MarketPlaceView> createState() => _MarketPlaceViewState();
}

enum MarketplaceFilter { starASC, starDESC, alphabetASC, alphabetDESC, none }

class _MarketPlaceViewState extends State<MarketPlaceView> {
  String dropdownValue = 'Name';
  List<String> dropdownItems = ['Name', 'Tag'];
  MarketplaceFilter? _marketplaceFilter = MarketplaceFilter.none;

  Future<List<MarketplaceDeck>> _decksFuture =
  MarketPlaceDao.getAllMarketplaceDecks();

  final textEditController = TextEditingController();

  Future<void> _onSearchTextChanged(String text) async {
    setState(() {
      if (dropdownValue == 'Name') {
        _decksFuture = MarketPlaceDao.searchDecks(text);
      } else if (dropdownValue == 'Tag') {
        _decksFuture = MarketPlaceDao.searchDecksByTags(text);
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<List<MarketplaceDeck>>(
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
                                    .map<DropdownMenuItem<String>>((
                                    String value) {
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
                                onPressed: () =>
                                    showDialog<String>(
                                        context: context,
                                        builder: (BuildContext context) =>
                                            AlertDialog(
                                              title: const Text('Filter by'),
                                              content: StatefulBuilder(
                                                  builder: (
                                                      BuildContext context,
                                                      StateSetter setState) {
                                                    return Column(
                                                      mainAxisSize: MainAxisSize
                                                          .min,
                                                      children: [
                                                        RadioListTile(
                                                          title: const Text(
                                                              'Star ASC'),
                                                          value: MarketplaceFilter
                                                              .starASC,
                                                          groupValue: _marketplaceFilter,
                                                          onChanged:
                                                              (
                                                              MarketplaceFilter? filter) {
                                                            setState(() {
                                                              _marketplaceFilter =
                                                                  filter;
                                                            });
                                                          },
                                                        ),
                                                        RadioListTile(
                                                          title: const Text(
                                                              'Star DESC'),
                                                          value: MarketplaceFilter
                                                              .starDESC,
                                                          groupValue: _marketplaceFilter,
                                                          onChanged:
                                                              (
                                                              MarketplaceFilter? filter) {
                                                            setState(() {
                                                              _marketplaceFilter =
                                                                  filter;
                                                            });
                                                          },
                                                        ),
                                                        RadioListTile(
                                                          title: const Text(
                                                              'Alphabet ASC'),
                                                          value: MarketplaceFilter
                                                              .alphabetDESC,
                                                          groupValue: _marketplaceFilter,
                                                          onChanged:
                                                              (
                                                              MarketplaceFilter? filter) {
                                                            setState(() {
                                                              _marketplaceFilter =
                                                                  filter;
                                                            });
                                                          },
                                                        ),
                                                        RadioListTile(
                                                          title: const Text(
                                                              'Alphabet DESC'),
                                                          value: MarketplaceFilter
                                                              .alphabetASC,
                                                          groupValue: _marketplaceFilter,
                                                          onChanged:
                                                              (
                                                              MarketplaceFilter? filter) {
                                                            setState(() {
                                                              _marketplaceFilter =
                                                                  filter;
                                                            });
                                                          },
                                                        ),
                                                        RadioListTile(
                                                          title: const Text(
                                                              'None'),
                                                          value: MarketplaceFilter
                                                              .none,
                                                          groupValue: _marketplaceFilter,
                                                          onChanged:
                                                              (
                                                              MarketplaceFilter? filter) {
                                                            setState(() {
                                                              _marketplaceFilter =
                                                                  filter;
                                                            });
                                                          },
                                                        ),
                                                      ],
                                                    );
                                                  }),
                                              actions: <Widget>[
                                                TextButton(
                                                  onPressed: () =>
                                                      Navigator.pop(
                                                          context, 'Cancel'),
                                                  child: const Text('Cancel'),
                                                ),
                                                TextButton(
                                                  onPressed: () =>
                                                      Navigator.pop(
                                                          context, 'OK'),
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
                                        child: DeckView(deck: deck));
                                  }))
                        ])));
          }
        });
  }
}
