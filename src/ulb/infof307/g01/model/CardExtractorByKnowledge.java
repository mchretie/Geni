package ulb.infof307.g01.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardExtractorByKnowledge extends CardExtractor {

    public CardExtractorByKnowledge(Deck deck) {
        super(deck);
    }
    @Override
    void sortDeck() {
        Collections.sort(this.sortedCards);
    }

}
