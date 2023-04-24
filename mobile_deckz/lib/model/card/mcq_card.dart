import 'abstract_card.dart';

class MCQCard extends AbstractCard {
  final List<String> choices;
  final int correctChoice;

  MCQCard(
      {required this.choices,
        required this.correctChoice,
        required String id,
        required String front})
      : super(id: id, cardType: "MCQCard", front: front);

  factory MCQCard.fromJson(Map<String, dynamic> json) {
    return MCQCard(
      id: json['id'],
      front: json['front'],
      choices: List<String>.from(json['choices']),
      correctChoice: json['correctChoice'],
    );
  }
}
