import 'package:flutter/material.dart';
import 'package:mobile_deckz/http_dao/auth_dao.dart';
import 'package:mobile_deckz/page/game_history_page.dart';
import 'package:mobile_deckz/page/home_page.dart';

class ProfileView extends StatefulWidget {
  const ProfileView({super.key});

  @override
  State<ProfileView> createState() => _ProfileViewState();
}

class _ProfileViewState extends State<ProfileView> {
  String username = '';

  @override
  Widget build(BuildContext context) {
    AuthDao.getUsername().then((value) => setState(() {
          username = value;
        }));

    return Scaffold(
      appBar: AppBar(
        title: const Text('Profile'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Icon(
              Icons.account_circle,
              size: 100,
            ),
            const SizedBox(height: 20),
            Text(
              'Welcome, $username!',
              style: const TextStyle(
                fontSize: 24,
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => const GameHistoryPage()),
                );
              },
              child: Row(
                mainAxisSize: MainAxisSize.min,
                mainAxisAlignment: MainAxisAlignment.center,
                children: const [
                  Text('Game History'),
                  SizedBox(width: 10),
                  Icon(Icons.history),
                ],
              ),
            ),
            const SizedBox(height: 20),
            ElevatedButton(
              onPressed: () {
                AuthDao.logout().then((value) {
                  Navigator.popUntil(context, (route) => false);
                  Navigator.of(context).push(MaterialPageRoute(
                    builder: (context) => const HomePage(),
                  ));
                });
              },
              child: const Text('Logout'),
            ),
          ],
        ),
      ),
    );
  }
}
