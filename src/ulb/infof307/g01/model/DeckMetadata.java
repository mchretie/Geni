package ulb.infof307.g01.model;


import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class DeckMetadata {
    private final UUID id;
    private final String name;
    private final String color;
    private String image;
    private final int cardCount;
    private final List<Tag> tags;
    private final int deckHashCode;

    public DeckMetadata(UUID id, String name, String color, String image, int cardCount, List<Tag> tags, int deckHashCode) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.image = image;
        this.cardCount = cardCount;
        this.tags = tags;
        this.deckHashCode = deckHashCode;
    }

    public DeckMetadata(DeckMetadata deckMetadata) {
        this(deckMetadata.id,
                deckMetadata.name,
                deckMetadata.color,
                deckMetadata.image,
                deckMetadata.cardCount,
                List.copyOf(deckMetadata.tags),
                deckMetadata.deckHashCode);
    }

    public static DeckMetadata fromJson(JsonObject deckMetadataJson) {
        return new Gson().fromJson(deckMetadataJson.toString(),
                DeckMetadata.class);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCardCount() {
        return cardCount;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public int getDeckHashCode() {
        return deckHashCode;
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
                && tags.equals(that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, color, image, cardCount, tags, deckHashCode);
    }
}