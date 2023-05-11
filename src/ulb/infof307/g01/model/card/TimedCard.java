package ulb.infof307.g01.model.card;

import com.google.gson.annotations.Expose;

import java.util.Objects;
import java.util.UUID;

public abstract class TimedCard extends Card {

    @Expose
    protected Integer countdownTime = 10;

    protected TimedCard() {
        super();
    }

    protected TimedCard(UUID uuid, UUID deckId, String front,
                        Integer countdownTime) {
        super(uuid, deckId, front);
        this.countdownTime = countdownTime;
    }

    public void setCountdownTime(Integer countdownTime) {
        this.countdownTime = countdownTime;
    }

    public Integer getCountdownTime() {
        return countdownTime;
    }

    @Override
    public boolean isCompetitive() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass())
            return false;

        TimedCard other = (TimedCard) o;
        return this.countdownTime.equals(other.getCountdownTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(countdownTime);
    }
}
