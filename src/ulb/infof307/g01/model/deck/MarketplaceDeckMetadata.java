package ulb.infof307.g01.model.deck;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;
import java.util.*;

public record MarketplaceDeckMetadata(UUID id,
                                      String name,
                                      String color,
                                      String image,
                                      int cardCount,
                                      List<Tag> tags,
                                      String owner,
                                      int rating,
                                      int downloads,
                                      int deckHashCode) {

    public MarketplaceDeckMetadata(Deck deck, String owner, int rating, int downloads) {
        this(deck.getId(),
                deck.getName(),
                deck.getColor(),
                deck.getImage(),
                deck.cardCount(),
                deck.getTags(),
                owner,
                rating,
                downloads,
                deck.hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarketplaceDeckMetadata that = (MarketplaceDeckMetadata) o;
        return cardCount == that.cardCount
                && deckHashCode == that.deckHashCode
                && id.equals(that.id)
                && name.equals(that.name)
                && color.equals(that.color)
                && image.equals(that.image)
                && tags.equals(that.tags)
                && owner.equals(that.owner)
                && rating == that.rating
                && downloads == that.downloads;
    }

}
