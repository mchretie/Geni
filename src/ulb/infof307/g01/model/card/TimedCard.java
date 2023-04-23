package ulb.infof307.g01.model.card;

import com.google.gson.annotations.Expose;
import java.util.Objects;
import java.util.UUID;

public class TimedCard extends Card{

    @Expose
    protected Integer countdownTime = 30;

    public TimedCard() { super(); }

    protected TimedCard(String front) {
        super( front);
    }

    protected TimedCard(UUID uuid, UUID deckId, String front, Integer countdownTime) {
        super(uuid, deckId, front);
        this.countdownTime = countdownTime;
    }

    public void setCountdownTime(Integer countdownTime) {
        this.countdownTime = countdownTime;
    }

    public Integer getCountdownTime() {
        return countdownTime;
    }
}
