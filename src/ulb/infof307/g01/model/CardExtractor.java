package ulb.infof307.g01.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

abstract public class CardExtractor implements Iterable<Card> {

    final List<Card> sortedCards;
    private int currentCardIndex; // to know where we are in the deck during the drawing

    public CardExtractor(Deck deck) {
        this.sortedCards = new ArrayList<>(deck.getCards());
        this.currentCardIndex = -1;
        this.sortDeck();
    }

    abstract void sortDeck();

    public List<Card> getSortedCards() {
        return sortedCards;
    }

    public int getNumberOfRemainingCards() {
        return currentCardIndex == -1 ? sortedCards.size() : sortedCards.size() - currentCardIndex-1;
    }

    public Card getNextCard() {
        if (getNumberOfRemainingCards() == 0) {
            return null;
        }

        return this.sortedCards.get(++currentCardIndex);
    }

    public Card getPreviousCard() {
        if (currentCardIndex <= 0) {
            return null;
        }
        return this.sortedCards.get(--currentCardIndex);
    }

    @Override
    public Iterator<Card> iterator() {
        return new Iterator<>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < sortedCards.size() && sortedCards.get(currentIndex) != null;
            }

            @Override
            public Card next() {
                return sortedCards.get(currentIndex++);
            }
        };
    }
}
