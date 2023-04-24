import 'card.dart';

class FlashCard extends Card {
  final String back;

  FlashCard({required this.back, required String id, required String front})
      : super(id: id, cardType: "FlashCard", front: front);

  factory FlashCard.fromJson(Map<String, dynamic> json) {
    return FlashCard(
      id: json['id'],
      front: json['front'],
      back: json['back'],
    );
  }
}