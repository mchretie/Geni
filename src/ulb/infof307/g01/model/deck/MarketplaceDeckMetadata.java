package ulb.infof307.g01.model.deck;

import java.util.List;
import java.util.UUID;

public record MarketplaceDeckMetadata(UUID id,
                                      String name,
                                      String color,
                                      String image,
                                      int cardCount,
                                      List<Tag> tags,
                                      int rating,
                                      int download,
                                      int deckHashCode) {

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
                && rating == that.rating
                && download == that.download;
    }
}
