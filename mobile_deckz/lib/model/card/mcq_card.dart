import 'card.dart';

class MCQCard extends Card {
  final String back;
  final List<String> options;
  final int correctOption;

  MCQCard(
      {required this.back,
        required this.options,
        required this.correctOption,
        required String id,
        required String front})
      : super(id: id, cardType: "MCQCard", front: front);
}
