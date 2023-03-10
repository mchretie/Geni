package ulb.infof307.g01.model;

import java.util.UUID;
import java.util.Objects;

public class Card {
    private final UUID id;
    private UUID deckId;
    private String front;
    private String back;

    public Card(String front, String back) {
        this(UUID.randomUUID(), null, front, back);
    }

    public Card(UUID uuid, UUID deckId, String front, String back)
    {
        this.id = uuid;
        this.deckId = deckId;
        this.front = front;
        this.back = back;
    }

    public String getFront() { return front; }
    public String getBack() { return back; }
    public UUID getId() { return id; }
    public UUID getDeckId() { return deckId; }

    public void setFront(String front) { this.front = front; }
    public void setBack(String back) { this.back = back; }
    public void setDeckId(UUID deckId) { this.deckId = deckId; }

    public boolean equals(Object o)
    {
        if (o == this)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;

        Card other = (Card)o;
        return id.equals(other.id)
            && (deckId == other.deckId || deckId.equals(other.deckId))
            && front.equals(other.front)
            && back.equals(other.back);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, deckId, front, back);
    }
}
