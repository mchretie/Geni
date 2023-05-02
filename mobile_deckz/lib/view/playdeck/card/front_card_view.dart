import 'package:flutter/material.dart';
import 'package:flutter_html/flutter_html.dart';

class FrontCardView extends StatelessWidget {
  final String text;

  const FrontCardView({super.key, required this.text});

  @override
  Widget build(BuildContext context) {
    return Card(
      elevation: 4,
      child: SizedBox(
          height: 200,
          width: 500,
          child: Center(
            child: Html(
              data: text,
            ),
          )),
    );
  }
}