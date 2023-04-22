import 'package:mobile_deckz/model/deck/tag.dart';

class Deck {
  final String id;
  final String name;
  final String color;
  final String image;
  final String cardCount;
  final List<Tag> tags;

  Deck(
      {required this.id,
        required this.name,
        required this.color,
        required this.image,
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
      cardCount: json['cardCount'].toString(),
      tags: tags,
    );
  }
}