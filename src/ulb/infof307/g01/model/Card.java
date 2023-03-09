package ulb.infof307.g01.model;

import java.util.UUID;

public class Card implements Comparable<Card> {
    private final UUID id = UUID.randomUUID();
    private String front;
    private String back;
    private KnowledgeLevel knowledge;

    public enum KnowledgeLevel {
        NEVER_SEEN(0), VERY_BAD(1), BAD(2), AVERAGE(3), GOOD(4), VERY_GOOD(5);

        private final int value;

        KnowledgeLevel(int value) { this.value = value;}
        public int getValue() {
            return value;
        }
    }

    public Card() {}

    public Card(String front, String back) {
        this.front = front;
        this.back = back;
        this.knowledge = KnowledgeLevel.NEVER_SEEN;
    }

    public Card(String front, String back, KnowledgeLevel knowledge) {
        this.front = front;
        this.back = back;
        this.knowledge = knowledge;
    }

    public KnowledgeLevel getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(KnowledgeLevel knowledge) {
        this.knowledge = knowledge;
    }

    public String getFront() { return front; }
    public String getBack() { return back; }
    public UUID getId() { return id; }

    public void setFront(String front) { this.front = front; }
    public void setBack(String back) { this.back = back; }

    @Override
    public int compareTo(Card otherCard) {
        return Integer.compare(this.knowledge.getValue(), otherCard.getKnowledge().getValue());
    }
}
