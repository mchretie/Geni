import 'package:flutter/material.dart';
import '../http_dao/marketplace_dao.dart';
import '../model/deck/deck.dart';
import 'deck_marcketplace_view.dart';

class MarketPlaceView extends StatefulWidget {
  const MarketPlaceView({super.key});

  @override
  State<MarketPlaceView> createState() => _MarketPlaceViewState();
}

class _MarketPlaceViewState extends State<MarketPlaceView> {
  String dropdownValue = 'Name';
  List<String> dropdownItems = ['Name', 'Tag'];
  @override
  Widget build(BuildContext context) {
    final Future<List<Deck>> decks = MarketPlaceDao.getAllMarketplaceDecks();

    return FutureBuilder<List<Deck>>(
        future: decks,
        builder: (BuildContext context, AsyncSnapshot<List<Deck>> snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Text('Error: ${snapshot.error}');
          } else {
            List<Deck> deckList = snapshot.data ?? [];
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
                                    items: dropdownItems.map<DropdownMenuItem<String>>((String value) {
                                      return DropdownMenuItem<String>(
                                        value: value,
                                        child: Text(value),
                                      );
                                    }).toList(),
                                  ),
                                  const SizedBox(width: 20),
                                  const Expanded(
                                    child: TextField(
                                      decoration: InputDecoration(
                                        hintText: 'Search',
                                        prefixIcon: Icon(Icons.search),
                                      ),
                                    ),
                                  ),
                                ],
                              )),
                          Expanded(
                              child: ListView.builder(
                                  itemCount: deckList.length,
                                  itemBuilder: (context, index) {
                                    final Deck deck = deckList[index];
                                    return Padding(
                                        padding: const EdgeInsets.all(4.0),
                                        child: DeckMarketplaceView(deck: deck));
                                  }))
                        ])));
          }
        });
  }
}