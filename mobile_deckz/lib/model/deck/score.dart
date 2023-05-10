import 'dart:math';

class Answer {
  final bool correctlyAnswered;
  final num remainingTime;
  final num totalTime;

  Answer(this.correctlyAnswered, this.remainingTime, this.totalTime);
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

  bool get isFinal => _final;

  void recordAnswer(Answer answer) {
    _answers.add(answer);
    _computeScore();
  }

  void setFinal() {
    _final = true;
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
