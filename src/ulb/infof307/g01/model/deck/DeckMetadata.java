package ulb.infof307.g01.model.deck;


import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record DeckMetadata(UUID id,
                           String name,
                           boolean isPublic,
                           String color,
                           String image,
                           String colorName,
                           int cardCount,
                           List<Tag> tags,
                           Score bestScore,
                           int deckHashCode) {

    @SuppressWarnings("CopyConstructorMissesField")
    public DeckMetadata(DeckMetadata deckMetadata) {
        this(deckMetadata.id,
                deckMetadata.name,
                deckMetadata.isPublic,
                deckMetadata.color,
                deckMetadata.image,
                deckMetadata.colorName,
                deckMetadata.cardCount,
                List.copyOf(deckMetadata.tags),
                deckMetadata.bestScore,
                deckMetadata.deckHashCode);
    }

    public static DeckMetadata fromJson(JsonObject deckMetadataJson) {
        return new Gson().fromJson(deckMetadataJson.toString(),
                DeckMetadata.class);
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
                && image.equals(that.image)
                && colorName.equals(that.colorName)
                && tags.equals(that.tags)
                && Objects.equals(bestScore, that.bestScore);
    }

}