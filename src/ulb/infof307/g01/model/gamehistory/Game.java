package ulb.infof307.g01.model.gamehistory;

import java.text.SimpleDateFormat;

public record Game(long timestamp, String deckName, String score) implements Comparable<Game> {

    public String getFormattedTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(timestamp);
    }

    public int getScore() {
        return Integer.parseInt(score);
    }

    @Override
    public int compareTo(Game o) {
        return Long.compare(timestamp, o.timestamp);
    }
}
