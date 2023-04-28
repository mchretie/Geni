class Score {
  int _score;

  Score(this._score);

  int get score => _score;

  incrementScore(remainingTime) {
    _score = (1000 * remainingTime) as int;
  }
}
