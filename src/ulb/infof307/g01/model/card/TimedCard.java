package ulb.infof307.g01.model.card;

import com.google.gson.annotations.Expose;

import java.util.UUID;

public abstract class TimedCard extends Card {

    @Expose
    protected Integer countdownTime = 10;

    public TimedCard() {
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
        Integer countdownTime = this.countdownTime;

        if (o == null || o.getClass() != this.getClass())
            return false;

        TimedCard other = (TimedCard) o;

        return countdownTime.equals(other.getCountdownTime());
    }

}
