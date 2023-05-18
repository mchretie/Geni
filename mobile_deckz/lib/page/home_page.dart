import 'package:flutter/material.dart';
import 'package:mobile_deckz/page/auth_page.dart';
import 'package:mobile_deckz/view/leaderboard_view.dart';
import 'package:mobile_deckz/page/profile_page.dart';
import 'package:mobile_deckz/view/user_deck_view.dart';

import '../http_dao/auth_dao.dart';
import '../view/marketplace_view.dart';

class HomePage extends StatefulWidget {
  const HomePage({super.key});

  final String title = 'Mobile Deckz 3000';
  
  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  int _currentWidgetIndex = 1;

  late final MarketPlaceView _marketPlaceView ;
  late final LeaderboardView _leaderboardView ;
  late final UserDeckView _userDeckView  ;

  final Future<bool> isLoggedIn = AuthDao.isLoggedIn();

  late StatefulWidget _currentWidget;


  @override
  void initState() {
    super.initState();

    _marketPlaceView= const MarketPlaceView();
    _userDeckView = const UserDeckView();
    _leaderboardView =  const LeaderboardView();

    _switchToCurrentWidget();
  }

  Future<void> _switchToCurrentWidget() async {
    switch (_currentWidgetIndex) {
      case 0:
        _currentWidget = _marketPlaceView;
        break;
      case 1:
        _currentWidget = _userDeckView;
        break;
      case 2:
        _currentWidget = _leaderboardView;
        break;
      }
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<bool>(
      future: isLoggedIn,
      builder: (BuildContext context, AsyncSnapshot<bool> snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          return const CircularProgressIndicator();
        } else if (snapshot.hasError) {
          return Text('Error: ${snapshot.error}');
        } else {
          if (snapshot.data == false) {
            return const AuthPage();
          }
          return Scaffold(
              appBar: AppBar(
                title: Text(widget.title),
                actions: [
                  IconButton(
                    icon: const Icon(Icons.account_circle),
                    tooltip: 'Profile',
                    onPressed: () {
                      Navigator.of(context).push(
                        MaterialPageRoute(
                          builder: (context) => const ProfileView(),
                        ),
                      );
                    },
                  ),
                ],
              ),
              body: Center(
                  child: Container(
                    child: _currentWidget
                  )),
              bottomNavigationBar: BottomNavigationBar(
                items: const <BottomNavigationBarItem>[
                  BottomNavigationBarItem(
                    icon: Icon(Icons.store),
                    label: 'Store',
                  ),
                  BottomNavigationBarItem(
                    icon: Icon(Icons.home),
                    label: 'Home',
                  ),
                  BottomNavigationBarItem(
                    icon: Icon(Icons.leaderboard),
                    label: 'Leaderboard',
                  ),
                ],
                currentIndex: _currentWidgetIndex,
                selectedItemColor: Colors.purple[800],
                onTap: (int index) {
                  setState(() {
                    _currentWidgetIndex = index;
                    _switchToCurrentWidget();
                  });
                },
              ));
        }
      },
    );
  }
}
