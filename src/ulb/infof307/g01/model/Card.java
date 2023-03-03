package ulb.infof307.g01.model;

import java.util.UUID;

public class Card {
    private final UUID id = UUID.randomUUID();
    private String front;
    private String back;

    public Card() {}

    public Card(String front, String back) {
        this.front = front;
        this.back = back;
    }

    public String getFront() { return front; }
    public String getBack() { return back; }
    public UUID getId() { return id; }

    public void setFront(String front) { this.front = front; }
    public void setBack(String back) { this.back = back; }
}
