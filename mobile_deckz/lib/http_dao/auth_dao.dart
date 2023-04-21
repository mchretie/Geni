import 'dart:ffi';

import 'package:http/http.dart' as http;
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:mobile_deckz/http_dao/server_path.dart';


class AuthDao {
  static const storage = FlutterSecureStorage();

  static Future<http.Response> login(String email, String password) async {
    final http.Response response = await http.post(
      ServerPath.loginPath,
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
      ServerPath.registerPath,
      body: <String, String>{
        'email': email,
        'password': password,
      },
    );

    return response;
  }

  static Future<bool> isLoggedIn() async {
    if (await storage.read(key: 'token') != null) {
      return true;
    }
    return false;
  }
}