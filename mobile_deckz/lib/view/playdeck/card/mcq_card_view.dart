import 'package:flutter/material.dart';
import 'package:mobile_deckz/model/card/mcq_card.dart';
import 'package:mobile_deckz/view/playdeck/card/countdown_view.dart';
import 'package:mobile_deckz/view/playdeck/card/front_card_view.dart';

class MCQCardView extends StatefulWidget {
  final MCQCard card;

  const MCQCardView({super.key, required this.card});

  @override
  State<MCQCardView> createState() => _MCQCardViewState();
}

class _MCQCardViewState extends State<MCQCardView> {
  bool isAnswered = false;
  int answerIndex = 0;

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
                      setState(() {
                        isAnswered = true;
                        answerIndex = index;
                      });
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
              CountdownView(seconds: 10, onCountdownFinished: () {}),
            ]),
    ]);
  }
}
