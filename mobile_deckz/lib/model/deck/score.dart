class Score {
  int _score;

  Score(this._score);

  int get score => _score;

  void incrementScore(double remainingTime) {
    _score = (1000 * remainingTime).round();
  }
}
