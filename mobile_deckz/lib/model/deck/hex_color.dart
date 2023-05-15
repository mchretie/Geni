import 'dart:ui';

class HexColor extends Color {
  static int _getColorFromHex(String hexColor) {
    hexColor = hexColor.toUpperCase().replaceAll("#", "");
    if (hexColor.length == 8) {
      hexColor = hexColor.substring(0, hexColor.length - 2);
    }
    if (hexColor.length == 6) {
      hexColor = "FF$hexColor";
    }
    return int.parse(hexColor, radix: 16);
  }

  static bool isDark(String hexColor) {
    final color = _getColorFromHex(hexColor);
    final darkness = 1 - (0.299 * Color(color).red + 0.587 * Color(color).green + 0.114 * Color(color).blue) / 255;
    return darkness >= 0.5;
  }

  HexColor(final String hexColor) : super(_getColorFromHex(hexColor));
}