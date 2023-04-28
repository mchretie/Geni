import 'package:flutter/material.dart';

class MarketPlaceView extends StatefulWidget {
  const MarketPlaceView({super.key});

  @override
  State<MarketPlaceView> createState() => _MarketPlaceViewState();
}

class _MarketPlaceViewState extends State<MarketPlaceView> {
  @override
  Widget build(BuildContext context) {
    return const Scaffold(
      body: Center(
        child: Text('Store'),
      ),
    );
  }
}