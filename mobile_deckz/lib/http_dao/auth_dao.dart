import 'package:http/http.dart' as http;
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:mobile_deckz/http_dao/server_path.dart';


class AuthDao {
  static const storage = FlutterSecureStorage();

  static Future<http.Response> login(String username, String password) async {
    final http.Response response = await http.post(
      ServerPath.loginPath,
      body: '{"username":$username, "password":$password}'
    );

    if (response.statusCode == 200){
      await storage.write(key: 'token', value: response.body);
      await storage.write(key: 'username', value: username);
    }

    return response;
  }

  static Future<http.Response> register(String username, String password) async {
    final http.Response response = await http.post(
      ServerPath.registerPath,
      body: '{"username":$username, "password":$password}'
    );

    return response;
  }

  static Future<void> logout() async {
    await storage.delete(key: 'token');
  }

  static Future<bool> isLoggedIn() async {
    if (await storage.read(key: 'token') != null) {
      return true;
    }
    return false;
  }

  static Future<String> getUsername() async {
    return await storage.read(key: 'username') ?? '';
  }
}