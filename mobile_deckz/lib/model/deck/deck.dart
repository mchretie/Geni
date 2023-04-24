import 'package:mobile_deckz/model/card/flash_card.dart';
import 'package:mobile_deckz/model/card/input_card.dart';
import 'package:mobile_deckz/model/deck/tag.dart';

import '../card/mcq_card.dart';

class Deck {
  final String id;
  final String name;
  final String color;
  final String image;
  final String score;
  final String cardCount;
  final List<Tag> tags;
  List<dynamic> cards = [];

  Deck(
      {required this.id,
        required this.name,
        required this.color,
        required this.image,
        required this.score,
        required this.cardCount,
        required this.tags});

  factory Deck.fromJson(Map<String, dynamic> json) {
    List<Tag> tags = [];
    for (var tag in json['tags']) {
      tags.add(Tag.fromJson(tag));
    }
    return Deck(
      id: json['id'],
      name: json['name'],
      color: json['color'],
      image: json['image'],
      score: json['score'],
      cardCount: json['cardCount'].toString(),
      tags: tags,
    );
  }

  void loadCards(Map<String, dynamic> json) {
    for (var card in json['cards']) {
      switch (card['cardType']) {
        case 'FlashCard':
          cards.add(FlashCard.fromJson(card));
          break;
        case 'MCQCard':
          cards.add(MCQCard.fromJson(card));
          break;
        case 'InputCard':
          cards.add(InputCard.fromJson(card));
          break;
      }
    }
  }

}