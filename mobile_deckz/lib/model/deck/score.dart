import 'dart:math';

import '../../http_dao/auth_dao.dart';

class Answer {
  final bool _correctlyAnswered;
  final num _remainingTime;
  final num _totalTime;

  bool get correctlyAnswered => _correctlyAnswered;

  num get remainingTime => _remainingTime;

  num get totalTime => _totalTime;

  num get responseTime => _totalTime - _remainingTime;

  Answer(this._correctlyAnswered, this._remainingTime, this._totalTime);
}

class Score {
  late String _username;
  final String _deckId;

  int _score;
  final _answers = [];

  final minScore = 0;
  final maxScore = 1000;

  bool _final = false;

  Score(this._score, this._deckId) {
    AuthDao.getUsername().then((value) {
      _username = value;
    });
  }

  int get score => _score;

  double get averageResponseTime => _averageResponseTime();

  double get totalResponseTime => _totalResponseTime();

  int get correctAnswers => _correctAnswers();

  int get incorrectAnswers => _incorrectAnswers();

  bool get isFinal => _final;

  String toJson() {
    return '{"username":"$_username", "deckId":"$_deckId",'
        '"score":$_score,"times":${responseTimes().values.toList()},'
        '"scoreHistory":${_answers.map((e) => _scoreOf(e)).toList()}},'
        '"timestamp":${DateTime.now().millisecondsSinceEpoch}}';
  }

  void recordAnswer(Answer answer) {
    _answers.add(answer);
    _computeScore();
  }

  void setFinal() {
    _final = true;
  }

  Map<int, int> responseTimes() {
    Map<int, int> timings = {};
    for (Answer answer in _answers) {
      int ceilResponseTime = answer.responseTime.ceil();
      int currentValue = timings[ceilResponseTime] ?? 0;
      timings[ceilResponseTime] = currentValue + 1;
    }
    return timings;
  }

  int _correctAnswers() {
    return _answers.where((e) => e.correctlyAnswered).length;
  }

  int _incorrectAnswers() {
    return _answers.length - _correctAnswers();
  }

  double _averageResponseTime() {
    return _totalResponseTime() / _answers.length;
  }

  double _totalResponseTime() {
    return _answers.fold(0.0, (val, elem) => val + (elem.responseTime));
  }

  void _computeScore() {
    var score = _answers.fold(0, (val, elem) => val + _scoreOf(elem));
    _score = (score / _answers.length).round();
  }

  int _scoreOf(Answer answer) {
    var score = 0;
    if (answer.correctlyAnswered) {
      final unitRemainingTime = answer.totalTime / answer.remainingTime;
      final exponent = -8 * unitRemainingTime + 4;
      final toAdd = maxScore * (1 / (1 + exp(exponent)));
      score += toAdd.round();
    }
    return score;
  }
}
