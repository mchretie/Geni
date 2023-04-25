import 'package:mobile_deckz/model/card/indulgent_validator.dart';

import 'abstract_card.dart';

class InputCard extends AbstractCard {
  final String answer;

  InputCard({required this.answer, required String id, required String front})
      : super(id: id, cardType: "InputCard", front: front);

  bool isUserAnswerValid(String userAnswer){
    return IndulgentValidator().isEquals(answer, userAnswer);
  }

  factory InputCard.fromJson(Map<String, dynamic> json) {
    return InputCard(
      id: json['id'],
      front: json['front'],
      answer: json['answer'],
    );
  }
}