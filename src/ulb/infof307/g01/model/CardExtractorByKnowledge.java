package ulb.infof307.g01.model;

import java.util.Comparator;

public class CardExtractorByKnowledge extends CardExtractor {

    public CardExtractorByKnowledge(Deck deck) {
        super(deck);
    }
    @Override
    void sortDeck() {
        Comparator<Card> cardComparatorByKnowledge = Comparator.comparingInt(card -> card.getKnowledge().getValue());
        this.sortedCards.sort(cardComparatorByKnowledge);
    }
}
