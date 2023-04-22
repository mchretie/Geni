import 'card.dart';

class FlashCard extends Card {
  final String back;

  FlashCard({required this.back, required String id, required String front})
      : super(id: id, cardType: "FlashCard", front: front);
}