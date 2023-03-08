package ulb.infof307.g01.model;

import java.util.UUID;

public class Card {
    private final UUID id;
    private String front;
    private String back;

    public Card(String front, String back) {
        this(UUID.randomUUID(), front, back);
    }

    public Card(UUID uuid, String front, String back)
    {
        this.id = uuid;
        this.front = front;
        this.back = back;
    }

    public String getFront() { return front; }
    public String getBack() { return back; }
    public UUID getId() { return id; }

    public void setFront(String front) { this.front = front; }
    public void setBack(String back) { this.back = back; }

    public boolean equals(Object o)
    {
        if (o == this)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;

        Card other = (Card)o;
        return id.equals(other.id) && front.equals(other.front) &&
          back.equals(other.back);
    }
}
