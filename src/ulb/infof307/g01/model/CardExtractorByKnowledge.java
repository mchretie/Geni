package ulb.infof307.g01.model;

import java.util.Comparator;

public class CardExtractorByKnowledge extends CardExtractor {

    public CardExtractorByKnowledge(Deck deck) {
        super(deck);
    }
    @Override
    void sortDeck() {
        Comparator<Card> cardComparatorByKnowledge = (card1, card2) -> Integer.compare(card1.getKnowledge().getValue(),
                card2.getKnowledge().getValue());
        this.sortedCards.sort(cardComparatorByKnowledge);
    }
}
