import 'dart:math';

class Answer {
  final bool _correctlyAnswered;
  final num _remainingTime;
  final num _totalTime;

  bool get correctlyAnswered => _correctlyAnswered;
  num get remainingTime => _remainingTime;
  num get totalTime => _totalTime;

  Answer(this._correctlyAnswered, this._remainingTime, this._totalTime);
}

class Score {
  int _score;
  final _answers = [];

  final minScore = 0;
  final maxScore = 1000;

  bool _final = false;

  Score(this._score);

  int get score => _score;
  double get averageResponseTime => _averageResponseTime();
  double get totalResponseTime => _totalResponseTime();
  Map<String, double> get statCorrectAnswers => _statCorrectAnswers();

  bool get isFinal => _final;

  void recordAnswer(Answer answer) {
    _answers.add(answer);
    _computeScore();
  }

  void setFinal() {
    _final = true;
  }

  Map<String, double> _statCorrectAnswers() {
    var map = {'Correct': 0.0, 'Incorrect': 0.0};
    for (var answer in _answers) {
      if (answer.correctlyAnswered) {
        var value = map['Correct'] ?? 0;
        map['Correct'] = value + 1;
      } else {
        var value = map['Incorrect'] ?? 0;
        map['Incorrect'] = value + 1;
      }
    }
    return map;
  }

  double _averageResponseTime() {
    return _totalResponseTime() / _answers.length;
  }

  double _totalResponseTime() {
    return _answers.fold(
        0.0, (val, elem) => val + (elem.totalTime - elem.remainingTime));
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
