import 'package:flutter/material.dart';

class CountdownView extends StatefulWidget {
  final int seconds;
  final Function onCountdownFinished;

  const CountdownView(
      {Key? key, required this.seconds, required this.onCountdownFinished})
      : super(key: key);

  @override
  _CountdownViewState createState() => _CountdownViewState();
}

class _CountdownViewState extends State<CountdownView> {
  @override
  Widget build(BuildContext context) {
    return TweenAnimationBuilder<double>(
      duration: const Duration(seconds: 10),
      curve: Curves.easeInOut,
      tween: Tween<double>(
        begin: 1,
        end: 0,
      ),
      builder: (context, value, _) =>
          LinearProgressIndicator(
              value: value,
              color: (value > 0.5) ? Colors.green : Colors.red,
              minHeight: 10,
          ),
    );
  }
}
