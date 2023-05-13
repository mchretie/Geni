import 'package:flutter/material.dart';
import 'package:mobile_deckz/model/card/input_card.dart';
import 'package:mobile_deckz/model/deck/score.dart';

import 'countdown_view.dart';
import 'front_card_view.dart';

class InputCardView extends StatefulWidget {
  final InputCard card;
  final Score score;
  final Function onCardAnswered;
  final Function onCardLoaded;

  const InputCardView(
      {super.key,
      required this.card,
      required this.score,
      required this.onCardAnswered,
      required this.onCardLoaded});

  @override
  State<InputCardView> createState() => _InputCardViewState();
}

class _InputCardViewState extends State<InputCardView>
    with AutomaticKeepAliveClientMixin<InputCardView> {
  bool isAnswered = false;
  String answer = '';
  double remainingTimeValue = 1;

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      widget.onCardLoaded();
    });
  }

  void handleAnswer() {
    if (!isAnswered) {
      widget.onCardAnswered();
      setState(() {
        isAnswered = true;
      });
    }
    _submitScore();
  }

  void _submitScore() {
    final correct = widget.card.isUserAnswerValid(answer);
    final remTime = remainingTimeValue;
    final totTime = widget.card.countdownTime;
    final answer_ = Answer(correct, remTime, totTime);
    widget.score.recordAnswer(answer_);
  }

  void _updateRemainingTime(double remainingTime) {
    remainingTimeValue = remainingTime;
    if (remainingTimeValue <= 0) {
      handleAnswer();
    }
  }

  @override
  Widget build(BuildContext context) {
    super.build(context);
    return Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
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
          : Column(
              children: [
                Row(
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
                        onPressed: handleAnswer,
                        child: const Icon(Icons.check),
                      ),
                    )
                  ],
                ),
                const SizedBox(height: 10),
                CountdownView(
                  seconds: widget.card.countdownTime,
                  onCountdownUpdated: _updateRemainingTime,
                ),
              ],
            )
    ]);
  }

  @override
  bool get wantKeepAlive => true;
}
