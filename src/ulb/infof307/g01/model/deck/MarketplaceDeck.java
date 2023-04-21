package ulb.infof307.g01.model.deck;

import ulb.infof307.g01.model.card.Card;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MarketplaceDeck extends Deck {

    private final String owner;
    private final int rating;
    private final int downloads;

    public MarketplaceDeck(String name,
                           UUID id,
                           List<Card> cards,
                           List<Tag> tags,
                           String color,
                           String image,
                           String owner,
                           int rating,
                           int downloads) {
        super(name, id, cards, tags, color, image);
        this.owner = owner;
        this.rating = rating;
        this.downloads = downloads;
    }

    public MarketplaceDeck(Deck deck, String owner, int rating, int downloads) {
        super(deck);
        this.owner = owner;
        this.rating = rating;
        this.downloads = downloads;
    }

    public int getRating() {
        return rating;
    }

    public int getDownloads() {
        return downloads;
    }

    public String getOwner() {
        return owner;
    }

    public MarketplaceDeckMetadata getMarketplaceMetadata() {
        return new MarketplaceDeckMetadata(id,
                name,
                color,
                image,
                cards.size(),
                tags,
                owner,
                rating,
                downloads,
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
                && this.owner.equals(other.owner)
                && this.rating == other.rating
                && this.downloads == other.downloads;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, cards, tags, color, image, owner, rating, downloads);
    }
}
