import 'package:flutter/cupertino.dart';
import 'package:http/http.dart' as http;
import 'package:mobile_deckz/http_dao/http_dao.dart';
import 'package:mobile_deckz/http_dao/server_path.dart';

//import '../model/leaderboard/leaderboard.dart';
import '../model/leaderboard/score.dart';

class ScoreDAO{

  static Future<void> addScore(Score score) async {
    final http.Response response = await HttpDao.post(
        Uri.parse('${ServerPath.saveScorePath}'), "score json ?");
    if (response.statusCode == 200) {
      debugPrint('Score added to Deck');
    }
  }
}

