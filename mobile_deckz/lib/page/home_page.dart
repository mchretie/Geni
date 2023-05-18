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

  late final GlobalKey<MarketPlaceViewState> _MarketPlaceViewKey ;
  late final GlobalKey<UserDeckViewState> _UserDeckViewKey ;
  late final GlobalKey<LeaderboardViewState> _LeaderboardViewKey ;

  late final MarketPlaceView _marketPlaceView ;
  late final LeaderboardView _leaderboardView ;
  late final UserDeckView _userDeckView  ;

  final Future<bool> isLoggedIn = AuthDao.isLoggedIn();

  late StatefulWidget _currentWidget;


  @override
  void initState() {
    super.initState();

    _MarketPlaceViewKey = GlobalKey();
    _UserDeckViewKey = GlobalKey();
    _LeaderboardViewKey = GlobalKey();

    _marketPlaceView= MarketPlaceView();
    _leaderboardView =  LeaderboardView();
    _userDeckView = UserDeckView(key: _UserDeckViewKey);

    print("calling switch from init");
    _switchToCurrentWidget();

  }

  @override
  void didUpdateWidget(HomePage oldWidget) {
    super.didUpdateWidget(oldWidget);
    //_fetchDataFor_currentWidget();
    print("did update called in home");
  }

  /*Future<void> _fetchDataFor_currentWidget() async {
    switch (__currentWidgetIndex) {
      case 0:
        _marketPlaceView.createState();
        break;
      case 1:
        _userDeckView.fetchData();
        break;
      case 2:
        _leaderboardView.fetchData();
        break;
    }
  }*/
  
    Future<void> _switchToCurrentWidget() async {
    switch (_currentWidgetIndex) {
      case 0:
        _currentWidget = _marketPlaceView;
        break;
      case 1:
        _currentWidget = _userDeckView;
        print("switching to deckview and calling reload now");
        print("deck view state : " );
        print( _UserDeckViewKey.currentState==null );
        //_UserDeckViewKey.currentState!.reloadDecks();
        break;
      case 2:
        _currentWidget = _leaderboardView;
        break;

    }}

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
