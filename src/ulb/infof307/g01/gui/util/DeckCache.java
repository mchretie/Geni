package ulb.infof307.g01.gui.util;

import ulb.infof307.g01.model.deck.Deck;
import ulb.infof307.g01.model.deck.DeckMetadata;

import java.util.*;

import static java.util.stream.Collectors.toList;


public class DeckCache {

    private final Map<UUID, Deck> decks = new HashMap<>();
    private final Map<UUID, DeckMetadata> decksMetadata = new HashMap<>();
    private final Set<UUID> allDecksIds = new HashSet<>();

    public DeckCache(Collection<DeckMetadata> allDecksMetadata) {
        for (DeckMetadata deckMetadata : allDecksMetadata) {
            var deckId = deckMetadata.id();
            this.allDecksIds.add(deckId);
            this.decksMetadata.put(deckId, deckMetadata);
        }
    }

    public void updateDeckMetadata(DeckMetadata deckMetadata) {
        var deckId = deckMetadata.id();
        decksMetadata.put(deckId, deckMetadata);
        allDecksIds.add(deckId);
    }

    public void updateDeck(Deck deck) {
        decks.put(deck.getId(), deck);
        updateDeckMetadata(deck.getMetadata());
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
