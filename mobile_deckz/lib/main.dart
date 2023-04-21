import 'package:flutter/material.dart';
import 'package:mobile_deckz/page/home_page.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Mobile Deckz 3000',
      theme: ThemeData(
        primarySwatch: Colors.deepPurple,
      ),
      home: const HomeView(title: 'Mobile Deckz 3000'),
    );
  }
}