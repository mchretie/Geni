package ulb.infof307.g01.model.leaderboard;

import java.util.*;
import java.util.function.Consumer;

public class GlobalLeaderboard implements Iterable<GlobalLeaderboardEntry> {

    private final Map<String, GlobalLeaderboardEntry> leaderboard;
    private final List<GlobalLeaderboardEntry> leaderboardEntries;

    public GlobalLeaderboard(List<GlobalLeaderboardEntry> leaderboardEntries) {
        this.leaderboard = new HashMap<>();
        leaderboardEntries.forEach(entry -> leaderboard.put(entry.getUsername(), entry));

        leaderboardEntries.sort(Comparator.reverseOrder());

        for (int i = 0; i < leaderboardEntries.size(); i++) {
            leaderboardEntries.get(i).setRank(String.valueOf(i + 1));
        }

        this.leaderboardEntries = leaderboardEntries;
    }

    private GlobalLeaderboardEntry getEntry(String username) {
        return leaderboard.get(username);
    }

    public String getUserScore(String username) {
        return getEntry(username) == null ? "0" : getEntry(username).getTotalScore();
    }

    public String getUserRank(String username) {
        return getEntry(username) == null ? "Pas de classement" : getEntry(username).getRank();
    }

    @Override
    public Iterator<GlobalLeaderboardEntry> iterator() {
        return leaderboardEntries.iterator();
    }

    @Override
    public void forEach(Consumer<? super GlobalLeaderboardEntry> action) {
        Iterable.super.forEach(action);
    }
}
