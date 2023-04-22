import 'card.dart';

class InputCard extends Card {
  final String answer;

  InputCard({required this.answer, required String id, required String front})
      : super(id: id, cardType: "InputCard", front: front);
}