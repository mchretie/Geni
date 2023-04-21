class UrlConstant {
  static const String baseUrl = "http://localhost:8080";

  static Uri loginUri = Uri.parse("$baseUrl/login");
  static Uri registerUri = Uri.parse("$baseUrl/register");

}