package ulb.infof307.g01.model.deck;

import ulb.infof307.g01.model.card.Card;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MarketplaceDeck extends Deck {

    private final int rating;
    private final int download;

    public MarketplaceDeck(String name,
                           UUID id,
                           List<Card> cards,
                           List<Tag> tags,
                           String color,
                           String image,
                           int rating,
                           int download) {
        super(name, id, cards, tags, color, image);
        this.rating = rating;
        this.download = download;
    }

    public MarketplaceDeckMetadata getMarketplaceMetadata() {
        return new MarketplaceDeckMetadata(id,
                name,
                color,
                image,
                cards.size(),
                tags,
                rating,
                download,
                hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;

        MarketplaceDeck other = (MarketplaceDeck) obj;
        return this.id.equals(other.id)
                && this.name.equals(other.name)
                && this.cards.equals(other.cards)
                && this.tags.equals(other.tags)
                && this.color.equals(other.color)
                && this.image.equals(other.image)
                && this.rating == other.rating
                && this.download == other.download;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, cards, tags, color, image, rating, download);
    }
}
