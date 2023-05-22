import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:http/http.dart' as http;
import 'package:mobile_deckz/http_dao/http_dao.dart';
import 'package:mobile_deckz/http_dao/server_path.dart';

import '../model/deck/deck.dart';
import '../model/indulgent_validator.dart';

class DeckDao {

  static Future<List<Deck>> getAllDecks() async {
    final http.Response response =
    await HttpDao.get(ServerPath.getAllDecksPath);
    if (response.statusCode == 200) {
      List<dynamic> json = jsonDecode(response.body);
      List<Deck> decks = [];
      Map<String, dynamic> scores = await getBestDecksScore(json);
      for (var deck in json) {
        deck['score'] = scores.containsKey(deck['id']) ? scores[deck['id']] : 'N/A';
        decks.add(Deck.fromJson(deck));
      }
      return decks;
    }
    return [];
  }

  static Future<Map<String, String>> getBestDecksScore(List<dynamic> decksJson) async {
    String path = '${ServerPath.getBestScoresPath}?';
    for (dynamic deck in decksJson) {
      path += "deckId[]=${deck['id']}&";
    }
    final http.Response response = await HttpDao.get(Uri.parse(path));

    Map<String, String> scores = {};
    if (response.statusCode == 200) {
      if (response.body == 'null') {
        return scores;
      }
      List<dynamic> json = jsonDecode(response.body);
      for (dynamic deckJson in json) {
        scores[deckJson['deckId']] = deckJson['score'].toString();
      }
      return scores;
    }
    return scores;
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
  }

  static Future<void> removeDeckFromCollection(String deckID) async {
    final http.Response response = await HttpDao.delete(
        Uri.parse('${ServerPath.removeDeckFromCollectionPath}?deck_id=$deckID'),
        "");
  }

  static Future<List<Deck>> searchDecks(String query) async {
    List<Deck> decks = await getAllDecks();
    if (query.isEmpty) {
      return decks;
    }
    IndulgentValidator validator = IndulgentValidator();
    return decks.where((deck) => validator.addTolerance(deck.name).contains(validator.addTolerance(query))).toList();
  }

  static Future<List<Deck>> searchDecksByTags(String query) async {
    List<Deck> decks = await getAllDecks();
    if (query.isEmpty) {
      return decks;
    }
    IndulgentValidator validator = IndulgentValidator();
    return decks.where((deck) {
      return deck.tags.any((tag) {
        return validator.addTolerance(tag.name).contains(validator.addTolerance(query));
      });
    }).toList();
  }
}
