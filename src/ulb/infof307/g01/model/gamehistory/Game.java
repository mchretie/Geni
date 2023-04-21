package ulb.infof307.g01.model.gamehistory;

import java.text.SimpleDateFormat;
import java.util.Date;

public record Game(Date timestamp, String deckName, String score) implements Comparable<Game> {

    public String getFormattedTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(timestamp);
    }

    public int getScore() {
        return Integer.parseInt(score);
    }

    @Override
    public int compareTo(Game o) {
        return o.timestamp().compareTo(timestamp());
    }
}
