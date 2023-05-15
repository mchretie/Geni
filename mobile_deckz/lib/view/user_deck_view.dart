import 'package:flutter/material.dart';
import 'package:mobile_deckz/view/deck_view.dart';
import 'package:visibility_detector/visibility_detector.dart';

import '../http_dao/deck_dao.dart';
import '../model/deck/deck.dart';

class UserDeckView extends StatefulWidget {
  const UserDeckView({super.key});

  @override
  State<UserDeckView> createState() => _UserDeckViewState();
}

class _UserDeckViewState extends State<UserDeckView> {
  String dropdownValue = 'Name';
  List<String> dropdownItems = ['Name', 'Tag'];

  Future<List<Deck>> _decksFuture = DeckDao.getAllDecks();

  final textEditController = TextEditingController();

  Future<void> _onSearchTextChanged(String text) async {
    setState(() {
      if (dropdownValue == 'Name') {
        _decksFuture = DeckDao.searchDecks(text);
      } else if (dropdownValue == 'Tag') {
        _decksFuture = DeckDao.searchDecksByTags(text);
      }
    });
  }

  void onVisibilityChanged(VisibilityInfo info) {
    if (info.visibleFraction == 1) {
      // Fetch updated values when the page becomes fully visible
      _reloadDecks();
    }
  }

  Future<void> _reloadDecks() async {
    setState(() {
      _decksFuture = DeckDao.getAllDecks();
    });
  }

  @override
  Widget build(BuildContext context) {
    return VisibilityDetector(
      key: const Key('userDeckView'),
      onVisibilityChanged: onVisibilityChanged,
      child: Scaffold(
          body: RefreshIndicator(
              onRefresh: _reloadDecks,
              child: FutureBuilder<List<Deck>>(
                  future: _decksFuture,
                  builder:
                      (BuildContext context, AsyncSnapshot<List<Deck>> snapshot) {
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
                                  ],
                                )),
                            Expanded(
                                child: ListView.builder(
                                    itemCount: deckList.length,
                                    itemBuilder: (context, index) {
                                      final Deck deck = deckList[index];
                                      return Padding(
                                          padding: const EdgeInsets.all(4.0),
                                          child: DeckView(deck: deck));
                                    }))
                          ])));
                    }
                  }))),
    );
  }
}
