import 'package:mobile_deckz/model/deck/deck.dart';
import 'package:mobile_deckz/model/deck/tag.dart';

class MarketplaceDeck extends Deck {
  String owner;

  MarketplaceDeck(
      {required this.owner,
      required String id,
      required String name,
      required String color,
      required String image,
      required String score,
      required String cardCount,
      required List<Tag> tags})
      : super(
            id: id,
            name: name,
            color: color,
            image: image,
            score: score,
            cardCount: cardCount,
            tags: tags);

  factory MarketplaceDeck.fromJson(Map<String, dynamic> json) {
    List<Tag> tags = [];
    for (var tag in json['tags']) {
      tags.add(Tag.fromJson(tag));
    }
    return MarketplaceDeck(
      owner: json['owner'],
      id: json['id'],
      name: json['name'],
      color: json['color'],
      image: json['image'],
      score: json['score'],
      cardCount: json['cardCount'].toString(),
      tags: tags,
    );
  }
}
