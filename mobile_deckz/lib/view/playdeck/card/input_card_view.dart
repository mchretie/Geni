import 'package:flutter/material.dart';
import 'package:mobile_deckz/model/card/input_card.dart';
import 'package:mobile_deckz/model/deck/score.dart';

import 'front_card_view.dart';

class InputCardView extends StatefulWidget {
  final InputCard card;
  final Score score;

  const InputCardView({super.key, required this.card, required this.score});

  @override
  State<InputCardView> createState() => _InputCardViewState();
}

class _InputCardViewState extends State<InputCardView> {
  bool isAnswered = false;
  String answer = '';

  void _submitScore() {
    if (widget.card.isUserAnswerValid(answer)) {
      widget.score.incrementScore(10);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Column(children: [
      FrontCardView(text: widget.card.front),
      const SizedBox(height: 10),
      isAnswered
          ? Column(
              children: [
                widget.card.isUserAnswerValid(answer)
                    ? const Text('Correct!')
                    : SizedBox(
                        width: 300,
                        height: 40,
                        child: Container(
                            padding: const EdgeInsets.all(8.0),
                            decoration: BoxDecoration(
                              color: Colors.red,
                              borderRadius: BorderRadius.circular(10.0),
                              border: Border.all(
                                color: Colors.grey,
                                width: 1.0,
                              ),
                            ),
                            child: Center(
                              child: Text(
                                answer,
                                style: const TextStyle(fontSize: 16.0),
                              ),
                            )),
                      ),
                const SizedBox(height: 5),
                SizedBox(
                  width: 300,
                  height: 40,
                  child: Container(
                      padding: const EdgeInsets.all(8.0),
                      decoration: BoxDecoration(
                        color: Colors.green,
                        borderRadius: BorderRadius.circular(10.0),
                        border: Border.all(
                          color: Colors.grey,
                          width: 1.0,
                        ),
                      ),
                      child: Center(
                        child: Text(
                          widget.card.answer,
                          style: const TextStyle(fontSize: 16.0),
                        ),
                      )),
                )
              ],
            )
          : Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                SizedBox(
                    width: 300,
                    child: TextField(
                      decoration: const InputDecoration(
                        border: OutlineInputBorder(),
                        labelText: 'Answer',
                      ),
                      onChanged: (text) {
                        setState(() {
                          answer = text;
                        });
                      },
                    )),
                SizedBox(
                  width: 60,
                  height: 60,
                  child: ElevatedButton(
                    onPressed: () {
                      setState(() {
                        isAnswered = true;
                      });
                    },
                    child: const Icon(Icons.check),
                  ),
                )
              ],
            ),
      const SizedBox(height: 85)
    ]);
  }
}
