package ulb.infof307.g01.model;


import java.util.List;
import java.util.UUID;

public class DeckMetadata {
    private final UUID id;
    private final String color;
    private final int cardCount;
    private final List<Tag> tags;
    private final int deckHashCode;

    public DeckMetadata(UUID id, String color, int cardCount, List<Tag> tags, int deckHashCode) {
        this.id = id;
        this.color = color;
        this.cardCount = cardCount;
        this.tags = tags;
        this.deckHashCode = deckHashCode;
    }

    public UUID getId() {
        return id;
    }

    public String getColor() {
        return color;
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
}