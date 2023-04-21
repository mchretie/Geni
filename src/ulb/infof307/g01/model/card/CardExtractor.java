package ulb.infof307.g01.model.card;

import ulb.infof307.g01.model.deck.Deck;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

abstract public class CardExtractor implements Iterable<Card> {

    protected final List<Card> sortedCards;
    private int currentCardIndex;

    public CardExtractor(Deck deck) {
        this.sortedCards = new ArrayList<>(deck.getCards());
        this.currentCardIndex = -1;
        this.sortDeck();
    }

    abstract void sortDeck();

    public int getCurrentCardIndex() {
        return currentCardIndex;
    }

    public int getNumberOfRemainingCards() {
        return currentCardIndex == -1 ? sortedCards.size() : sortedCards.size() - currentCardIndex-1;
    }

    public Card getNextCard() {
        return getNumberOfRemainingCards() == 0 ? null : this.sortedCards.get(++currentCardIndex);
    }

    public Card getPreviousCard() {
        return currentCardIndex > 0 ? this.sortedCards.get(--currentCardIndex) : null;
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
