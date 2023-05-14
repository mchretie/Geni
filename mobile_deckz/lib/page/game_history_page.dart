import 'package:flutter/material.dart';
import 'package:mobile_deckz/view/game_history_view.dart';


class GameHistoryPage extends StatefulWidget {
  const GameHistoryPage({super.key});

  @override
  State<GameHistoryPage> createState() => _GameHistoryPageState();
}

class _GameHistoryPageState extends State<GameHistoryPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Game History'),
      ),
      body: const Center(
        child: GameHistoryView(),
      ),
    );
  }
}