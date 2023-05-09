class Score {
  int _score;
  bool _final = false;

  Score(this._score);

  int get score => _score;

  void incrementScore(double remainingTime) {
    _score += (1000 * remainingTime).round();
  }

  void setFinal() {
    _final = true;
  }

  bool get isFinal => _final;
}
