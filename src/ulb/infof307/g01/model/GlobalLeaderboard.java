package ulb.infof307.g01.model;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Consumer;

public class GlobalLeaderboard implements Iterable<Map<String, String>>  {

    public static final String ENTRY_USERNAME = "username";
    public static final String ENTRY_TOTAL_SCORE = "total_score";

    private final List<Map<String, String>> leaderboard;

    public GlobalLeaderboard(List<Map<String, String>> allUserDeckScore) {
        this.leaderboard = allUserDeckScore;
        this.leaderboard.sort((a, b) -> Integer.compare(
                Integer.parseInt(b.get(ENTRY_TOTAL_SCORE)),
                Integer.parseInt(a.get(ENTRY_TOTAL_SCORE))));
    }

    public String getUserScore(String username) {
        for (Map<String, String> userScore : leaderboard) {
            if (userScore.get("username").equals(username)) {
                return userScore.get("total_score");
            }
        }
        return "0";
    }

    public String getUserRank(String username) {
        for (int i = 0; i < leaderboard.size(); i++) {
            if (leaderboard.get(i).get("username").equals(username)) {
                return String.valueOf(i + 1);
            }
        }
        return "0";
    }

    @Override
    public Iterator<Map<String, String>> iterator() {
        return leaderboard.iterator();
    }

    @Override
    public void forEach(Consumer<? super Map<String, String>> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<Map<String, String>> spliterator() {
        return Iterable.super.spliterator();
    }
}
