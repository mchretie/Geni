package ulb.infof307.g01.model;

import java.util.Collections;

public class CardExtractorRandom extends CardExtractor {

    public CardExtractorRandom(Deck deck) {
        super(deck);
    }
    @Override
    void sortDeck() {
        Collections.shuffle(this.sortedCards);
    }
}
