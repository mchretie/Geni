import 'dart:convert';

import 'package:http/http.dart' as http;
import 'package:mobile_deckz/http_dao/http_dao.dart';
import 'package:mobile_deckz/http_dao/server_path.dart';

class Deck {
  final String id;
  final String name;
  final String color;
  final String image;
  final String cardCount;
  final List<String> tags;

  Deck(
      {required this.id,
      required this.name,
      required this.color,
      required this.image,
      required this.cardCount,
      required this.tags});

  factory Deck.fromJson(Map<String, dynamic> json) {
    return Deck(
      id: json['id'],
      name: json['name'],
      color: json['color'],
      image: json['image'],
      cardCount: json['cardCount'].toString(),
      tags: json['tags'].cast<String>(),
    );
  }
}

class DeckDao {
  static Future<List<Deck>> getAllDecks() async {
    final http.Response response =
        await HttpDao.get(ServerPath.getAllDecksPath);
    if (response.statusCode == 200) {
      List<dynamic> json = jsonDecode(response.body);
      List<Deck> decks = [];
      for (var deck in json) {
        decks.add(Deck.fromJson(deck));
      }
      return decks;
    }
    return [];
  }
}
