import 'package:flutter/material.dart';
import 'package:pie_chart/pie_chart.dart' as pc;
import 'package:fl_chart/fl_chart.dart';
import 'dart:math';

import '../../model/deck/score.dart';

class ScoreResultView extends StatelessWidget {
  final Score score;

  const ScoreResultView({Key? key, required this.score}) : super(key: key);

  List<BarChartGroupData> responseTimeBars() {
    List<BarChartGroupData> timings = [];
    Map<int, int> responseTimes = score.responseTimes();
    int maximum = responseTimes.keys.fold(1, (a, b) => max(a, b));

    for (int i = 1; i <= maximum; ++i) {
      int value = responseTimes[i] ?? 0;
      timings.add(
        BarChartGroupData(
          x: i,
          barRods: [
            BarChartRodData(
              toY: value.toDouble(),
            ),
          ],
        ),
      );
    }

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
          child: const Text('Revenir à la page d’acceuil')),
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
            flex: 8,
            child: pc.PieChart(
                dataMap: {
                  'Réponses correctes': score.correctAnswers.toDouble(),
                  'Réponses incorrectes': score.incorrectAnswers.toDouble()
                },
                colorList: [
                  Colors.green,
                  Colors.red
                ],
                legendOptions: const pc.LegendOptions(
                  legendPosition: pc.LegendPosition.bottom,
                ))),
        const SizedBox(width: 10),
        Expanded(
            flex: 8,
            child: BarChart(
              BarChartData(
                barGroups: responseTimeBars(),
                gridData: FlGridData(
                  show: true,
                  drawHorizontalLine: true,
                  drawVerticalLine: true,
                  horizontalInterval: 1,
                  verticalInterval: 1,
                ),
                titlesData: FlTitlesData(
                  leftTitles: AxisTitles(
                      sideTitles: SideTitles(
                        showTitles: true,
                        interval: 1,
                      ),
                      axisNameWidget: Text('Nombre de réponses')),
                  topTitles: AxisTitles(
                    sideTitles: SideTitles(
                      showTitles: false,
                    ),
                  ),
                  rightTitles: AxisTitles(
                    sideTitles: SideTitles(
                      showTitles: false,
                    ),
                  ),
                  bottomTitles: AxisTitles(
                    sideTitles: SideTitles(
                      showTitles: true,
                      interval: 1,
                    ),
                    axisNameWidget: Text('Temps de réponse (s)'),
                  ),
                ),
              ),
              swapAnimationDuration: Duration(milliseconds: 150), // Optional
              swapAnimationCurve: Curves.linear, // Optional
            )),
      ])),
    ]));
  }
}
