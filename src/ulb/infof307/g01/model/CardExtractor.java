package ulb.infof307.g01.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

abstract public class CardExtractor implements Iterable<Card> {

    final List<Card> sortedCards;

    public CardExtractor(Deck deck) {
        this.sortedCards = new ArrayList<>(deck.getCards());
        this.sortDeck();
    }

    abstract void sortDeck();

    public List<Card> getSortedCards() {
        return sortedCards;
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
