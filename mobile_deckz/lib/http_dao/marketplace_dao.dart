import 'dart:convert';

import 'package:http/http.dart' as http;
import 'package:mobile_deckz/http_dao/http_dao.dart';
import 'package:mobile_deckz/http_dao/server_path.dart';
import 'package:mobile_deckz/http_dao/deck_dao.dart';
import 'package:mobile_deckz/model/deck/deck.dart';
import 'package:mobile_deckz/model/deck/marketplace_deck.dart';

import '../model/indulgent_validator.dart';

class MarketPlaceDao {
  static Future<List<MarketplaceDeck>> getAllMarketplaceDecks() async {
    final http.Response response =
        await HttpDao.get(ServerPath.getMarketplacePath);
    if (response.statusCode == 200) {
      List<dynamic> json = jsonDecode(response.body);
      Map<String, dynamic> scores = await DeckDao.getBestDecksScore(json);
      List<MarketplaceDeck> decks = [];
      for (var deck in json) {
        deck['score'] =
            scores.containsKey(deck['id']) ? scores[deck['id']] : 'N/A';
        decks.add(MarketplaceDeck.fromJson(deck));
      }
      return decks;
    }
    return [];
  }

  static Future<List<MarketplaceDeck>> searchDecks(String query) async {
    List<MarketplaceDeck> decks = await getAllMarketplaceDecks();
    if (query.isEmpty) {
      return decks;
    }
    IndulgentValidator validator = IndulgentValidator();
    return decks
        .where((deck) => validator
            .addTolerance(deck.name)
            .contains(validator.addTolerance(query)))
        .toList();
  }

  static Future<List<MarketplaceDeck>> searchDecksByTags(String query) async {
    List<MarketplaceDeck> decks = await getAllMarketplaceDecks();
    if (query.isEmpty) {
      return decks;
    }
    IndulgentValidator validator = IndulgentValidator();
    return decks.where((deck) {
      return deck.tags.any((tag) {
        return validator
            .addTolerance(tag.name)
            .contains(validator.addTolerance(query));
      });
    }).toList();
  }

  static Future<List<MarketplaceDeck>> searchDecksByOwner(String query) async {
    List<MarketplaceDeck> decks = await getAllMarketplaceDecks();
    if (query.isEmpty) {
      return decks;
    }
    IndulgentValidator validator = IndulgentValidator();
    return decks
        .where((deck) => validator
            .addTolerance(deck.owner)
            .contains(validator.addTolerance(query)))
        .toList();
  }

  static Future<List<MarketplaceDeck>> _showSavedDecks(
      bool showSavedDecks) async {
    List<MarketplaceDeck> decks = await getAllMarketplaceDecks();
    List<Deck> savedDecks = await DeckDao.getAllDecks();
    if (showSavedDecks) {
      return decks.where((deck) {
        return savedDecks.any((savedDeck) {
          return savedDeck.id == deck.id;
        });
      }).toList();
    }
    return decks.where((deck) {
      return savedDecks.every((savedDeck) {
        return savedDeck.id != deck.id;
      });
    }).toList();
  }

  static Future<List<MarketplaceDeck>> sortDecksByAlphabet(
      bool ascending, bool showUserDecks) async {
    List<MarketplaceDeck> decks = await _showSavedDecks(showUserDecks);
    decks.sort((a, b) =>
        ascending ? a.name.compareTo(b.name) : b.name.compareTo(a.name));
    return decks;
  }

  static Future<List<MarketplaceDeck>> sortDecksByStars(
      bool ascending, bool showUserDecks) async {
    List<MarketplaceDeck> decks = await _showSavedDecks(showUserDecks);
    print(decks);
    decks.sort((a, b) => ascending
        ? a.rating.compareTo(b.rating)
        : b.rating.compareTo(a.rating));
    return decks;
  }
}
