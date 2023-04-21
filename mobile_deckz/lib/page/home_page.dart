import 'package:flutter/material.dart';
import 'package:mobile_deckz/page/auth_page.dart';
import 'package:mobile_deckz/view/leaderboard_view.dart';
import 'package:mobile_deckz/page/profile_page.dart';
import 'package:mobile_deckz/view/store_view.dart';
import 'package:mobile_deckz/view/user_deck_view.dart';

import '../http_dao/auth_dao.dart';

class HomeView extends StatefulWidget {
  const HomeView({super.key, required this.title});

  final String title;

  @override
  State<HomeView> createState() => _HomeViewState();
}

class _HomeViewState extends State<HomeView> {
  int _selectedIndex = 0;

  final Future<bool> isLoggedIn = AuthDao.isLoggedIn();

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
                  child: IndexedStack(
                index: _selectedIndex,
                children: const [
                  UserDeckView(),
                  StoreView(),
                  LeaderboardView(),
                ],
              )),
              bottomNavigationBar: BottomNavigationBar(
                items: const <BottomNavigationBarItem>[
                  BottomNavigationBarItem(
                    icon: Icon(Icons.home),
                    label: 'Home',
                  ),
                  BottomNavigationBarItem(
                    icon: Icon(Icons.store),
                    label: 'Store',
                  ),
                  BottomNavigationBarItem(
                    icon: Icon(Icons.leaderboard),
                    label: 'Leaderboard',
                  ),
                ],
                currentIndex: _selectedIndex,
                selectedItemColor: Colors.purple[800],
                onTap: (int index) {
                  setState(() {
                    _selectedIndex = index;
                  });
                },
              ));
          ;
        }
      },
    );
  }

// @override
// Widget build(BuildContext context) {
//   return Scaffold(
//       appBar: AppBar(
//         title: Text(widget.title),
//         actions: [
//           IconButton(
//             icon: const Icon(Icons.account_circle),
//             tooltip: 'Profile',
//             onPressed: () {
//               Navigator.of(context).push(
//                 MaterialPageRoute(
//                   builder: (context) => const ProfileView(),
//                 ),
//               );
//             },
//           ),
//         ],
//       ),
//       body: Center(
//           child: IndexedStack(
//         index: _selectedIndex,
//         children: const [
//           UserDeckView(),
//           StoreView(),
//           LeaderboardView(),
//         ],
//       )),
//       bottomNavigationBar: BottomNavigationBar(
//         items: const <BottomNavigationBarItem>[
//           BottomNavigationBarItem(
//             icon: Icon(Icons.home),
//             label: 'Home',
//           ),
//           BottomNavigationBarItem(
//             icon: Icon(Icons.store),
//             label: 'Store',
//           ),
//           BottomNavigationBarItem(
//             icon: Icon(Icons.leaderboard),
//             label: 'Leaderboard',
//           ),
//         ],
//         currentIndex: _selectedIndex,
//         selectedItemColor: Colors.purple[800],
//         onTap: (int index) {
//           setState(() {
//             _selectedIndex = index;
//           });
//         },
//       ));
// }
}
