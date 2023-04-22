import 'dart:convert';

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
        print(deck);
        decks.add(Deck.fromJson(deck));
      }
      return decks;
    }
    return [];
  }

  static Future<void> getDeck(String id) async {
    Map<String, String> queryParameters = {
      'deck_id': id,
    };
    final uri = Uri.parse(ServerPath.getDeckPath)
        .replace(queryParameters: queryParameters);
    print(uri);
    final http.Response response = await HttpDao.get(uri);
    if (response.statusCode == 200) {
      Map<String, dynamic> json = jsonDecode(response.body);
      print(json);
    }
  }

// static Future<List<Deck>> searchDecks(String query) async {
//   final http.Response response =
//       await HttpDao.get(ServerPath.searchDecksPath + query);
//   if (response.statusCode == 200) {
//     List<dynamic> json = jsonDecode(response.body);
//     List<Deck> decks = [];
//     for (var deck in json) {
//       decks.add(Deck.fromJson(deck));
//     }
//     return decks;
//   }
//   return [];
// }
}
