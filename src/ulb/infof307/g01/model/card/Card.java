package ulb.infof307.g01.model.card;

import com.google.gson.annotations.Expose;
import ulb.infof307.g01.model.card.visitor.CardVisitor;

import java.util.UUID;

public abstract class Card {
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

    protected Card(String front) {
        this.id = UUID.randomUUID();
        this.front = front;
    }

    protected Card(UUID uuid, UUID deckId, String front) {
        this.id = uuid;
        this.deckId = deckId;
        this.front = front;
    }

    public void generateNewId() {
        this.id = UUID.randomUUID();
    }

    public String getFront() {
        return front;
    }

    public UUID getId() {
        return id;
    }

    public UUID getDeckId() {
        return deckId;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public void setDeckId(UUID deckId) {
        this.deckId = deckId;
    }

    public abstract void accept(CardVisitor visitor);
}
