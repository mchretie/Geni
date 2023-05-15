import 'package:collection/collection.dart';

class IndulgentValidator {
  String removeDiacritics(String str) {
    var withDia = 'ÀÁÂÃÄÅàáâãäåÒÓÔÕÕÖØòóôõöøÈÉÊËèéêëðÇçÐÌÍÎÏìíîïÙÚÛÜùúûüÑñŠšŸÿýŽž';
    var withoutDia = 'AAAAAAaaaaaaOOOOOOOooooooEEEEeeeeeCcDIIIIiiiiUUUUuuuuNnSsYyyZz';
    for (int i = 0; i < withDia.length; i++) {
      str = str.replaceAll(withDia[i], withoutDia[i]);
    }
    return str;

  }

  String removeDeterminers(String text) {
    final determiners = [
      'le ',
      'la ',
      "l'",
      'les ',
      'de ',
      'du ',
      'des ',
    ];

    for (final determiner in determiners) {
      text = text.replaceAll(determiner, '');
    }
    return text;
  }

  String addTolerance(String text) {
    text = text.toLowerCase();
    text = removeDiacritics(text);
    text = removeDeterminers(text);
    return text;
  }

  bool isEquals(String text1, String text2) {
    final cardAnswerWithTol = addTolerance(text1);
    final userAnswerWithTol = addTolerance(text2);

    return const ListEquality()
        .equals(cardAnswerWithTol.split(' '), userAnswerWithTol.split(' '));
  }
}
