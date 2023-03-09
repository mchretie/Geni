package ulb.infof307.g01.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardExtractorByKnowledge implements CardExtractor {

    final private List<Card> sortedCards;
    private Integer currentCardIndex; // to know where we are in the deck during the drawing

    public CardExtractorByKnowledge(Deck deck) {
        this.sortedCards = new ArrayList<>(deck.getCards());
        this.currentCardIndex = -1;
        this.sortDeck();
    }

    private void sortDeck() {
        Collections.sort(this.sortedCards);
    }

    public List<Card> getSortedCards() {
        return sortedCards;
    }

    @Override
    public Card getNextCard() {
        this.currentCardIndex += 1;

        if (this.currentCardIndex == this.sortedCards.size()) {
            this.currentCardIndex = 0;
            this.sortDeck();

        }
        return this.sortedCards.get(currentCardIndex);
    }
}
