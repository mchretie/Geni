import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class ScoreResultView extends StatelessWidget {
  final int finalScore;

  const ScoreResultView({Key? key, required this.finalScore}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Center(
        child: Column(children: [
      Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          const Text('Final Score: ', style: TextStyle(fontSize: 20)),
          Text(finalScore.toString(),
              style:
                  const TextStyle(fontWeight: FontWeight.bold, fontSize: 20)),
        ],
      ),
      const SizedBox(height: 20),
      ElevatedButton(
          onPressed: () => Navigator.pop(context),
          child: const Text('Go back home'))
    ]));
  }
}
