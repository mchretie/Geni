package ulb.infof307.g01.model.deck;

import java.util.*;

public record MarketplaceDeckMetadata(UUID id,
                                      String name,
                                      boolean isPublic,
                                      String color,
                                      String image,
                                      String colorName,
                                      int cardCount,
                                      List<Tag> tags,
                                      String owner,
                                      int rating,
                                      int downloads,
                                      int deckHashCode) {

    public MarketplaceDeckMetadata(Deck deck, String owner, int rating, int downloads) {
        this(deck.getId(),
                deck.getName(),
                deck.isPublic(),
                deck.getColor(),
                deck.getImage(),
                deck.getColorName(),
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
                && isPublic == that.isPublic
                && color.equals(that.color)
                && image.equals(that.image)
                && colorName.equals(that.colorName)
                && tags.equals(that.tags)
                && owner.equals(that.owner)
                && rating == that.rating
                && downloads == that.downloads;
    }

}
