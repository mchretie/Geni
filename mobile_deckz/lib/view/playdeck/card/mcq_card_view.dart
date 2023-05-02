import 'package:flutter/material.dart';
import 'package:mobile_deckz/model/card/mcq_card.dart';
import 'package:mobile_deckz/model/deck/score.dart';
import 'package:mobile_deckz/view/playdeck/card/countdown_view.dart';
import 'package:mobile_deckz/view/playdeck/card/front_card_view.dart';

class MCQCardView extends StatefulWidget {
  final MCQCard card;
  final Score score;

  const MCQCardView({super.key, required this.card, required this.score});

  @override
  State<MCQCardView> createState() => _MCQCardViewState();
}

class _MCQCardViewState extends State<MCQCardView> with AutomaticKeepAliveClientMixin<MCQCardView> {
  bool isAnswered = false;
  int answerIndex = 0;
  double remainingTimeValue = 1;

  @override
  void initState() {
    super.initState();
  }

  void _submitScore() {
    if (widget.card.correctChoice == answerIndex) {
      widget.score.incrementScore(remainingTimeValue);
    }
  }

  void _timeUp() {
    setState(() {
      isAnswered = true;
      answerIndex = -1;
    });
  }

  void _updateRemainingTime(double remainingTime) {
    remainingTimeValue = remainingTime;
    if (remainingTimeValue <= 0) {
      _timeUp();
    }
  }

  @override
  Widget build(BuildContext context) {
    return Column(children: [
      FrontCardView(
        text: widget.card.front,
      ),
      const SizedBox(height: 10),
      isAnswered
          ? SizedBox(
              height: 150,
              child: GridView.builder(
                physics: const NeverScrollableScrollPhysics(),
                shrinkWrap: true,
                gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                  crossAxisCount: 2,
                  crossAxisSpacing: 5.0,
                  mainAxisSpacing: 5.0,
                  childAspectRatio: 3.0,
                ),
                itemCount: widget.card.choices.length,
                itemBuilder: (context, index) {
                  return ElevatedButton(
                    onPressed: () {
                      if (!isAnswered) {
                        setState(() {
                          isAnswered = true;
                          answerIndex = index;
                          _submitScore();
                        });
                      }
                    },
                    style: ElevatedButton.styleFrom(
                      primary: widget.card.correctChoice == index
                          ? Colors.green
                          : (answerIndex == index ? Colors.red : Colors.grey),
                      onPrimary: Colors.white,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(10.0),
                        side: const BorderSide(color: Colors.grey, width: 1.0),
                      ),
                      minimumSize: const Size(double.infinity, 50),
                    ),
                    child: Center(
                      child: Text(
                        widget.card.choices[index],
                        style: const TextStyle(color: Colors.white),
                      ),
                    ),
                  );
                },
              ))
          : Column(children: [
              SizedBox(
                  height: 150,
                  child: GridView.builder(
                    physics: const NeverScrollableScrollPhysics(),
                    shrinkWrap: true,
                    gridDelegate:
                        const SliverGridDelegateWithFixedCrossAxisCount(
                      crossAxisCount: 2,
                      crossAxisSpacing: 5.0,
                      mainAxisSpacing: 5.0,
                      childAspectRatio: 3.0,
                    ),
                    itemCount: widget.card.choices.length,
                    itemBuilder: (context, index) {
                      return ElevatedButton(
                        onPressed: () {
                          setState(() {
                            isAnswered = true;
                            answerIndex = index;
                            _submitScore();
                          });
                        },
                        style: ElevatedButton.styleFrom(
                          primary: Colors.purple,
                          onPrimary: Colors.white,
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(10.0),
                            side: const BorderSide(
                                color: Colors.grey, width: 1.0),
                          ),
                          minimumSize: const Size(double.infinity, 50),
                        ),
                        child: Center(
                          child: Text(
                            widget.card.choices[index],
                            style: const TextStyle(color: Colors.white),
                          ),
                        ),
                      );
                    },
                  )),
              CountdownView(
                seconds: widget.card.countdownTime,
                onCountdownUpdated: _updateRemainingTime,
              ),
            ]),
    ]);
  }

  @override
  bool get wantKeepAlive => true;
}
