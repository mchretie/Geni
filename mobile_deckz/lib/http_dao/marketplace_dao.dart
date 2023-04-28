import 'dart:convert';

import 'package:http/http.dart' as http;
import 'package:mobile_deckz/http_dao/http_dao.dart';
import 'package:mobile_deckz/http_dao/server_path.dart';

import '../model/deck/deck.dart';

class MarketPlaceDao {
  static Future<List<Deck>> getAllMarketplaceDecks() async {
    final http.Response response =
        await HttpDao.get(ServerPath.getMarketplacePath);
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
    final http.Response response =
    await HttpDao.get(Uri.parse('${ServerPath.getBestScorePath}?deck=$deckId'));
    if (response.statusCode == 200) {
      if (response.body == 'null') {
        return 'N/A';
      }
      Map<String, dynamic> json = jsonDecode(response.body);
      return json['score'].toString();
    }
    return 'N/A';
  }
}