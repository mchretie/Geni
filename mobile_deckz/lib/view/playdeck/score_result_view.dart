import 'package:flutter/material.dart';
import 'package:pie_chart/pie_chart.dart' as pc;
import 'package:fl_chart/fl_chart.dart';

import '../../model/deck/score.dart';

class ScoreResultView extends StatelessWidget {
  final Score score;

  const ScoreResultView({Key? key, required this.score}) : super(key: key);

  List<BarChartGroupData> responseTimeBars() {
    List<BarChartGroupData> timings = [];
    for (MapEntry<int, int> entry in score.responseTimes().entries) {
      timings.add(BarChartGroupData(x: entry.key, barRods: [
        BarChartRodData(
          toY: entry.value.toDouble(),
        )
      ]));
    }
    timings.sort((a, b) => a.x.compareTo(b.x));
    return timings;
  }

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
          child: const Text('Go back home')),
      const SizedBox(height: 20),
      const Divider(),
      const SizedBox(height: 10),
      Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: const [
          Text('Statistiques', style: TextStyle(fontSize: 24)),
        ],
      ),
      const SizedBox(height: 20),
      Expanded(
          child: Row(children: [
        Expanded(
            child: pc.PieChart(
                dataMap: {
              'Correct': score.correctAnswers.toDouble(),
              'Incorrect': score.incorrectAnswers.toDouble()
            },
                colorList: [
              Colors.green,
              Colors.red
            ],
                legendOptions: const pc.LegendOptions(
                  legendPosition: pc.LegendPosition.bottom,
                ))),
        Expanded(
            flex: 1,
            child: BarChart(
              BarChartData(barGroups: responseTimeBars()),
              swapAnimationDuration: Duration(milliseconds: 150), // Optional
              swapAnimationCurve: Curves.linear, // Optional
            )),
      ])),
    ]));
  }
}
