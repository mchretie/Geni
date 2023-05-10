import 'package:flutter/material.dart';

import '../../model/deck/score.dart';

class ScoreResultView extends StatelessWidget {
  final Score score;

  const ScoreResultView({Key? key, required this.score}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Center(
        child: Column(children: [
      Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          const Text('Votre score : ', style: TextStyle(fontSize: 20)),
          Text(score.score.toString(),
              style:
                  const TextStyle(fontWeight: FontWeight.bold, fontSize: 20)),
        ],
      ),
      Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          const Text('Temps moyen : ', style: TextStyle(fontSize: 20)),
          Text(score.averageResponseTime.toStringAsFixed(2),
              style:
                  const TextStyle(fontWeight: FontWeight.bold, fontSize: 20)),
          const Text(' s',
              style: TextStyle(fontWeight: FontWeight.bold, fontSize: 20)),
        ],
      ),
      Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          const Text('Temps total : ', style: TextStyle(fontSize: 20)),
          Text(score.totalResponseTime.toStringAsFixed(2),
              style:
                  const TextStyle(fontWeight: FontWeight.bold, fontSize: 20)),
          const Text(' s',
              style: TextStyle(fontWeight: FontWeight.bold, fontSize: 20)),
        ],
      ),
      const SizedBox(height: 20),
      ElevatedButton(
          onPressed: () => Navigator.pop(context),
          child: const Text('Go back home'))
    ]));
  }
}
