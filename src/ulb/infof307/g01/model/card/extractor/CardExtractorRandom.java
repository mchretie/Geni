package ulb.infof307.g01.model.card.extractor;

import ulb.infof307.g01.model.card.extractor.CardExtractor;
import ulb.infof307.g01.model.deck.Deck;

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
