package ulb.infof307.g01.model;

import java.util.ArrayList;
import java.util.List;

abstract public class CardExtractor {

    final List<Card> sortedCards;
    private Integer currentCardIndex; // to know where we are in the deck during the drawing

    public CardExtractor(Deck deck) {
        this.sortedCards = new ArrayList<>(deck.getCards());
        this.currentCardIndex = -1;
        this.sortDeck();
    }

    abstract void sortDeck();

    public List<Card> getSortedCards() {
        return sortedCards;
    }

    public Card getNextCard() {
        this.currentCardIndex += 1;

        if (this.currentCardIndex == this.sortedCards.size()) {
            this.currentCardIndex = 0;
            this.sortDeck();

        }
        return this.sortedCards.get(currentCardIndex);
    }
}
