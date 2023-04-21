import 'package:flutter/material.dart';

import 'package:mobile_deckz/http_dao/leaderboard_dao.dart';

class LeaderboardView extends StatefulWidget {
  const LeaderboardView({super.key});

  @override
  State<LeaderboardView> createState() => _LeaderboardViewState();
}

class _LeaderboardViewState extends State<LeaderboardView> {
  @override
  Widget build(BuildContext context) {
    final Future<Leaderboard> leaderboard =
        LeaderboardDao.getGlobalLeaderboard();

    return FutureBuilder<Leaderboard>(
        future: leaderboard,
        builder: (BuildContext context, AsyncSnapshot<Leaderboard> snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return const Text('An error occurred while loading the leaderboard',
                style: TextStyle(color: Colors.red));
          } else {
            List<Score> scores = snapshot.data?.getLeaderboard() ?? [];
            Score userScore = snapshot.data?.getUserScore() ??
                Score(username: '', score: 'N/A', rank: 'N/A');
            return Scaffold(
              body: Column(
                children: [
                  Padding(
                    padding: const EdgeInsets.all(16),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        const Text(
                          'Your Rank:',
                          style: TextStyle(
                            fontSize: 16,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        Text(
                          userScore.rank,
                          style: const TextStyle(
                            fontSize: 24,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        const SizedBox(height: 20),
                        const Text(
                          'Your Score:',
                          style: TextStyle(
                            fontSize: 16,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        Text(
                          userScore.score,
                          style: const TextStyle(
                            fontSize: 24,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ],
                    ),
                  ),
                  const Divider(),
                  Expanded(
                    child: ListView.builder(
                      itemCount: scores.length,
                      itemBuilder: (context, index) {
                        final score = scores[index];
                        return ListTile(
                            leading: Text(score.rank),
                            title: Text(score.username),
                            trailing:
                                Row(mainAxisSize: MainAxisSize.min, children: [
                              const Icon(Icons.emoji_events),
                              Text(score.score),
                            ]));
                      },
                    ),
                  ),
                ],
              ),
            );
          }
        });
  }
}
