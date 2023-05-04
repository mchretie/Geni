import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:http/http.dart' as http;
import 'package:mobile_deckz/http_dao/http_dao.dart';
import 'package:mobile_deckz/http_dao/server_path.dart';

import '../model/deck/deck.dart';

class DeckDao {
  static Future<List<Deck>> getAllDecks() async {
    final http.Response response =
    await HttpDao.get(ServerPath.getAllDecksPath);
    if (response.statusCode == 200) {
      List<dynamic> json = jsonDecode(response.body);
      List<Deck> decks = [];
      for (var deck in json) {
        deck['score'] = await getBestDeckScore(deck['id']);
        decks.add(Deck.fromJson(deck));
      }
      return decks;
    }
    return [];
  }

  static Future<String> getBestDeckScore(String deckId) async {
    final http.Response response = await HttpDao.get(
        Uri.parse('${ServerPath.getBestScorePath}?deck_id=$deckId'));
    if (response.statusCode == 200) {
      if (response.body == 'null') {
        return 'N/A';
      }
      Map<String, dynamic> json = jsonDecode(response.body);
      return json['score'].toString();
    }
    return 'N/A';
  }

  static Future<void> loadDeck(Deck deck) async {
    Map<String, String> queryParameters = {
      'deck_id': deck.id,
    };
    final uri = Uri.parse(ServerPath.getDeckPath)
        .replace(queryParameters: queryParameters);
    final http.Response response = await HttpDao.get(uri);
    if (response.statusCode == 200) {
      Map<String, dynamic> json = jsonDecode(response.body);
      deck.loadCards(json);
    }
  }

  static Future<bool> isDeckDownloaded(String deckID) async {
    final http.Response response =
    await HttpDao.get(ServerPath.getAllDecksPath);
    if (response.statusCode == 200) {
      List<dynamic> json = jsonDecode(response.body);
      for (var deck in json) {
        if (deck['id'] == deckID) {
          return true;
        }
      }
    }
    return false;
  }

  static Future<void> addDeckToCollection(String deckID) async {
    final http.Response response = await HttpDao.post(
        Uri.parse('${ServerPath.addDeckToCollectionPath}?deck_id=$deckID'), "");
    if (response.statusCode == 200) {
      debugPrint('Deck added to collection');
    }
  }

  static Future<void> removeDeckFromCollection(String deckID) async {
    final http.Response response = await HttpDao.delete(
        Uri.parse('${ServerPath.removeDeckFromCollectionPath}?deck_id=$deckID'),
        "");
    if (response.statusCode == 200) {
      debugPrint('Deck removed from collection');
    }
  }

  static Future<List<Deck>> searchDecks(String query) async {
    List<Deck> decks = await getAllDecks();

    // TODO add tolerance (new IndulgentValidater)
    // final pattern = RegExp('${validator.addTolerance(query)}.*');
    // decks = decks.where((deck) => pattern.hasMatch(validator.addTolerance(deck.name))).toList();

    if (query.isNotEmpty) {
      for (var deck in decks) {
        if (!deck.name.toLowerCase().contains(query.toLowerCase())) {
          return [deck];
        }
      }
    }
    return decks;
  }
}

// Future<List<DeckMetadata>> searchDecks(String deckName) async {
//   if (deckName.isEmpty) {
//     return getAllDecksMetadata();
//   }
//
//   final pattern = RegExp('${validator.addTolerance(deckName)}.*');
//
//   final allDecks = await getAllDecksMetadata();
//   final filteredDecks = allDecks.where((deck) => pattern.hasMatch(validator.addTolerance(deck.name)));
//
//   return filteredDecks.toList();
// }
