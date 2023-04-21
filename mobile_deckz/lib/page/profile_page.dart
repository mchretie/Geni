import 'package:flutter/material.dart';
import 'package:mobile_deckz/http_dao/auth_dao.dart';
import 'package:mobile_deckz/page/home_page.dart';

class ProfileView extends StatefulWidget {
  const ProfileView({super.key});

  @override
  State<ProfileView> createState() => _ProfileViewState();
}

class _ProfileViewState extends State<ProfileView> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Profile'),
      ),
      body: Column(
        children: [
          const Text('Profile'),
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
    );
  }
}
