import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:http/http.dart' as http;

class HttpDao {
  static const storage = FlutterSecureStorage();

  static String authHeader = "Authorization";

  static Future<http.Response> get(Uri path) async {
    String? token = await storage.read(key: 'token');

    if (token != null) {
      final http.Response response =
          await http.get(path, headers: {authHeader: token});
      return response;
    }

    return http.Response('{"error": "No token"}', 401);
  }


  static Future<http.Response> post(Uri path, String body) async {
    String? token = await storage.read(key: 'token');

    if (token != null) {
      final http.Response response =
      await http.post(path, headers: {authHeader: token}, body: body);
      return response;
    }

    return http.Response('{"error": "No token"}', 401);
  }
}
