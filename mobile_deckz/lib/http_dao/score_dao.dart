import 'package:flutter/cupertino.dart';
import 'package:http/http.dart' as http;
import 'package:mobile_deckz/http_dao/http_dao.dart';
import 'package:mobile_deckz/http_dao/server_path.dart';
import '../model/deck/score.dart';

class ScoreDAO{

  static Future<void> addScore(Score score) async {
    final http.Response response = await HttpDao.post(
        Uri.parse('${ServerPath.saveScorePath}'), score.toJson());
  }
}

