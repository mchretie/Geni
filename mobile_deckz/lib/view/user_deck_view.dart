import 'package:flutter/material.dart';

class UserDeckView extends StatefulWidget {
  const UserDeckView({super.key});

  @override
  State<UserDeckView> createState() => _UserDeckViewState();
}

class _UserDeckViewState extends State<UserDeckView> {
  @override
  Widget build(BuildContext context) {
    return const Scaffold(
      body: Center(
        child: Text('User Deck'),
      ),
    );
  }
}