package ulb.infof307.g01.gui.util;

import ulb.infof307.g01.model.deck.Deck;
import ulb.infof307.g01.model.deck.DeckMetadata;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class DeckCache {

    Map<UUID, Deck> decks = new HashMap<>();
    Map<UUID, DeckMetadata> decksMetadata = new HashMap<>();
    Set<UUID> allDecksIds = new HashSet<>();

    public DeckCache(Collection<DeckMetadata> allDecksMetadata) {
        for (DeckMetadata deckMetadata : allDecksMetadata) {
            var deckId = deckMetadata.id();
            this.allDecksIds.add(deckId);
            this.decksMetadata.put(deckId, deckMetadata);
        }
    }

    public void updateDeck(Deck deck) {
        var deckId = deck.getId();
        decks.put(deckId, deck);
        decksMetadata.put(deckId, deck.getMetadata());
        allDecksIds.add(deckId);
    }

    public void removeDeck(DeckMetadata deckMetadata) {
        var deckId = deckMetadata.id();
        decks.remove(deckId);
        decksMetadata.remove(deckId);
        allDecksIds.remove(deckId);
    }

    public Optional<Deck> getDeck(DeckMetadata deckMetadata) {
        return decks.containsKey(deckMetadata.id()) ?
                Optional.of(decks.get(deckMetadata.id())) :
                Optional.empty();
    }

    public List<DeckMetadata> getAllDecksMetadata() {
        return allDecksIds.stream()
                .map(deckId -> new DeckMetadata(decksMetadata.get(deckId)))
                .collect(toList());
    }
}
