import 'package:http/http.dart' as http;
import 'package:mobile_deckz/http_dao/url_constant.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';


class AuthDao {
  static const storage = FlutterSecureStorage();

  static Future<http.Response> login(String email, String password) async {
    final http.Response response = await http.post(
      UrlConstant.loginUri,
      body: <String, String>{
        'email': email,
        'password': password,
      },
    );

    if (response.statusCode == 200){
      await storage.write(key: 'token', value: response.body);
    }

    return response;
  }

  static Future<http.Response> register(String email, String password) async {
    final http.Response response = await http.post(
      UrlConstant.registerUri,
      body: <String, String>{
        'email': email,
        'password': password,
      },
    );

    return response;
  }

  static Future<http.Response> isAuth() async {
    final http.Response response = await http.get(
      UrlConstant.loginUri,
    );

    return response;
  }
}