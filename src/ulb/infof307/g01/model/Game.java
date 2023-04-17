package ulb.infof307.g01.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public record Game(Date timestamp, String deckName, String score) {

    public String getFormattedTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(timestamp);
    }
}
