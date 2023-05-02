import 'package:flutter/material.dart';

class CountdownView extends StatefulWidget {
  final int seconds;
  final Function onCountdownUpdated;

  const CountdownView(
      {Key? key, required this.seconds, required this.onCountdownUpdated})
      : super(key: key);

  @override
  State<CountdownView> createState() => _CountdownViewState();
}

class _CountdownViewState extends State<CountdownView> {
  @override
  Widget build(BuildContext context) {
    return TweenAnimationBuilder<double>(
      duration: Duration(seconds: widget.seconds),
      curve: Curves.easeInOut,
      tween: Tween<double>(
        begin: 1,
        end: 0,
      ),
      onEnd: () {
        widget.onCountdownUpdated(0.0);
      },
      builder: (context, value, _) {
        if (value > 0) widget.onCountdownUpdated(value * widget.seconds);
        return LinearProgressIndicator(
          value: value,
          color: (value > 0.6)
              ? Colors.green
              : (value > 0.3)
                  ? Colors.orange
                  : Colors.red,
          minHeight: 10,
        );
      },
    );
  }
}
