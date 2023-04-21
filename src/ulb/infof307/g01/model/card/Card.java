package ulb.infof307.g01.model.card;

import com.google.gson.annotations.Expose;

import java.util.UUID;

public class Card {
    private UUID deckId;
    private UUID id;

    @Expose
    private String front;

    @Expose
    protected String cardType;

    protected Card() {
        this.front = "Avant";
        this.id = UUID.randomUUID();
    }

    protected Card (String front) {
        this.id = UUID.randomUUID();
        this.front = front;
    }

    protected Card(UUID uuid, UUID deckId, String front) {
        this.id = uuid;
        this.deckId = deckId;
        this.front = front;
    }

    public String getFront() { return front; }
    public UUID getId() { return id; }
    public UUID getDeckId() { return deckId; }

    public void generateNewId() {
        this.id = UUID.randomUUID();
    }
    public void setFront(String front) { this.front = front; }
    public void setDeckId(UUID deckId) { this.deckId = deckId; }
}
