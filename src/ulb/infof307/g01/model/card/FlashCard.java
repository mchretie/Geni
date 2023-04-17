package ulb.infof307.g01.model.card;

import com.google.gson.annotations.Expose;

import java.util.Objects;
import java.util.UUID;


public class FlashCard extends Card {
    @Expose
    private String back;

    public FlashCard(String front, String back) {
        super(front);
        this.back = back;
        this.cardType = "FlashCard";
    }

    public FlashCard(UUID uuid, UUID deckId, String front, String back) {
        super(uuid, deckId, front);
        this.back = back;
        this.cardType = "FlashCard";
    }


    public FlashCard(String front, String back, KnowledgeLevel knowledge) {
        super(front, knowledge);
        this.back = back;
        this.cardType = "FlashCard";
    }

    public String getBack() { return back; }

    public void setBack(String back) { this.back = back; }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), this.getDeckId(), this.getFront(), back);
    }

    @Override
    public boolean equals(Object o)
    {
        UUID id = this.getId();
        UUID deckId = this.getDeckId();
        String front = this.getFront();

        if (o == this)
            return true;
        if (o == null || o.getClass() != this.getClass())
            return false;

        FlashCard other = (FlashCard) o;
        return id.equals(other.getId())
                && (deckId == other.getDeckId() || deckId.equals(other.getDeckId()))
                && front.equals(other.getFront()) && back.equals(other.getBack());
    }

}
