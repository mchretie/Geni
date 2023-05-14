import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:mobile_deckz/page/home_page.dart';
import 'package:mobile_deckz/view/auth/register_view.dart';

import '../../http_dao/auth_dao.dart';

class LoginView extends StatefulWidget {
  const LoginView({super.key});

  @override
  State<LoginView> createState() => _LoginViewState();
}

class _LoginViewState extends State<LoginView> {
  String _username = '';
  String _password = '';
  String _errorMsg = '';

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Login'),
      ),
    body: Container(
      decoration: BoxDecoration(
        gradient: LinearGradient(
          begin: Alignment.topCenter,
          end: Alignment.bottomCenter,
          colors: [Colors.deepPurple.shade700, Colors.deepPurple.shade400],
        ),
      ),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          const Padding(
            padding: EdgeInsets.symmetric(horizontal: 32.0),
            child: Text(
              'Login',
              textAlign: TextAlign.center,
              style: TextStyle(
                color: Colors.white,
                fontSize: 32.0,
                fontWeight: FontWeight.bold,
              ),
            ),
          ),
          const SizedBox(height: 32.0),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 32.0),
            child: TextFormField(
              onChanged: (value) => setState(() => _username = value),
              style: const TextStyle(color: Colors.white),
              decoration: const InputDecoration(
                labelText: 'Username',
                labelStyle: TextStyle(color: Colors.white),
                enabledBorder: UnderlineInputBorder(
                  borderSide: BorderSide(color: Colors.white),
                ),
                focusedBorder: UnderlineInputBorder(
                  borderSide: BorderSide(color: Colors.white),
                ),
              ),
            ),
          ),
          const SizedBox(height: 16.0),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 32.0),
            child: TextFormField(
              onChanged: (value) => setState(() => _password = value),
              obscureText: true,
              style: const TextStyle(color: Colors.white),
              decoration: const InputDecoration(
                labelText: 'Password',
                labelStyle: TextStyle(color: Colors.white),
                enabledBorder: UnderlineInputBorder(
                  borderSide: BorderSide(color: Colors.white),
                ),
                focusedBorder: UnderlineInputBorder(
                  borderSide: BorderSide(color: Colors.white),
                ),
              ),
            ),
          ),
          const SizedBox(height: 32.0),
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              const Text(
                'Don\'t have an account?',
                style: TextStyle(color: Colors.white),
              ),
              TextButton(
                onPressed: () {
                  Navigator.of(context).push(
                    MaterialPageRoute(
                      builder: (context) => const RegisterView(),
                    ),
                  );
                },
                child: const Text(
                  'Register',
                  style: TextStyle(color: Colors.white),
                ),
              ),
            ],
          ),
          Text(_errorMsg,
              style: const TextStyle(color: Colors.deepOrange),
              textAlign: TextAlign.center),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 32.0),
            child: ElevatedButton(
              onPressed: () {
                if (_username.isEmpty) {
                  setState(() => _errorMsg = 'Username is required');
                  return;
                }
                if (_password.isEmpty) {
                  setState(() => _errorMsg = 'Password is required');
                  return;
                }

                AuthDao.login(_username, _password).then((value) {
                  Map<String, dynamic> result = json.decode(value.body);
                  if (result.containsKey("success") &&
                      result['success'] == true) {
                    Navigator.pop(context, true);
                    Navigator.of(context).push(
                      MaterialPageRoute(
                        builder: (context) => const HomePage(),
                      ),
                    );
                  } else {
                    setState(() => _errorMsg = "Login failed");
                  }
                }).catchError((error) {
                  setState(() => _errorMsg = error.toString());
                });
              },
              child: const Text('Sign in'),
            ),
          ),
        ],
      ),
    ),
    );
  }
}