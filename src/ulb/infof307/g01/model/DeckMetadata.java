package ulb.infof307.g01.model;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record DeckMetadata(UUID id,
                           String name,
                           String color,
                           int cardCount,
                           List<Tag> tags,
                           int deckHashCode) {

    public DeckMetadata(DeckMetadata deckMetadata) {
        this(deckMetadata.id,
             deckMetadata.name,
             deckMetadata.color,
             deckMetadata.cardCount,
             List.copyOf(deckMetadata.tags),
             deckMetadata.deckHashCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeckMetadata that = (DeckMetadata) o;
        return cardCount == that.cardCount
                && deckHashCode == that.deckHashCode
                && id.equals(that.id)
                && name.equals(that.name)
                && color.equals(that.color)
                && tags.equals(that.tags);
    }

}