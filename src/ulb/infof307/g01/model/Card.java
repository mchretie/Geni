package ulb.infof307.g01.model;

import com.google.gson.annotations.Expose;

import java.util.UUID;

public class Card {
    private UUID deckId;
    private UUID id;

    @Expose
    private String front;
    @Expose
    private KnowledgeLevel knowledge;

    @Expose
    protected String cardType;

    public enum KnowledgeLevel {
        NEVER_SEEN(0), VERY_BAD(1), BAD(2), AVERAGE(3), GOOD(4), VERY_GOOD(5);

        private final int value;

        KnowledgeLevel(int value) { this.value = value;}
        public int getValue() {
            return value;
        }
    }

    protected Card (String front) {
        this.id = UUID.randomUUID();
        this.front = front;
        this.knowledge = KnowledgeLevel.NEVER_SEEN;
    }

    protected Card(UUID uuid, UUID deckId, String front)
    {
        this.id = uuid;
        this.deckId = deckId;
        this.front = front;
        this.knowledge = KnowledgeLevel.NEVER_SEEN;
    }

    protected Card(String front, KnowledgeLevel knowledge) {
        this.id = UUID.randomUUID();
        this.front = front;
        this.knowledge = knowledge;
    }

    public KnowledgeLevel getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(KnowledgeLevel knowledge) {
        this.knowledge = knowledge;
    }

    public String getFront() { return front; }
    public UUID getId() { return id; }
    public UUID getDeckId() { return deckId; }

    public void setNewId() {
        this.id = UUID.randomUUID();
    }
    public void setFront(String front) { this.front = front; }
    public void setDeckId(UUID deckId) { this.deckId = deckId; }
}
