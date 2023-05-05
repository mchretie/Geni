class IndulgentValidator {
  final List<String> _determiners = [
    "le",
    "la",
    "l'",
    "les",
    "de",
    "du",
    "des"
  ];

  final diacritics =
      'ÀÁÂÃÄÅàáâãäåÒÓÔÕÕÖØòóôõöøÈÉÊËĚèéêëěðČÇçčÐĎďÌÍÎÏìíîïĽľÙÚÛÜŮùúûüůŇÑñňŘřŠšŤťŸÝÿýŽž';
  final nonDiacritics =
      'AAAAAAaaaaaaOOOOOOOooooooEEEEEeeeeeeCCccDDdIIIIiiiiLlUUUUUuuuuuNNnnRrSsTtYYyyZz';

  String _removeAccents(String text) {
    return text.replaceAll(RegExp('[$diacritics]'), (match) {
      var i = diacritics.indexOf(match);
      return nonDiacritics[i];
    } as String);}

  String _removeDeterminers(String text) {
    for (var determiner in _determiners) {
      text = text.replaceAll(determiner, '');
    }
    return text;
  }
  String addTolerance(String text) {
    text = text.toLowerCase();
    text = _removeAccents(text);
    text = _removeDeterminers(text);
    return text;
  }

  bool areEqual(String text1, String text2) {
    String cardAnswerWithTol = addTolerance(text1);
    String userAnswerWithTol = addTolerance(text2);
    return cardAnswerWithTol == userAnswerWithTol;
  }
}